import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.type.constructed.BACnetArray;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.EngineeringUnits;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.*;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.util.sero.ByteQueue;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.smi.OctetString;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EatonUPS implements CanSendSNMP {


    private String deviceName;
    private CommunityTarget comtarget;
    private Map<Integer, BacnetData> bacnetDataMap;

    public EatonUPS(String ipAddress, String port, String deviceName) {

        this.deviceName = deviceName;

        // Create Target Address object
        comtarget = new CommunityTarget();
        comtarget.setCommunity(new OctetString("private"));
        comtarget.setVersion( SnmpConstants.version1);
        comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
        comtarget.setRetries(2);
        comtarget.setTimeout(1000);

        try {
            initializeBacnet ();
        } catch (BACnetErrorException e) {
            e.printStackTrace();
        }
    }

    private void initializeBacnet() throws BACnetErrorException {
        bacnetDataMap = new HashMap<>();

        bacnetDataMap.put(1, new BacnetData("upsOutputSource ", // other(1), none(2), normal(3), bypass(4), battery(5), booster(6),reducer(7)
                UnsignedInteger.ZERO,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                new BACnetArray<>( //
                        new CharacterString("Неизв."), //
                        new CharacterString("Отсутсв."), //
                        new CharacterString("Норма"),
                        new CharacterString("Байпас"), //
                        new CharacterString("Батарея"), //
                        new CharacterString("Booster"), //
                        new CharacterString("Reducer")),
                ObjectType.multiStateValue));

        bacnetDataMap.put(2, new BacnetData("upsAlarmsPresent ",
                new Real(0),
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.analogValue));

        bacnetDataMap.put(3, new BacnetData("upsEnvAmbientTemp ",
                new Real(0),
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.analogValue));

        bacnetDataMap.put(4, new BacnetData("upsBatCapacity ",
                new Real(0),
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.analogValue));

        bacnetDataMap.put(5, new BacnetData("upsBatTimeRemaining ",
                new Real(0),
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.analogValue));

        bacnetDataMap.put(6, new BacnetData("upsBatteryAbmStatus ",
                UnsignedInteger.ZERO,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                new BACnetArray<>( //
                        new CharacterString("Зарядка"), //
                        new CharacterString("Разрядка"), //
                        new CharacterString("batteryFloating"),
                        new CharacterString("batteryResting"), //
                        new CharacterString("unknown"), //
                        new CharacterString("Батарея отключена"), //
                        new CharacterString("Батарея тестируется"),
                        new CharacterString("Треюуется ТО батареи")),
                ObjectType.multiStateValue));

        bacnetDataMap.put(7, new BacnetData("onBattery ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(8, new BacnetData("onLine ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(9, new BacnetData("Bypass ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(10, new BacnetData("SystemDescription ",
                null,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.characterstringValue));
        bacnetDataMap.put(11, new BacnetData("SystemContact ",
                null,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.characterstringValue));
        bacnetDataMap.put(12, new BacnetData("SystemLocation ",
                null,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.characterstringValue));
    }
    @Override
    public void updateState(TransportMapping transport) {
        // Create Snmp object for sending data to Agent

        Snmp snmp;
        snmp = new Snmp(transport);

        // Create the PDU object
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.33.1.4.1.0")));//0
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.33.1.6.1.0")));//1
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.534.1.6.1.0")));//2
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.534.1.2.4.0")));//3
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.534.1.2.1.0")));//4
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.534.1.2.5.0")));//5d
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.1.1.0")));//6
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.1.4.0")));//7
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.1.6.0")));//8
        pdu.setType(PDU.GET);
        pdu.setRequestID(new Integer32(1));

        try {
            ResponseEvent response = snmp.get(pdu, comtarget);
            // Process Agent Response
            if (response != null) {
                PDU responsePDU = response.getResponse();
                //Extract boolean value from OctetString
                bacnetDataMap.get(1).setPresentValue(new UnsignedInteger(responsePDU.getVariableBindings().get(0).getVariable().toInt()));
                bacnetDataMap.get(2).setPresentValue(new Real(responsePDU.getVariableBindings().get(1).getVariable().toInt()));
                bacnetDataMap.get(3).setPresentValue(new Real(responsePDU.getVariableBindings().get(2).getVariable().toInt()));
                bacnetDataMap.get(4).setPresentValue(new Real(responsePDU.getVariableBindings().get(3).getVariable().toInt()));
                bacnetDataMap.get(5).setPresentValue(new Real(responsePDU.getVariableBindings().get(4).getVariable().toInt()));
                bacnetDataMap.get(6).setPresentValue(new UnsignedInteger(responsePDU.getVariableBindings().get(5).getVariable().toInt()));
                bacnetDataMap.get(7).setPresentValue((responsePDU.getVariableBindings().get(0).getVariable().toInt() == 5) ? BinaryPV.active : BinaryPV.inactive);
                bacnetDataMap.get(8).setPresentValue((responsePDU.getVariableBindings().get(0).getVariable().toInt() == 3) ? BinaryPV.active : BinaryPV.inactive);
                bacnetDataMap.get(9).setPresentValue((responsePDU.getVariableBindings().get(0).getVariable().toInt() == 4) ? BinaryPV.active : BinaryPV.inactive);
                bacnetDataMap.get(10).setPresentValue(new CharacterString(responsePDU.getVariableBindings().get(6).getVariable().toString()));
                bacnetDataMap.get(11).setPresentValue(new CharacterString(responsePDU.getVariableBindings().get(7).getVariable().toString()));
                bacnetDataMap.get(12).setPresentValue(new CharacterString(responsePDU.getVariableBindings().get(8).getVariable().toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        snmp.removeTransportMapping(transport);
    }

    @Override
    public Map<Integer, BacnetData> getBacnetData() {
        return bacnetDataMap;
    }

    @Override
    public String getDeviceName() {
        return this.deviceName;
    }
}

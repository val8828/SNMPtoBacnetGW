import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.type.constructed.BACnetArray;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.EngineeringUnits;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.*;
import com.serotonin.bacnet4j.type.primitive.Boolean;
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

public class ApcGalaxy7000UPS implements CanSendSNMP{

    private String deviceName;
    private CommunityTarget comtarget;
    private Map<Integer, BacnetData> bacnetDataMap;

    public ApcGalaxy7000UPS(String  ipAddress, String  port, String deviceName ) {
        this.deviceName = deviceName;

        // Create Target Address object
        comtarget = new CommunityTarget();
        comtarget.setCommunity(new OctetString("private"));
        comtarget.setVersion( SnmpConstants.version1);
        comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
        comtarget.setRetries(2);
        comtarget.setTimeout(1000);

        try {
            initializeBacnet();
        } catch (BACnetErrorException e) {
            e.printStackTrace();
        }

    }

    private void initializeBacnet() throws BACnetErrorException {
        bacnetDataMap = new HashMap<>();

        bacnetDataMap.put(1, new BacnetData("abnormalConditionPresent ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(2, new BacnetData("onBattery ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(3, new BacnetData("lowBattery ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(4, new BacnetData("onLine ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(5, new BacnetData("replaceBattery ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(6, new BacnetData("serialCommunicationEstablished ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(7, new BacnetData("aVRBoostActive ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(8, new BacnetData("aVRTrimActive ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(9, new BacnetData("overload ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(10, new BacnetData("runtimeCalibration ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(11, new BacnetData("batteriesDischarged ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(12, new BacnetData("manualBypass ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(13, new BacnetData("softwareBypass ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(14, new BacnetData("inBypassDueToInternalFault ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(15, new BacnetData("inBypassDueToSupplyFailure ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(16, new BacnetData("inBypassDueToFanFailure ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(17, new BacnetData("sleepingOnATimer ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(18, new BacnetData("sleepingUntilUtilityPowerReturns ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(19, new BacnetData("on ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(20, new BacnetData("rebooting ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(21, new BacnetData("batteryCommunicationLost ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(22, new BacnetData("gracefulShutdownInitiated ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(23, new BacnetData("smartBoostOrSmartTrimFault ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(24, new BacnetData("badOutputVoltage ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(25, new BacnetData("batteryChargerFailure ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(26, new BacnetData("highBatteryTemperature ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(27, new BacnetData("warningBatteryTemperature ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(28, new BacnetData("criticalBatteryTemperature ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(29, new BacnetData("selfTestInProgress ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(30, new BacnetData("lowBatteryOnBattery ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(31, new BacnetData("gracefulShutdownIssuedByUpstreamDevice ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(32, new BacnetData("gracefulShutdownIssuedByDownstreamDevice ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(33, new BacnetData("noBatteriesAttached ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(34, new BacnetData("synchronizedCommandIsInProgress ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(35, new BacnetData("synchronizedSleepingCommandIsInProgress ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(36, new BacnetData("synchronizedRebootingCommandIsInProgress ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(37, new BacnetData("inverterDCImbalance ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(38, new BacnetData("transferRelayFailure ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(39, new BacnetData("shutdownOrUnableToTransfer ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(40, new BacnetData("lowBatteryShutdown ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(41, new BacnetData("electronicUnitFanFailure ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(42, new BacnetData("mainRelayFailure ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(43, new BacnetData("bypassRelayFailure ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(44, new BacnetData("temporarBypass ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(45, new BacnetData("highInternalTemperature ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(46, new BacnetData("batteryTemperatureSensorFault ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(47, new BacnetData("inputOutOfRangeForBypass ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(48, new BacnetData("dCBusOvervoltage ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(49, new BacnetData("pFCFailure ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(50, new BacnetData("criticalHardwareFault ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(51, new BacnetData("greenModeECOMode ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(52, new BacnetData("hotStandby ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(53, new BacnetData("emergencyPowerOff ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(54, new BacnetData("loadAlarmViolation ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(55, new BacnetData("bypassPhaseFault ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(56, new BacnetData("uPSInternalCommunicationFailure ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue,
                BinaryPV.active));
        bacnetDataMap.put(57, new BacnetData("efficiencyBoosterMode ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(58, new BacnetData("off ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(59, new BacnetData("standby ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(60, new BacnetData("minorOrEnvironmentAlarm ",
                BinaryPV.inactive,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.binaryValue));
        bacnetDataMap.put(61, new BacnetData("temperature ",
                new Real(0),
                EngineeringUnits.degreesCelsius,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.analogValue));
        bacnetDataMap.put(62, new BacnetData("batteryPercent ",
                new Real(0),
                EngineeringUnits.percent,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.analogValue));
        bacnetDataMap.put(63, new BacnetData("runtimeRemaining ",
                new Real(0),
                EngineeringUnits.seconds,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.analogValue));
        bacnetDataMap.put(64, new BacnetData("batteryStatus ",
                UnsignedInteger.ZERO,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                new BACnetArray<>( //
                        new CharacterString("unknown"), //
                        new CharacterString("batteryNormal"), //
                        new CharacterString("batteryLow"),
                        new CharacterString("batteryInFaultCondition")),
                ObjectType.multiStateValue));
        bacnetDataMap.put(65, new BacnetData("SystemDescription ",
                null,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.characterstringValue));
        bacnetDataMap.put(66, new BacnetData("SystemContact ",
                null,
                EngineeringUnits.noUnits,
                Boolean.FALSE,
                EventState.normal,
                new StatusFlags(false, false, false, false),
                null,
                ObjectType.characterstringValue));
        bacnetDataMap.put(67, new BacnetData("SystemLocation ",
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
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.318.1.1.1.11.1.1.0")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.318.1.1.1.2.2.2.0")));//Temperature
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.318.1.1.1.2.2.1.0")));//Battery percent
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.318.1.1.1.2.2.3.0")));//runtime remaining
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.318.1.1.1.2.1.1.0")));//Battery status
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.1.5.0")));//6
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
                for(int i=0;i<60;i++){
                    if (i<=bacnetDataMap.size() && bacnetDataMap.get(i+1)!= null) {
                        Character currentCharacter = responsePDU.getVariableBindings().get(0).getVariable().toString().charAt(i);
                        bacnetDataMap.get(i+1).setPresentValue(currentCharacter.equals('1') ? BinaryPV.active : BinaryPV.inactive);
                    }
                }
                bacnetDataMap.get(61).setPresentValue(new Real(responsePDU.getVariableBindings().get(1).getVariable().toInt()));
                bacnetDataMap.get(62).setPresentValue(new Real(responsePDU.getVariableBindings().get(2).getVariable().toInt()));
                bacnetDataMap.get(63).setPresentValue(new Real(responsePDU.getVariableBindings().get(3).getVariable().toInt()));
                bacnetDataMap.get(64).setPresentValue(new UnsignedInteger(responsePDU.getVariableBindings().get(4).getVariable().toInt()));
                bacnetDataMap.get(65).setPresentValue(new CharacterString(responsePDU.getVariableBindings().get(5).getVariable().toString()));
                bacnetDataMap.get(66).setPresentValue(new CharacterString(responsePDU.getVariableBindings().get(6).getVariable().toString()));
                bacnetDataMap.get(67).setPresentValue(new CharacterString(responsePDU.getVariableBindings().get(7).getVariable().toString()));
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

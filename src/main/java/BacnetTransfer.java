import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.obj.*;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.*;
import com.serotonin.bacnet4j.type.enumerated.*;
import com.serotonin.bacnet4j.type.primitive.*;
import com.serotonin.bacnet4j.type.primitive.Boolean;

import java.util.ArrayList;

public class BacnetTransfer extends Thread {
    private LocalDevice localDevice;

    private ArrayList<CanSendSNMP> snmp;

    BacnetTransfer(ArrayList<CanSendSNMP> snmpI) {
        this.snmp = snmpI;
        try {

            String hostAddress = "10.2.0.64";
            String macAddress = "00:0C:29:FF:09:4C";
            String[] macAddressParts = macAddress.split(":");

            // convert hex string to byte values
            byte[] macAddressBytes = new byte[6];
            for(int i=0; i<6; i++){
                Integer hex = Integer.parseInt(macAddressParts[i], 16);
                macAddressBytes[i] = hex.byteValue();
            }

            IpNetwork network = new IpNetworkBuilder().withPort(47808).withSubnet(hostAddress, 24).build();
            network.enableBBMD();
            Transport transport = new DefaultTransport(network);

            // create device with random device number
            int localDeviceID = 16414;//10000 + (int) ( Math.random() * 10000);
            localDevice = new LocalDevice(localDeviceID, transport);
            localDevice.initialize();
            for(int i = 1; i <= 16; i++) {
                NotificationClassObject ntfl = new NotificationClassObject(localDevice,
                        i,
                        "NOTIFCL"+i,
                        new BACnetArray<>(3, new UnsignedInteger(255)),
                        new EventTransitionBits(true, true, true));
                ntfl.writePropertyInternal(PropertyIdentifier.description,new CharacterString("NOTIFCL"+i))

                        .writePropertyInternal(PropertyIdentifier.recipientList,
                        new Destination(new Recipient(new ObjectIdentifier(ObjectType.device,1048577)),//macAddressBytes)),
                                new UnsignedInteger(7),
                                Boolean.TRUE,
                                new EventTransitionBits(true,true,true)));

                ntfl.supportIntrinsicReporting(new EventTransitionBits(true, true, true),
                        NotifyType.alarm);
            }

            // create sample BACnet object
            int i = 16;

                for (CanSendSNMP currentDevice : snmp) {
                    for (BacnetData currentBacnetData : currentDevice.getBacnetData().values()) {
                        i++;
                        switch (currentBacnetData.getObjectType().intValue()) {
                            case 2:
                                getDefaultAnalogObject(i,
                                        (currentDevice.getDeviceName() + currentBacnetData.getObjectName()),
                                         currentBacnetData.getObjectName(),
                                        (EngineeringUnits) currentBacnetData.getUnits());
                                break;
                            case 5:
                                getDefaultBoolObject(i,
                                        (currentDevice.getDeviceName() + currentBacnetData.getObjectName()),
                                        (BinaryPV) currentBacnetData.getAlarmValue(),
                                        currentBacnetData.getObjectName());
                                break;
                            case 19:
                                getDefaultMultistateObject(i,
                                        (currentDevice.getDeviceName() + currentBacnetData.getObjectName()),
                                        currentBacnetData.getObjectName(), (BACnetArray<CharacterString>) currentBacnetData.getStateText());
                                break;
                            case 40:
                                getDefaultCharacterstringObject(i,
                                        (currentDevice.getDeviceName() + currentBacnetData.getObjectName()),
                                        currentBacnetData.getObjectName());
                                break;
                        }
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        updatePresentValue();
    }

    public void run() {
        while(true)
            try{
                sleep(5000);
                updatePresentValue();
            }catch (Exception e) {
                e.printStackTrace();
            }
    }

    //Обновляем текущее значение
    private void updatePresentValue(){
        try {
            for (CanSendSNMP currentSNMPDevice : snmp) {
                for (BacnetData currentBacnetData : currentSNMPDevice.getBacnetData().values()) {
                    for(BACnetObject currentBacnetObject : localDevice.getLocalObjects()){
                        if(!currentBacnetData.getObjectType().equals(ObjectType.notificationClass)){
                            String variableName = currentSNMPDevice.getDeviceName() + currentBacnetData.getObjectName();
                            if (variableName.equalsIgnoreCase(currentBacnetObject.getObjectName()) && currentBacnetData.getPresentValue()!= null) {
                                //Устанавливаем текущее значение
                                currentBacnetObject.writePropertyInternal(PropertyIdentifier.presentValue, currentBacnetData.getPresentValue());

                            }
                        }
                    }
                }
            }
        }catch (Exception e){
       ;
        }

    }

    //Generate default bool object
    private BACnetObject getDefaultBoolObject(int objectId, String objectName, BinaryPV alarmValue, CharacterString description){
        BinaryValueObject resultObject = null;
        try {
            resultObject = new BinaryValueObject(localDevice,objectId,objectName,BinaryPV.inactive,false);
            resultObject.supportStateText("False","True");
            if(alarmValue !=null) {
                resultObject.supportIntrinsicReporting(1,
                        16,
                        alarmValue,
                        new EventTransitionBits(true, true, true),
                        NotifyType.alarm,
                        1);
            }
            resultObject.writePropertyInternal(PropertyIdentifier.description, description);

        } catch (BACnetServiceException e) {
            e.printStackTrace();
        }

        return resultObject;
    }

    //Generate default analog object
    private BACnetObject getDefaultAnalogObject(int objectId, String objectName, CharacterString description, EngineeringUnits units){
        AnalogValueObject resultObject = null;
        try {
            resultObject =  new AnalogValueObject(localDevice,
                    objectId,
                    objectName,
                    0.0F,
                    units,
                    false);
            /*
            resultObject.supportIntrinsicReporting(1,
                    16,
                    10000.0F,
                    200000F,
                    0F,
                    10000000.0F,
                    -1F,
                    new LimitEnable(true,false),
                    new EventTransitionBits(true, true, true),
                    NotifyType.alarm,
                    1);
                    */
            resultObject.writePropertyInternal(PropertyIdentifier.description, description);

        } catch (BACnetServiceException e) {
            e.printStackTrace();
        }
        return resultObject;
    }

    //Generate default String object
    private BACnetObject getDefaultCharacterstringObject(int objectId, String objectName, CharacterString description){
        CharacterStringObject characterStringObject = null;
        try {
            characterStringObject = new CharacterStringObject(localDevice,
                    objectId,
                    objectName,
                    new CharacterString(""),
                    false);
            characterStringObject.writePropertyInternal(PropertyIdentifier.description,description);
            characterStringObject.writePropertyInternal(PropertyIdentifier.notificationClass,new UnsignedInteger(16));
        } catch (BACnetServiceException e) {
            e.printStackTrace();
        }
        return characterStringObject;
    }

    //Generate default Multistate object
    private BACnetObject getDefaultMultistateObject(int objectId, String objectName, CharacterString description,BACnetArray<CharacterString> stateTexts){
        MultistateValueObject result = null;
        try {
            result = new MultistateValueObject(localDevice,
                    objectId,
                    objectName,
                    stateTexts.size(),
                    stateTexts,
                    1,
                    false);
            result.writePropertyInternal(PropertyIdentifier.description,description);
            result.writePropertyInternal(PropertyIdentifier.notificationClass,new UnsignedInteger(16));
        } catch (BACnetServiceException e) {
            e.printStackTrace();
        }
        return result;

    }
}

import org.snmp4j.TransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.util.ArrayList;

public class SnmpTransfer extends Thread {

    private ArrayList<CanSendSNMP> snmpObjects;
    private TransportMapping snmpTransport;

    SnmpTransfer() throws Exception{
        // Create TransportMapping for SNMP protocol and Listen
        snmpTransport = new DefaultUdpTransportMapping();
        snmpTransport.listen();

        snmpObjects = new ArrayList<>();

        snmpObjects.add(new ApcGalaxy7000UPS("10.25.110.2", "161", "VP3_APC_UPS"));
        snmpObjects.add(new ApcGalaxy7000UPS("10.17.110.2", "161", "VP2_APC_UPS"));
        snmpObjects.add(new ApcGalaxy7000UPS("10.9.110.2", "161", "VP1_APC_UPS"));

        snmpObjects.add(new EatonUPS("10.2.110.2","161","Eaton1"));
        snmpObjects.add(new EatonUPS("10.2.110.3","161","Eaton2"));
        snmpObjects.add(new EatonUPS("10.2.110.5","161","Eaton3"));

        snmpObjects.add(new ApcGalaxy7000UPS("10.2.110.4", "161", "CC_APC_UPS_70019"));
        snmpObjects.add(new ApcGalaxyVMUPS("10.2.110.6", "161", "CC_APC_UPS_20034"));
        snmpObjects.add(new ApcGalaxyVMUPS("10.2.110.7", "161", "CC_APC_UPS2_20034"));
    }

    public void run() {
        while(true)
            try{
                sleep(15000);
                updateDeviceState();
            }catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void updateDeviceState(){
        for (CanSendSNMP currentDevice : snmpObjects) {
            currentDevice.updateState(snmpTransport);
        }
    }

    ArrayList<CanSendSNMP> getSnmpObjects() {
        return snmpObjects;
    }

}

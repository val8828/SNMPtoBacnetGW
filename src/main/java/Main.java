public class Main {

    public static void main(String[] args) throws Exception {

            SnmpTransfer snmp = new SnmpTransfer();
            snmp.start();

            //BacnetPart
            BacnetTransfer bacnetTransfer = new BacnetTransfer(snmp.getSnmpObjects());
            bacnetTransfer.start();
    }

}
import java.util.List;

public class RemoteSNMPDevice {
    String remoteDeviceName;
    String deviceIpAddress;
    List<SNMPVariable> variables;
}

class SNMPVariable{
    String oid;
    String snmpVersion;
    String community;
    String variableName;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getSnmpVersion() {
        return snmpVersion;
    }

    public void setSnmpVersion(String snmpVersion) {
        this.snmpVersion = snmpVersion;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
}

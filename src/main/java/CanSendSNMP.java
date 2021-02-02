import org.snmp4j.TransportMapping;

import java.util.Map;

public interface CanSendSNMP {
    public void updateState(TransportMapping transport);

    public Map<Integer, BacnetData> getBacnetData();

    public String getDeviceName() ;


}

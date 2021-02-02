import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Primitive;

public class BacnetData {
    private String objectName;
    private Primitive presentValue;
    private Encodable units;
    private Encodable outOfService;
    private Encodable eventState;
    private Encodable statusFlags;
    private Encodable stateText;
    private ObjectType objectType;
    private Encodable alarmValue;


    public BacnetData(String objectName, Primitive presentValue, Encodable units, Encodable outOfService, Encodable eventState, Encodable statusFlags, Encodable stateText, ObjectType objectType, Encodable alarmValue) {
        this.objectName = objectName;
        this.presentValue = presentValue;
        this.units = units;
        this.outOfService = outOfService;
        this.eventState = eventState;
        this.statusFlags = statusFlags;
        this.stateText = stateText;
        this.objectType = objectType;
        this.alarmValue = alarmValue;
    }

    public BacnetData(String objectName, Primitive presentValue, Encodable units, Encodable outOfService, Encodable eventState, Encodable statusFlags, Encodable stateText, ObjectType objectType) {
        this.objectName = objectName;
        this.presentValue = presentValue;
        this.units = units;
        this.outOfService = outOfService;
        this.eventState = eventState;
        this.statusFlags = statusFlags;
        this.stateText = stateText;
        this.objectType = objectType;
    }

    public CharacterString getObjectName() {

        return new CharacterString(objectName);
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Primitive getPresentValue() {
        return presentValue;
    }

    public void setPresentValue(Primitive presentValue) {
        this.presentValue = presentValue;
    }

    public Encodable getUnits() {
        return units;
    }

    public void setUnits(Encodable units) {
        this.units = units;
    }

    public Encodable getOutOfService() {
        return outOfService;
    }

    public void setOutOfService(Encodable outOfService) {
        this.outOfService = outOfService;
    }

    public Encodable getEventState() {
        return eventState;
    }

    public void setEventState(Encodable eventState) {
        this.eventState = eventState;
    }

    public Encodable getStatusFlags() {
        return statusFlags;
    }

    public void setStatusFlags(Encodable statusFlags) {
        this.statusFlags = statusFlags;
    }

    public Encodable getStateText() {
        return stateText;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public Encodable getAlarmValue() {
        return alarmValue;
    }

    public void setStateText(Encodable stateText) {
        this.stateText = stateText;
    }
}



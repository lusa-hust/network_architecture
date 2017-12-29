package network_architect.service;

import org.fourthline.cling.binding.annotations.*;
import java.beans.PropertyChangeSupport;

@UpnpService(
        serviceId = @UpnpServiceId("HumiditySensor"),
        serviceType = @UpnpServiceType(value = "HumiditySensor", version = 1)
)

public class HumiditySensor {
    private final PropertyChangeSupport propertyChangeSupport;

    public HumiditySensor() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @UpnpStateVariable(defaultValue = "Unknown")
    private String id;

    @UpnpStateVariable(defaultValue = "0")
    private int value; // Note that the status indicates if the lightSensor is turn off and vice versa

    @UpnpAction
    public void setId(@UpnpInputArgument(name = "NewId") String newId) {
        id = newId;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultId"))
    public String getId() {
        return id;
    }

    @UpnpAction
    public void setValue(@UpnpInputArgument(name = "NewValue") int newValue) {
        System.out.println("new humidity: " + newValue);
        int oldValue = value;
        value = newValue;
        getPropertyChangeSupport().firePropertyChange("value", oldValue, value);
        getPropertyChangeSupport().firePropertyChange("Value", oldValue, value);
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultValue"))
    public int getValue() {
        return value;
    }

}

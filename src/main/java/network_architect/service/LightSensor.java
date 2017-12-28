package network_architect.service;

import org.fourthline.cling.binding.annotations.*;
import java.beans.PropertyChangeSupport;

@UpnpService(
        serviceId = @UpnpServiceId("LightSensor"),
        serviceType = @UpnpServiceType(value = "LightSensor", version = 1)
)

public class LightSensor {
    private final PropertyChangeSupport propertyChangeSupport;

    public LightSensor() {
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
        value = newValue;
        getPropertyChangeSupport().firePropertyChange("Value", null, null);
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultValue"))
    public int getValue() {
        return value;
    }

}

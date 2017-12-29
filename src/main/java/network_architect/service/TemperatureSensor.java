package network_architect.service;

import org.fourthline.cling.binding.annotations.*;
import java.beans.PropertyChangeSupport;

@UpnpService(
        serviceId = @UpnpServiceId("TemperatureSensor"),
        serviceType = @UpnpServiceType(value = "TemperatureSensor", version = 1)
)

public class TemperatureSensor {

    private final PropertyChangeSupport propertyChangeSupport;

    public TemperatureSensor() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @UpnpStateVariable(defaultValue = "Unknown")
    private String id;

    @UpnpStateVariable(defaultValue = "0")
    private boolean status = false; // Note that the status indicates if the lightSensor is turn off and vice versa

    @UpnpStateVariable(defaultValue = "0")
    private int value; // Note that the value indicates how strong the light intensity is

    @UpnpAction
    public void setId(@UpnpInputArgument(name = "NewId") String newId) {
        id = newId;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultId"))
    public String getId() {
        return id;
    }

    @UpnpAction
    public void setStatus(@UpnpInputArgument(name = "NewStatus") boolean newStatus) {
        status = newStatus;
        getPropertyChangeSupport().firePropertyChange("Status", null, null);
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultStatus"))
    public boolean getStatus() {
        return status;
    }

    @UpnpAction
    public void setValue(@UpnpInputArgument(name = "NewValue") int newValue) {
        System.out.println("new temperature: " + newValue);
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

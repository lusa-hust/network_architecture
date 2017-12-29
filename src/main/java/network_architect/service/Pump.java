package network_architect.service;

import org.fourthline.cling.binding.annotations.*;
import java.beans.PropertyChangeSupport;

@UpnpService(
        serviceId = @UpnpServiceId("Pump"),
        serviceType = @UpnpServiceType(value = "Pump", version = 1)
)

public class Pump {
    private final PropertyChangeSupport propertyChangeSupport;

    public Pump() {
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
        boolean oldStatus = status;
        status = newStatus;
        getPropertyChangeSupport().firePropertyChange("Status", oldStatus, status);
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultStatus"))
    public boolean getStatus() {
        return status;
    }

    @UpnpAction
    public void setValue(@UpnpInputArgument(name = "NewValue") int newValue) {
        int oldValue = value;
        value = newValue;
        getPropertyChangeSupport().firePropertyChange("Value", oldValue, value);
        System.out.println(" humidity: " + value);
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultValue"))
    public int getValue() {
        return value;
    }
}

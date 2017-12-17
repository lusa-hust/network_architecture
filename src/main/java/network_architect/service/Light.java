package network_architect.service;

import org.fourthline.cling.binding.annotations.*;
import java.beans.PropertyChangeSupport;

@UpnpService(
        serviceId = @UpnpServiceId("Light"),
        serviceType = @UpnpServiceType(value = "Light", version = 1)
)

public class Light {
    private final PropertyChangeSupport propertyChangeSupport;

    public Light() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @UpnpStateVariable(defaultValue = "Unknown")
    private String id;

    @UpnpStateVariable(defaultValue = "0")
    private boolean status = false; // Note that the status indicates if the light is turn off and vice versa

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
    public void setValue(@UpnpInputArgument(name = "NewValue") boolean newStatus) {
        status = newStatus;
        getPropertyChangeSupport().firePropertyChange("Value", null, null);
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultValue"))
    public boolean getValue() {
        return status;
    }


}

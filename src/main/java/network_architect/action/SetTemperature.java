


package network_architect.action;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;

public class SetTemperature extends ActionInvocation {
    public SetTemperature(Service service, int temp) {
        super(service.getAction("SetValue"));

        try {
            setInput("NewValue", temp);
        } catch (InvalidValueException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}

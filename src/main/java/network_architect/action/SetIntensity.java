package network_architect.action;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;

public class SetIntensity extends ActionInvocation {
    public SetIntensity(Service service, int intensity) {
        super(service.getAction("SetValue"));

        try {
            setInput("NewValue", intensity);
        } catch (InvalidValueException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}

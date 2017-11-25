package network_architect.control_point;


import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.*;
import org.fourthline.cling.model.UnsupportedDataException;
import org.fourthline.cling.model.action.*;
import org.fourthline.cling.model.gena.*;
import org.fourthline.cling.model.message.*;
import org.fourthline.cling.model.message.header.*;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.model.types.*;
import org.fourthline.cling.registry.*;

import java.util.Map;

public class Client implements Runnable {

    private Device binaryLight;
    public static void main(String[] args) throws Exception {
        // Start a user thread that runs the UPnP stack
        Thread clientThread = new Thread(new Client());
        clientThread.setDaemon(false);
        clientThread.start();

    }

    public void run() {
        UpnpService upnpService = new UpnpServiceImpl();
        Registry registry = upnpService.getRegistry();
        ControlPoint controlPoint = upnpService.getControlPoint();
        try {
            // Add a listener for device registration events
            registry.addListener(
                    createRegistryListener(upnpService)
            );

            // Broadcast a search message for all devices
            controlPoint.search(new STAllHeader());


        } catch (Exception ex) {
            System.err.println("Exception occured: " + ex);
            System.exit(1);
        }
    }

    RegistryListener createRegistryListener(final UpnpService upnpService) {
        return new DefaultRegistryListener() {

            ServiceId serviceId = new UDAServiceId("SwitchPower");

            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {

                Service switchPower;
                if ((switchPower = device.findService(serviceId)) != null) {

                    System.out.println("Service discovered: " + switchPower);
                    SubscriptionCallback callback = createSubscriptionCallback(switchPower);
                    upnpService.getControlPoint().execute(callback);
                    binaryLight = device;
                    executeAction(upnpService, switchPower);

                }

            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                Service switchPower;
                if ((switchPower = device.findService(serviceId)) != null) {
                    System.out.println("Service disappeared: " + switchPower);
                }
            }

        };
    }

    void executeAction(UpnpService upnpService, Service switchPowerService) {

        ActionInvocation setTargetInvocation =
                new SetTargetActionInvocation(switchPowerService);

        // Executes asynchronous in the background
        upnpService.getControlPoint().execute(
                new ActionCallback(setTargetInvocation) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        System.out.println("Successfully called action!");
                    }

                    @Override
                    public void failure(ActionInvocation invocation,
                                        UpnpResponse operation,
                                        String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                }
        );

    }

    class SetTargetActionInvocation extends ActionInvocation {

        SetTargetActionInvocation(Service service) {
            super(service.getAction("SetTarget"));
            try {

                // Throws InvalidValueException if the value is of wrong type
                setInput("NewTargetValue", true);

            } catch (InvalidValueException ex) {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }
    }

    public SubscriptionCallback createSubscriptionCallback(Service service) {
        return new SubscriptionCallback(service, 5) {

            @Override
            public void established(GENASubscription sub) {
                System.out.println("Established: " + sub.getSubscriptionId());
            }

            @Override
            protected void failed(GENASubscription subscription,
                                  UpnpResponse responseStatus,
                                  Exception exception,
                                  String defaultMsg) {
                System.err.println(defaultMsg);
            }

            @Override
            public void ended(GENASubscription sub,
                              CancelReason reason,
                              UpnpResponse response) {
                System.out.println("Ended");
            }

            @Override
            public void eventReceived(GENASubscription sub) {

                System.out.println("Event: " + sub.getCurrentSequence().getValue());

                Map<String, StateVariableValue> values = sub.getCurrentValues();
                StateVariableValue status = values.get("Status");

                System.out.println("Status is: " + status.toString());

            }

            @Override
            public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
                System.out.println("Missed events: " + numberOfMissedEvents);
            }

            @Override
            protected void invalidMessage(RemoteGENASubscription sub,
                                          UnsupportedDataException ex) {
                // Log/send an error report?
                System.out.println("invalid message");
            }
        };
    }
}

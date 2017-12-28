package network_architect.devices;

import network_architect.action.SetDeviceIdAction;
import javafx.application.Application;
import javafx.stage.Stage;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.UnsupportedDataException;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.gena.RemoteGENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceId;

public abstract class DeviceApp extends Application {
    //protected Device device;
    protected UpnpService upnpService;

    // JavaFX
    protected Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        upnpService = new UpnpServiceImpl();
        this.primaryStage = primaryStage;
    }

    protected void executeAction(UpnpService upnpService, ActionInvocation action) {
        // Executes asynchronous in the background
        upnpService.getControlPoint().execute(
                new ActionCallback(action) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        System.out.println("Successfully called action " + invocation.getClass().getSimpleName());
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

    /**
     * Create upnp devices
     */
    private Device createDevices(String prefix, String type, String friendlyName, String description, Class deviceClass) {
        // Create device model
        String id = prefix;
        Device device = new Device(id, type,  id,1, friendlyName,
                                    "Tesla", description, "B502", deviceClass);
        device.initializeDevice();

        return device;
    }

    protected Device initializeDevices(
            String prefix, String type,
            String friendlyName, String description,
            Class deviceClass) {
        Device device = createDevices(prefix, type, friendlyName, description, deviceClass);

        try {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    upnpService.shutdown();
                }
            });

            // Add the bound local device to the registry
            addDevices(device);
        } catch (Exception ex) {
            System.err.println("Exception occured: " + ex);
            ex.printStackTrace(System.err);
            System.exit(1);
        }
        return device;
    }

    /**
     * Add devices into upnp services
     *
     * @param device
     */
    private void addDevices(Device device) {
        upnpService.getRegistry().addDevice(device.getDevice());
    }

    protected Service getService(LocalDevice device, String serviceId) {
        return device.findService(new UDAServiceId(serviceId));
    }

    // Receiving events from services
    protected void initializePropertyChangeCallback(UpnpService upnpService, Service service) {
        SubscriptionCallback callback = new SubscriptionCallback(service, 600) {

            @Override
            public void established(GENASubscription sub) {
                System.out.println("Established: " + sub.getSubscriptionId());
            }

            @Override
            protected void failed(GENASubscription subscription, UpnpResponse responseStatus, Exception exception, String defaultMsg) {
                System.err.println(defaultMsg);
            }

            @Override
            public void ended(GENASubscription sub, CancelReason reason, UpnpResponse response) {
            }

            @Override
            public void eventReceived(GENASubscription sub) {
                onPropertyChangeCallbackReceived(sub);
            }

            @Override
            public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
                System.out.println("Missed events: " + numberOfMissedEvents);
            }

            @Override
            protected void invalidMessage(RemoteGENASubscription sub, UnsupportedDataException ex) {
                // Log/send an error report?
            }
        };

        upnpService.getControlPoint().execute(callback);
    }

    public abstract void onPropertyChangeCallbackReceived(GENASubscription subscription);
}

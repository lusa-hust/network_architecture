package network_architect.control_point;

import network_architect.view.ControlPointView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
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
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.UDAServiceId;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SmartGardenControlPoint extends Application {
    private HashMap<String, RemoteDevice> controlledDevices;
    private final UpnpService upnpService = new UpnpServiceImpl();
    private Stage primaryStage;
    private ControlPointView controlPointView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Smart Parking Control Panel");

        try {
            controlledDevices = new HashMap<String, RemoteDevice>();

            // Add a listener for device registration events
            upnpService.getRegistry().addListener(
                    createRegistryListener(upnpService)
            );

            // Broadcast a search message for all devices
            upnpService.getControlPoint().search(
                    new STAllHeader()
            );



            initializeRootLayout();
        } catch (Exception ex) {
            System.err.println("Exception occured: " + ex);
            System.exit(1);
        }
    }

    private void initializeRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/SmartGardenControlPointView.fxml"));
            AnchorPane rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Set app reference for controller
            controlPointView = loader.getController();
            controlPointView.setApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializePropertyChangeCallback(UpnpService upnpService, Service service) {
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
                Map<String, StateVariableValue> values = sub.getCurrentValues();
                StateVariableValue idVar = values.get("Id");

                // Only care about data change
                if (idVar != null) {
                    String id = (String) idVar.getValue();

                    if (id.contains("LightSensor")) {

                        onLightSensorDataChange(id,true);
                    } else if (id.contains("HumiditySensor")) {
                        onHumiditySensorDataChange(id,true);
                    } else if (id.contains("TemperatureSensor")) {

                        onTemperatureSensorDataChange(id,true);

                    } else if (id.contains("Light")) {
                        StateVariableValue getStatus = values.get("Status");
                        Boolean status = (Boolean) getStatus.getValue();
                        onLightDataChange(id,status);
                    } else if (id.contains("AirConditioning")) {
                        onAirConditioningDataChange(id,true);
                    } else if (id.contains("Pump")) {
                        onPumpDataChange(id,true);
                    }
                }
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

    private RegistryListener createRegistryListener(final UpnpService upnpService) {
        return new DefaultRegistryListener() {
            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
                String deviceId = device.getDetails().getFriendlyName();

                if (deviceId.contains("Light")) {
                    // Add device to hashmap
                    controlledDevices.put(deviceId, device);

                    // Set data change callback
                    Service slotSensorService = device.findService(new UDAServiceId("Light"));
                    if (slotSensorService != null) {
                        initializePropertyChangeCallback(upnpService, slotSensorService);
                    }
                }

                if (deviceId.contains("LightSensor")) {
                    // Add device to hashmap
                    controlledDevices.put(deviceId, device);

                    // Set data change callback
                    Service signMonitorService = device.findService(new UDAServiceId("LightSensor"));
                    if (signMonitorService != null) {
                        initializePropertyChangeCallback(upnpService, signMonitorService);
                    }
                }

                if (deviceId.contains("AirConditioning")) {
                    // Add device to hashmap
                    controlledDevices.put(deviceId, device);

                    // Set data change callback
                    Service signMonitorService = device.findService(new UDAServiceId("AirConditioning"));
                    if (signMonitorService != null) {
                        initializePropertyChangeCallback(upnpService, signMonitorService);
                    }
                }

                if (deviceId.contains("HumiditySensor")) {
                    // Add device to hashmap
                    controlledDevices.put(deviceId, device);

                    // Set data change callback
                    Service signMonitorService = device.findService(new UDAServiceId("HumiditySensor"));
                    if (signMonitorService != null) {
                        initializePropertyChangeCallback(upnpService, signMonitorService);
                    }
                }

                if (deviceId.contains("Pump")) {
                    // Add device to hashmap
                    controlledDevices.put(deviceId, device);

                    // Set data change callback
                    Service signMonitorService = device.findService(new UDAServiceId("Pump"));
                    if (signMonitorService != null) {
                        initializePropertyChangeCallback(upnpService, signMonitorService);
                    }
                }

                if (deviceId.contains("TemperatureSensor")) {
                    // Add device to hashmap
                    controlledDevices.put(deviceId, device);

                    // Set data change callback
                    Service signMonitorService = device.findService(new UDAServiceId("TemperatureSensor"));
                    if (signMonitorService != null) {
                        initializePropertyChangeCallback(upnpService, signMonitorService);
                    }
                }

                System.out.println("Device discovered: " + deviceId);
            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                String deviceId = device.getDetails().getFriendlyName();

                // Remove device from hashmap
                controlledDevices.remove(deviceId);
                System.out.println("Device disappeared: " + deviceId);
            }
        };
    }

    private void executeAction(UpnpService upnpService, ActionInvocation action) {
        // Executes asynchronous in the background
        upnpService.getControlPoint().execute(
                new ActionCallback(action) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        System.out.println("Successfully called action " + invocation.getClass().getSimpleName());
                    }

                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                }
        );
    }

    //TODO: Handle data change for each devices

    private void onLightSensorDataChange(String id, boolean status) {

    }

    private void onHumiditySensorDataChange(String id, boolean status) {

    }

    private void onTemperatureSensorDataChange(String id, boolean status) {

    }

    private void onLightDataChange(String id, boolean status) {

    }

    private void onPumpDataChange(String id, boolean status) {

    }

    private void onAirConditioningDataChange(String id, boolean status) {

    }
}

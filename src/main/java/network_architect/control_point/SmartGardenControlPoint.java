package network_architect.control_point;

import network_architect.action.SetIntensity;
import network_architect.action.SetStatus;
import network_architect.action.SetTemperature;
import network_architect.devices.Device;
import network_architect.service.*;
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
    private Device currentDevice;
    private Service LightService, LightSensorService, pumpService, acService;

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
        SubscriptionCallback callback = new SubscriptionCallback(service, 5) {

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

                System.out.println("Event: " + sub.getCurrentSequence().getValue());
                StateVariableValue test = values.get("Value");
                System.out.println("Value is: " + test.toString());

                // Only care about data change

                if (idVar != null) {
                    System.out.println("ID : " + idVar.getValue().toString());
                    String id = idVar.getValue().toString();


                    if (id.equals("LightSensor")) {
                        //boolean status = Boolean.parseBoolean(values.get("Status").getValue().toString());
                        int intensity = Integer.parseInt(values.get("Value").getValue().toString());
                        System.out.println(">>>>> " + " " + intensity);
                        onLightSensorDataChange(id, intensity);

                    } else if (id.equals("HumiditySensor")) {
                        int humidity = Integer.parseInt(values.get("Value").getValue().toString());
                        System.out.println("CP: humidity >>>>> " + " " + humidity);
                        onHumiditySensorDataChange(id, humidity);

                    } else if (id.equals("TemperatureSensor")) {

                        int temp = Integer.parseInt(values.get("Value").getValue().toString());
                        System.out.println("CP: Temp >>>>> " + " " + temp);
                        onHumiditySensorDataChange(id, temp);

                        onTemperatureSensorDataChange(id, temp);

                    } else if (id.equals("Light")) {
                        boolean status = Boolean.parseBoolean(values.get("Status").getValue().toString());
                        int intensity = Integer.parseInt(values.get("Value").getValue().toString());
                        System.out.println(">>>>> " + status + " " + intensity);
                        onLightDataChange(id, status, intensity);

                    } else if (id.equals("AirConditioning")) {

                        onAirConditioningDataChange(id, true);

                    } else if (id.equals("Pump")) {

                        onPumpDataChange(id, true);
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
                System.out.println(deviceId);

                if (deviceId.contains("Light")) {
                    // Add device to hashmap
                    controlledDevices.put(deviceId, device);

                    // Set data change callback
                    LightService = device.findService(new UDAServiceId("Light"));
                    if (LightService != null) {
                        initializePropertyChangeCallback(upnpService, LightService);
                        System.out.println("1. subscribe Light completed!");
                        System.out.println(">> Light service: " + LightService.toString());
                    }

                }

                if (deviceId.contains("LightSensor")) {
                    // Add device to hashmap
                    controlledDevices.put(deviceId, device);

                    // Set data change callback
                    LightSensorService = device.findService(new UDAServiceId("LightSensor"));
                    if (LightSensorService != null) {

                        initializePropertyChangeCallback(upnpService, LightSensorService);
                        System.out.println("2. subscribe Light sensor service completed!");
                    }

                }

                if (deviceId.contains("AirConditioning")) {
                    // Add device to hashmap
                    controlledDevices.put(deviceId, device);

                    // Set data change callback
                    acService = device.findService(new UDAServiceId("AirConditioning"));
                    if (acService != null) {
                        initializePropertyChangeCallback(upnpService, acService);
                    }
                }

                if (deviceId.contains("HumiditySensor")) {
                    // Add device to hashmap
                    controlledDevices.put(deviceId, device);

                    // Set data change callback
                    Service HumiditySensorService = device.findService(new UDAServiceId("HumiditySensor"));
                    if (HumiditySensorService != null) {
                        initializePropertyChangeCallback(upnpService, HumiditySensorService);
                    }
                }

                if (deviceId.contains("Pump")) {
                    // Add device to hashmap
                    controlledDevices.put(deviceId, device);

                    // Set data change callback
                    pumpService = device.findService(new UDAServiceId("Pump"));
                    if (pumpService != null) {
                        initializePropertyChangeCallback(upnpService, pumpService);
                    }
                }

                if (deviceId.contains("TemperatureSensor")) {
                    // Add device to hashmap
                    controlledDevices.put(deviceId, device);

                    // Set data change callback
                    Service TemperatureSensorService = device.findService(new UDAServiceId("TemperatureSensor"));
                    if (TemperatureSensorService != null) {
                        initializePropertyChangeCallback(upnpService, TemperatureSensorService);
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

    private void onLightSensorDataChange(String id, int intensity) {
        int threshold = controlPointView.getIntenThreshold();
        // default th value = 50
        if (LightService == null) return;
        if (intensity < threshold) {
            //Service service = LightService;
            System.out.println("This service: " + LightService.toString());
            executeAction(upnpService, new SetStatus(LightService, true));
            executeAction(upnpService, new SetIntensity(LightService, 50));
        }

        if (intensity >= threshold) {
            //Service service = LightService;
            System.out.println("This service: " + LightService.toString());
            executeAction(upnpService, new SetStatus(LightService, false));
            executeAction(upnpService, new SetIntensity(LightService, 0));
        }
    }

    private void onHumiditySensorDataChange(String id, int humidity) {
        int threshold = controlPointView.getHumThreshold();
        // default th value = 30
        if (pumpService == null) return;
        if (humidity < threshold) {
            executeAction(upnpService, new SetStatus(pumpService, true));
        } else {
            executeAction(upnpService, new SetStatus(pumpService, false));
        }
    }

    private void onTemperatureSensorDataChange(String id, int temp) {
        int threshold = controlPointView.getTempThreshold();
        // default = 15
        if (acService == null) return;
        if (temp < threshold) {
            //Service service = LightService;
            System.out.println("This service: " + acService.toString());
            executeAction(upnpService, new SetStatus(acService, true));
            executeAction(upnpService, new SetTemperature(acService, 20));
        }

        else {
            //Service service = LightService;
            System.out.println("This service: " + acService.toString());
            executeAction(upnpService, new SetStatus(acService, false));
        }
    }

    private void onLightDataChange(String id, boolean status, int intensity) {

    }

    private void onPumpDataChange(String id, boolean status) {

    }

    private void onAirConditioningDataChange(String id, boolean status) {

    }
}

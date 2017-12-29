package network_architect.app;

import network_architect.action.SetStatus;
import network_architect.action.SetTemperature;
import network_architect.devices.DeviceApp;
import network_architect.devices.Device;
import network_architect.service.AirConditioning;
import network_architect.service.Light;
import network_architect.view.AirConditioningView;
import network_architect.view.LightView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.state.StateVariableValue;

import java.io.IOException;
import java.util.Map;

public class AirConditionApp extends DeviceApp {
    private Device currentDevice;
    private AirConditioningView airConditioningView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        super.start(primaryStage);
        primaryStage.setTitle("AC Control Panel");
        currentDevice = initializeDevices("AirConditioning", "AirConditioning", "AirConditioning", "Using for AirConditioner status", AirConditioning.class);
        initRootLayout();
        setServiceId(currentDevice, "AirConditioning");
        Service service = getService(currentDevice.getDevice(), "AirConditioning");
        initializePropertyChangeCallback(upnpService, service);
    }

    @Override
    public void onPropertyChangeCallbackReceived(GENASubscription subscription) {
        Map<String, StateVariableValue> values = subscription.getCurrentValues();
        StateVariableValue idVar = values.get("Id");
        System.out.println("check get from subscribes: " + values.toString());


        StateVariableValue status = values.get("Status");
        if (status != null)
            airConditioningView.setStatusAC((Boolean) status.getValue());

        StateVariableValue intensity = values.get("Value");
        if (intensity != null) airConditioningView.setTemp(intensity.getValue().toString());


    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/AirConditioningView.fxml"));
            AnchorPane rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, 320, 240);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Set app reference for controller
            airConditioningView = loader.getController();
            airConditioningView.setApp(this);
            this.getLightStatus();
            //this.getLightIntensity();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getLightStatus() {
        Service service = getService(currentDevice.getDevice(), "AirConditioning");

        if (service != null) {
            Action getStatusAction = service.getAction("GetStatus");
            ActionInvocation actionInvocation = new ActionInvocation(getStatusAction);
            ActionCallback getStatusCallback = new ActionCallback(actionInvocation) {
                @Override
                public void success(ActionInvocation invocation) {
                    ActionArgumentValue status = invocation.getOutput("ResultStatus");
                    airConditioningView.setStatusAC((Boolean) status.getValue());
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                    System.err.println(defaultMsg);
                }
            };
            upnpService.getControlPoint().execute(getStatusCallback);
        }

    }

    public void getLightIntensity() {
        Service service = getService(currentDevice.getDevice(), "AirConditioning");

        if (service != null) {
            Action getStatusAction = service.getAction("GetValue");
            ActionInvocation actionInvocation = new ActionInvocation(getStatusAction);
            ActionCallback getStatusCallback = new ActionCallback(actionInvocation) {
                @Override
                public void success(ActionInvocation invocation) {
                    ActionArgumentValue status = invocation.getOutput("ResultValue");
                    airConditioningView.setTemp(status.getValue().toString());
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                    System.err.println(defaultMsg);
                }
            };
            upnpService.getControlPoint().execute(getStatusCallback);
        }
    }

    public void setAcStatus(boolean status) {

        Service service = getService(currentDevice.getDevice(), "AirConditioning");

        if (service != null) {
            executeAction(upnpService, new SetStatus(service, status));
        }

    }

    public void setTemp(int Temp) {
        Service service = getService(currentDevice.getDevice(), "AirConditioning");

        if (service != null) {
            executeAction(upnpService, new SetTemperature(service, Temp));
        }
    }
}

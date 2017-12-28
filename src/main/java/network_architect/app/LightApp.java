package network_architect.app;

import network_architect.devices.DeviceApp;
import network_architect.devices.Device;
import network_architect.service.Light;
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

public class LightApp extends DeviceApp {
    private final int SIGN_NUMBER = 1;
    private Device currentDevice;
    private LightView lightViewController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        super.start(primaryStage);
        primaryStage.setTitle("Light Control Panel");
        currentDevice = initializeDevices("Light", "Light", "Light", "Using for displaying light status", Light.class);
        initRootLayout();

    }

    @Override
    public void onPropertyChangeCallbackReceived(GENASubscription subscription) {
        Map<String, StateVariableValue> values = subscription.getCurrentValues();
        StateVariableValue idVar = values.get("Id");

        if (idVar != null) {
            String id = (String) idVar.getValue();
            if (id.equals(currentDevice.getId())) {

            }
        }
    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/LightView.fxml"));
            AnchorPane rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, 320, 240);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Set app reference for controller
            lightViewController = loader.getController();
            lightViewController.setApp(this);
            this.getLightStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void getLightStatus() {
        Service service = getService(currentDevice.getDevice(), "Light");

        if (service != null) {
            Action getStatusAction = service.getAction("GetStatus");
            ActionInvocation actionInvocation = new ActionInvocation(getStatusAction);
            ActionCallback getStatusCallback = new ActionCallback(actionInvocation) {
                @Override
                public void success(ActionInvocation invocation) {
                    ActionArgumentValue status = invocation.getOutput("ResultStatus");
                    lightViewController.setStatusLight((Boolean) status.getValue());
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
        Service service = getService(currentDevice.getDevice(), "Light");

        if (service != null) {
            Action getStatusAction = service.getAction("GetValue");
            ActionInvocation actionInvocation = new ActionInvocation(getStatusAction);
            ActionCallback getStatusCallback = new ActionCallback(actionInvocation) {
                @Override
                public void success(ActionInvocation invocation) {
                    ActionArgumentValue status = invocation.getOutput("ResultValue");
                    lightViewController.setStatusLight((Boolean) status.getValue());
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                    System.err.println(defaultMsg);
                }
            };
            upnpService.getControlPoint().execute(getStatusCallback);
        }
    }

}


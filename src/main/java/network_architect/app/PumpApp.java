package network_architect.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import network_architect.devices.Device;
import network_architect.devices.DeviceApp;
import network_architect.service.Pump;
import network_architect.view.PumpView;
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

/**
 * Created by li on 29/12/2017.
 */
public class PumpApp extends DeviceApp {
    private Device currentDevice;
    private PumpView pumpViewController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        super.start(primaryStage);
        primaryStage.setTitle("Pump Control Panel");
        currentDevice = initializeDevices("Pump", "Pump", "Pump", "Using for displaying pump status", Pump.class);
        initRootLayout();
        setServiceId(currentDevice,"Pump");
        Service service = getService(currentDevice.getDevice(), "Pump");
        initializePropertyChangeCallback(upnpService, service);
    }

    @Override
    public void onPropertyChangeCallbackReceived(GENASubscription subscription) {
        Map<String, StateVariableValue> values = subscription.getCurrentValues();
        StateVariableValue idVar = values.get("Id");
        System.out.println("check get from subscribes: " + values.toString());


        StateVariableValue status = values.get("Status");
        if(status != null)
            pumpViewController.setStatusPump((Boolean) status.getValue());
    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/PumpView.fxml"));
            AnchorPane rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, 320, 240);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Set app reference for controller
            pumpViewController = loader.getController();
            pumpViewController.setApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPumpStatus() {
        Service service = getService(currentDevice.getDevice(), "Pump");

        if (service != null) {
            Action getStatusAction = service.getAction("GetStatus");
            ActionInvocation actionInvocation = new ActionInvocation(getStatusAction);
            ActionCallback getStatusCallback = new ActionCallback(actionInvocation) {
                @Override
                public void success(ActionInvocation invocation) {
                    ActionArgumentValue status = invocation.getOutput("ResultStatus");
                    pumpViewController.setStatusPump((Boolean) status.getValue());
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

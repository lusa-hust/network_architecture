package network_architect.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import network_architect.action.SetIntensity;
import network_architect.devices.Device;
import network_architect.devices.DeviceApp;
import network_architect.service.Light;
import network_architect.service.LightSensor;
import network_architect.view.LightSensorView;
import network_architect.view.LightView;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.state.StateVariableValue;

import java.io.IOException;
import java.util.Map;

import static javafx.application.Application.launch;

public class LightSensorApp extends DeviceApp {
    private Device currentDevice;
    private LightSensorView lightSensorViewController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        super.start(primaryStage);
        primaryStage.setTitle("Light Sensor Panel");
        currentDevice = initializeDevices(
                "LightSensor", "LightSensor",
                "LightSensor", "Using for displaying light sensor status",
                LightSensor.class
        );
        initRootLayout();
        setServiceId(currentDevice,"LightSensor");
    }

    @Override
    public void onPropertyChangeCallbackReceived(GENASubscription subscription) {

    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/LightSensorView.fxml"));
            AnchorPane rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, 320, 240);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Set app reference for controller
            System.out.println("ok");
            lightSensorViewController = loader.getController();
            lightSensorViewController.setApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setIntensity(int intensity) {
        Service service = getService(currentDevice.getDevice(), "LightSensor");

        if (service != null) {
            executeAction(upnpService, new SetIntensity(service, intensity));
        }
    }

}

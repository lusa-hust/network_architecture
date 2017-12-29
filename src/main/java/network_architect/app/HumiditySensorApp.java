package network_architect.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import network_architect.action.SetIntensity;
import network_architect.devices.Device;
import network_architect.devices.DeviceApp;
import network_architect.service.HumiditySensor;
import network_architect.view.HumiditySensorView;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.meta.Service;

import java.io.IOException;

/**
 * Created by li on 29/12/2017.
 */
public class HumiditySensorApp extends DeviceApp {
    private Device currentDevice;
    private HumiditySensorView humiditySensorViewController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        super.start(primaryStage);
        primaryStage.setTitle("Humidity Sensor Panel");
        currentDevice = initializeDevices(
                "HumiditySensor", "HumiditySensor",
                "HumiditySensor", "Using for displaying humidity sensor status",
                HumiditySensor.class
        );
        initRootLayout();
        setServiceId(currentDevice,"HumiditySensor");
    }

    @Override
    public void onPropertyChangeCallbackReceived(GENASubscription subscription) {

    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/HumiditySensorView.fxml"));
            AnchorPane rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, 320, 240);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Set app reference for controller
            humiditySensorViewController = loader.getController();
            humiditySensorViewController.setApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHumidity(int humidity) {
        Service service = getService(currentDevice.getDevice(), "HumiditySensor");

        if (service != null) {
            executeAction(upnpService, new SetIntensity(service, humidity));
        }
    }
}

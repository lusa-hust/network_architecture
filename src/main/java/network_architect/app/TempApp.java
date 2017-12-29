package network_architect.app;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import network_architect.action.SetTemperature;
import network_architect.devices.Device;
import network_architect.devices.DeviceApp;
import network_architect.service.TemperatureSensor;
import network_architect.view.TemperatureSensorView;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.state.StateVariableValue;

import java.io.IOException;
import java.util.Map;

import static javafx.application.Application.launch;

public class TempApp extends DeviceApp{
    private Device currentDevice;
    private TemperatureSensorView temperatureSensorView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        super.start(primaryStage);
        primaryStage.setTitle("Temperature Sensor Panel");
        currentDevice = initializeDevices(
                "TemperatureSensor", "TemperatureSensor",
                "TemperatureSensor", "Using for displaying Temperature Sensor status",
                TemperatureSensor.class
        );
        initRootLayout();
        setServiceId(currentDevice,"TemperatureSensor");
    }

    @Override
    public void onPropertyChangeCallbackReceived(GENASubscription subscription) {

    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/TemperatureSensorView.fxml"));
            AnchorPane rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, 320, 240);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Set app reference for controller
            System.out.println("ok");
            temperatureSensorView = loader.getController();
            temperatureSensorView.setApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTemp(int temperature) {
        Service service = getService(currentDevice.getDevice(), "TemperatureSensor");

        if (service != null) {
            executeAction(upnpService, new SetTemperature(service, temperature));
        }
    }

}

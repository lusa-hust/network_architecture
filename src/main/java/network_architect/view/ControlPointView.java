package network_architect.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import network_architect.control_point.SmartGardenControlPoint;
import javafx.fxml.FXML;

public class ControlPointView {
    private SmartGardenControlPoint app;

    public void setApp(SmartGardenControlPoint app) {
        this.app = app;
    }

    @FXML
    private void openLight() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LightView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Light");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openPump() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PumpView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Pump");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openTempSensor() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TemperatureSensorView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Temperature Sensor");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openLightSensor() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LightSensorView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Light Sensor");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openHumiditySensor() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HumiditySensorView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Humidity Sensor");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAirConditioner() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AirConditioningView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Air Conditioner");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

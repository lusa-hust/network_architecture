package network_architect.view;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import network_architect.app.LightApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import network_architect.devices.Device;


public class LightView {
    @FXML
    private Circle statusLight;

    @FXML
    private Button btnSwitch;

    @FXML
    private TextField lbIntensity;

    private LightApp app;


    @FXML
    public void btnClicked(){
        setStatusLight(true);
    }

    public void setLbIntensity(String intensity){
        if(intensity != null) {
            System.out.println("Intensity " + intensity);
            lbIntensity.setText(intensity);
        }
    }

    public void setStatusLight(Boolean status){
        if(status)
        {

            statusLight.setFill(Color.GREEN);
        }
        else
        {
            statusLight.setFill(Color.GRAY);
        }
    }

    public void setApp(LightApp app) {
        this.app = app;
    }
}

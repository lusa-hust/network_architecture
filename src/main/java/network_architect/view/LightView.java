package network_architect.view;

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

    private LightApp app;


    @FXML
    public void btnClicked(){
        setStatusLight(true);
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

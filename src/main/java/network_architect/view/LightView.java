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

    private static final String STATE_AVAILABLE = "available";
    private static final String STATE_UNAVAILABLE = "unavailable";

    private ObservableList<String> signDeviceIds = FXCollections.observableArrayList();
    private LightApp app;

    @FXML
    private void initialize() {
        // show current status to UI


    }

    public void populateSlotSensorList(Device[] devices)
    {
        for (Device device : devices)
        {
            signDeviceIds.add(device.getId());
        }
    }

    @FXML
    public void btnClicked(){
        setStatusLight(true);
    }


    public void setStatusLight(Boolean status){
        if(status)
        {

            statusLight.setFill(Color.RED);
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

package network_architect.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import network_architect.app.LightApp;
import network_architect.app.LightSensorApp;

public class LightSensorView {
    @FXML
    private Circle status;

    @FXML
    private TextField lblIntensity;

    private LightSensorApp app;

    @FXML
    public void onUpdate(){
        if (lblIntensity.getText() != null ) {
            //app.setSignDirection(directionText.getText());

            System.out.println(lblIntensity.getText());
            if(this.app != null)
                this.app.setIntensity(Integer.parseInt(lblIntensity.getText()));
            else
                System.out.println("null");
        }
    }

    public void setApp(LightSensorApp app) {
        this.app = app;
        System.out.println("da set");
    }

}

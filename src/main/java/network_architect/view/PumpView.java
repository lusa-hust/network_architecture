package network_architect.view;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import network_architect.app.LightApp;
import javafx.fxml.FXML;
import network_architect.app.PumpApp;

public class PumpView {
    @FXML
    private Circle statusPump;
    private PumpApp app;

    public void setStatusPump(Boolean status){
        if(status)
        {

            statusPump.setFill(Color.GREEN);
        }
        else
        {
            statusPump.setFill(Color.GRAY);
        }
    }

    public void setApp(PumpApp app) {
        this.app = app;
    }

}

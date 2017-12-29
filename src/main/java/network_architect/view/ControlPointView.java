package network_architect.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import network_architect.control_point.SmartGardenControlPoint;
import javafx.fxml.FXML;

public class ControlPointView {
    public TextField tfTemp;
    public TextField tfInten;
    public TextField tfHum;
    private SmartGardenControlPoint app;

    public void setApp(SmartGardenControlPoint app) {
        this.app = app;
    }

    public int getTempThreshold() {
        if (tfTemp.getText().trim().length() > 0)
            return Integer.parseInt(tfTemp.getText());
        else
            return 20;
    }

    public int getIntenThreshold() {
        if (tfInten.getText().trim().length() > 0)
            return Integer.parseInt(tfInten.getText());
        else
            return 50;


    }

    public int getHumThreshold() {
        if (tfHum.getText().trim().length() > 0)
            return Integer.parseInt(tfHum.getText());
        else
            return 20;
    }
}

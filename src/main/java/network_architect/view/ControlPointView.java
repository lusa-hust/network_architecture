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


}

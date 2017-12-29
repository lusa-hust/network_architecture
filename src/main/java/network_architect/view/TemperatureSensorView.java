package network_architect.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import network_architect.app.TempApp;

public class TemperatureSensorView {
    public Circle status;
    public TextField tfTemp;
    private TempApp app;

    public void setApp(TempApp app) {
        this.app = app;
    }

    public void onUpdate(ActionEvent actionEvent) {
        if (tfTemp.getText() != null) {
            //app.setSignDirection(directionText.getText());

            System.out.println(tfTemp.getText());
            if (this.app != null)
                this.app.setTemp(Integer.parseInt(tfTemp.getText()));
            else
                System.out.println("null");
        }
    }


}

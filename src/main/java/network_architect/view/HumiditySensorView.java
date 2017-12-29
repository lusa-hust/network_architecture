package network_architect.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import network_architect.app.HumiditySensorApp;

public class HumiditySensorView {
    @FXML
    private Circle status;

    @FXML
    private TextField lblHumidity;

    private HumiditySensorApp app;

    @FXML
    public void onUpdate(){
        if (lblHumidity.getText() != null ) {
            System.out.println(lblHumidity.getText());
            if(this.app != null)
                this.app.setHumidity(Integer.parseInt(lblHumidity.getText()));
            else
                System.out.println("null");
        }
    }

    public void setApp(HumiditySensorApp app) {
        this.app = app;
    }
}

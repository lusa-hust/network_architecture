package network_architect.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import network_architect.app.AirConditionApp;
import network_architect.app.LightApp;

public class AirConditioningView {
    private AirConditionApp app;
    public Circle statusAC;
    public Button btnSwitch;
    public TextField lbTemp;
    private boolean _status = false;
    

    public void setApp(AirConditionApp app) {
        this.app = app;
    }

    public void btnClicked(MouseEvent mouseEvent) {
        if(!_status) {
            setStatusAC(true);
        } else {
            setStatusAC(false);
        }
    }

    public void setTemp(String temp){
        if(temp != null) {
            System.out.println("Intensity " + temp);
            lbTemp.setText(temp);
//            app.setTemp(Integer.parseInt(temp));
        }
    }

    public void setStatusAC(Boolean status){
        if(status)
        {
            _status = true;
            statusAC.setFill(Color.GREEN);
            app.setAcStatus(true);
        }
        else
        {
            _status = false;
            statusAC.setFill(Color.GRAY);
            app.setAcStatus(false);
        }
    }
}

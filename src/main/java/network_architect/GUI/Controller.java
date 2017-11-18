package network_architect.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Controller {
    public Label lab, lab1, lab2;
    public Label lab3, lab4, lab5;
    public Label lab6, lab7, lab8;

    public Slider sd, sd1, sd2;
    public Slider sd3, sd4, sd5;
    public Slider sd6, sd7, sd8;

    @FXML
    public void initialize() {
        HashMap<Slider, Label> hmap = new HashMap<Slider, Label>();
        hmap.put(sd, lab);
        hmap.put(sd1, lab1);
        hmap.put(sd2, lab2);
        hmap.put(sd3, lab3);
        hmap.put(sd4, lab4);
        hmap.put(sd5, lab5);
        hmap.put(sd6, lab6);
        hmap.put(sd7, lab7);
        hmap.put(sd8, lab8);

        Set set = hmap.entrySet();
        for (Object aSet : set) {
            Map.Entry entry = (Map.Entry) aSet;
            Slider slider = (Slider) entry.getKey();
            Label label = (Label) entry.getValue();
            slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                label.setText(Double.toString(newValue.intValue()));
            });
        }
    }

}

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ControllerOfThePlanet {

    Logger log = LogManager.getLogger(ControllerOfThePlanet.class.getName());

    @FXML
    private Button help;
    @FXML
    private Button applyPl;
    @FXML
    private TextField namePl;
    @FXML
    private ColorPicker colorPl;
    @FXML
    private TextField radiusPl;
    @FXML
    private TextField positionXPl;
    @FXML
    private TextField positionYPl;
    @FXML
    private TextField degreesPl;
    @FXML
    private TextField speedPl;

    boolean[] filled = new boolean[6];

    private boolean check = false;

    private void enabler(boolean test, int num) {
        filled[num] = test;
        for (var b : filled)
            if (!b) {
                check = false;
                break;
            } else check = true;
        applyPl.setDisable(!check);
    }

    @FXML
    public void initialize(SystemCharacteristic system){
        help.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Help");
            alert.setContentText("For example, radius = 20, position x= 200, position y = -300, speed = 3, degrees= -3");
            alert.showAndWait();
        });

        var regex = "[0-9]+([.,][0-9]+)?";
        namePl.setOnKeyTyped(event -> enabler(!namePl.getText().isBlank(), 0));
        radiusPl.setOnKeyTyped(event -> enabler(radiusPl.getText().matches(regex), 1));
        positionXPl.setOnKeyTyped(event -> enabler(positionXPl.getText().matches("-?" + regex), 2));
        positionYPl.setOnKeyTyped(event -> enabler(positionYPl.getText().matches("-?" + regex), 3));
        degreesPl.setOnKeyTyped(event -> enabler(degreesPl.getText().matches("-?" + regex), 4));
        speedPl.setOnKeyTyped(event -> enabler(speedPl.getText().matches(regex), 5));

        applyPl.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            var stage = (Stage) applyPl.getScene().getWindow();
            stage.close();
            var planet = new PlanetCharacteristic();
            planet.setName(namePl.getText());
            planet.setColor(colorPl.getValue());
            try {
                planet.setRadius(radiusPl.getText());
                planet.setPositionX(positionXPl.getText());
                planet.setPositionY(positionYPl.getText());
                planet.setSpeed(speedPl.getText(), degreesPl.getText());

            } catch (Exception e) {
                log.error("Exception " + e);
                e.printStackTrace();
            }
            system.planet.add(planet);
            log.info(planet.toString());
            var main = new App();
            try {
                if (system.planet.size() == system.numberOfPlanets) main.space(system);
                else main.planetSetup(system);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

}
package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ContactController implements Initializable {
    private Controller controller;
    private Stage stage;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldNumber;
    @FXML
    private Button buttonAdd;
    @FXML
    private Label labelNumberPrompt;
    @FXML
    private Label labelNamePrompt;
    @FXML
    private Label labelInsertPrompt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new Controller();
        textFieldName.requestFocus();

    }

    public void buttonAddAction() {
        String name = textFieldName.getText();
        String number = textFieldNumber.getText();
        if(!name.isEmpty() && !number.isEmpty()){
            if(Variables.isInteger(number)){
                DatabaseHelper.addDataContacts(Variables.CONTACTSDB, name, number);
                labelInsertPrompt.setText("Inserted");
                labelNamePrompt.setText(name);
                labelNumberPrompt.setText(number);
            }
        }

    }


}

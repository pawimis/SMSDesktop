package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ContactController implements Initializable {
    Controller controller;
    @FXML
    private TextField text_field_name;
    @FXML
    private TextField text_field_number;
    @FXML
    private Button button_add;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new Controller();

    }
    public void button_add_action(){
        String name= text_field_name.getText().toString();
        String number = text_field_number.getText().toString();
        if(!name.isEmpty() && !number.isEmpty()){
            if(Variables.isInteger(number)){
                DatabaseHelper.addDatasContacts(Variables.CONTACTSDB,name,number);
            }
        }
    }
}

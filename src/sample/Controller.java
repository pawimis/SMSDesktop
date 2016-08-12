package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import java.net.*;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    String portnumber;
    Server server;
    int info_list_item_mumber = 0;
    @FXML
    private Button button_connect;
    @FXML
    private Button button_send;
    @FXML
    private RadioButton Radiobutton_sendNow;
    @FXML
    private TextField textField_hour;
    @FXML
    private TextField textField_number;
    @FXML
    private TextField textField_portNumber;
    @FXML
    private DatePicker datepicker;
    @FXML
    private TextArea textArea_Message;
    @FXML
    private ListView<String> listView_status;
    @FXML
    private ListView<String> listView_info;
    @FXML
    private Label label_ipAddress;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            label_ipAddress.setText(InetAddress.getLocalHost().getHostAddress());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
    public void button_connect_action(){
        portnumber =  textField_portNumber.getText().toString();
        if(!portnumber.isEmpty()){
            if(isInteger(portnumber)){
                if(isRange(portnumber)){
                    infoSetText("Connecting...");
                    System.out.println("S: Connecting...");
                    button_connect.setDisable(true);
                    server = new Server(portnumber, new Server.OnMessageReceived() {

                        @Override
                        public void messageReceived(String message) {
                            infoSetText("Connected");
                            infoSetText("Your Gcm_id is:");
                            infoSetText(message);
                        }
                    });
                    server.start();
                }else{
                    popupWarning("Range is from 1024 to 65535");
                }
            }else{
                popupWarning("It must be number!");
            }
        }else{
            popupWarning("Choose port number!");

        }
    }
    private void popupWarning(String warning){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(warning);
        alert.showAndWait();
    }
    public  boolean isInteger(String s) {
        try {
             Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }

        return true;
    }
    public boolean isRange(String s){
        int conf = Integer.parseInt(s);
        if((conf >= 1024) && (conf <= 65535))
            return true;
        else
            return false;
    }
    private void infoSetText(String message){
        listView_info.getItems().add(info_list_item_mumber , message);
        info_list_item_mumber++;
    }

}

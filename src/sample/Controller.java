package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    String portnumber;
    String phone_number;
    String message_text;
    ClientDatas clientDatas;
    GCMMessageSend gcmMessageSend;
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

    public void button_send_action() {
        phone_number = textField_number.getText().toString();
        message_text = textArea_Message.getText().toString();
        gcmMessageSend = new GCMMessageSend();
        if (!phone_number.isEmpty()) {
            if (!message_text.isEmpty()) {
                System.out.print("Button pressed");
                String gcm = clientDatas.getClient_GCM_ID();
                System.out.println("gcm : !!!!!!!!!! " + gcm);
                gcmMessageSend.SendMessage(message_text, phone_number, clientDatas.getClient_GCM_ID());
                infoSetText("Message send");
            }

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
                            System.out.println(message);
                            clientDatas = new ClientDatas(message);
                            infoSetText("GCM_ID obtained and saved");
                            server.CloseConnection();
                            infoSetText("Connection established");
                            infoSetText("Closing local connection");


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
        return (conf >= 1024) && (conf <= 65535);
    }
    private void infoSetText(String message){
        listView_info.getItems().add(info_list_item_mumber , message);
        info_list_item_mumber++;
    }

    private void SaveDatas(String text) {

    }

}

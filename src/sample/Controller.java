package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    String portnumber;
    String phone_number;
    String message_text;
    String date;
    String hour_mins_str;

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
    @FXML
    private Button button_save;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            label_ipAddress.setText(InetAddress.getLocalHost().getHostAddress());
            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY);
            int min = now.get(Calendar.MINUTE);
            Connection userCon = DatabaseHelper.connect(Variables.USERDB);
            Connection orderCon = DatabaseHelper.connect(Variables.ORDERDB);
            DatabaseHelper.createTableUser(userCon, Variables.USERDB);
            DatabaseHelper.createTableOrder(orderCon, Variables.ORDERDB);


            textField_hour.setPromptText(Integer.toString(hour) + ":" + Integer.toString(min));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void button_save_action() {

    }
    public void button_send_action() {
        phone_number = textField_number.getText().toString();
        message_text = textArea_Message.getText().toString();
        hour_mins_str = textField_hour.getText().toString();
        date = datepicker.getValue().toString();
        gcmMessageSend = new GCMMessageSend();
        if (!phone_number.isEmpty()) {
            if (!message_text.isEmpty()) {
                if (Radiobutton_sendNow.isSelected()) {
                    System.out.print("Attempt to send ");
                    gcmMessageSend.SendMessage(message_text, phone_number, DatabaseHelper.getUser(Variables.USERDB));
                    infoSetText("Message send");
                } else if (!hour_mins_str.isEmpty() && !date.isEmpty()) {


                } else
                    popupWarning("Pick send now or set date to send message");
            } else
                popupWarning("Message cannot be empty");
        } else
            popupWarning("Give phone number");
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

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    infoSetText("Connected");
                                    infoSetText("GCM_ID obtained and saved");
                                    System.out.println(message);
                                    DatabaseHelper.addDatasUser(Variables.USERDB, message);
                                    server.CloseConnection();
                                    infoSetText("Connection established");
                                    infoSetText("Closing local connection");
                                }
                            });
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



}

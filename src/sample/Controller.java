package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimerTask;

public class Controller implements Initializable {
    String portnumber;
    String phone_number;
    String message_text;
    String date;
    String currdate;
    String currhour;
    String hour_mins_str;
    SimpleDateFormat hourFormat;
    SimpleDateFormat dateFormat;


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
            updateDate();
            System.out.println(currdate + " " + currhour);

            Connection userCon = DatabaseHelper.connect(Variables.USERDB);
            Connection orderCon = DatabaseHelper.connect(Variables.ORDERDB);
            DatabaseHelper.createTableUser(userCon, Variables.USERDB);
            DatabaseHelper.createTableOrder(orderCon, Variables.ORDERDB);
            statusSetText();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void updateDate() {
        Calendar now = Calendar.getInstance();
        hourFormat = new SimpleDateFormat("HH:mm");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currdate = dateFormat.format(now.getTime()).toString();
        currhour = hourFormat.format(now.getTime()).toString();
    }

    public void button_save_action() {

    }
    public void button_send_action() {
        phone_number = textField_number.getText().toString();
        message_text = textArea_Message.getText().toString();
        if (datepicker.getValue() != null && textField_hour.getText() != null) {
            hour_mins_str = textField_hour.getText().toString();
            date = datepicker.getValue().toString();
        } else {
            updateDate();
            hour_mins_str = currhour;
            date = currdate;
        }
        System.out.println(date);
        if (!phone_number.isEmpty()) {
            if (!message_text.isEmpty()) {
                if (Radiobutton_sendNow.isSelected()) {
                    System.out.print("Attempt to send ");
                    String check = DatabaseHelper.getUser(Variables.USERDB);
                    System.out.println(check);
                    DatabaseHelper.addDatasOrder(Variables.ORDERDB, hour_mins_str, date, phone_number, message_text, 1);

                    //GCMMessageSend.SendMessage(message_text, phone_number, DatabaseHelper.getUser(Variables.USERDB));
                    infoSetText("Message send");
                    statusSetText();
                } else if (!hour_mins_str.isEmpty() && !date.isEmpty()) {
                    DatabaseHelper.addDatasOrder(Variables.ORDERDB, hour_mins_str, date, phone_number, message_text, 0);
                    statusSetText();
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

    private void statusSetText() {
        listView_status.getItems().clear();
        ArrayList<String> arrayList = DatabaseHelper.getAllOrders(Variables.ORDERDB);
        int i = 0;
        for (String res : arrayList) {
            listView_status.getItems().add(i, res);
            i++;
        }
    }

    private static class Sheduler extends TimerTask {

        @Override
        public void run() {

        }
    }

}

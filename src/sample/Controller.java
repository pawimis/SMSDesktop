package sample;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller implements Initializable {
    private static final SimpleDateFormat FULLDATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Server server;
    private Stage childStage;
    private ContactController controller;
    private int infoListItemMumber = 0;
    private String currentDate;
    @FXML
    private Button buttonConnect;
    @FXML
    private Button buttonSend;
    @FXML
    private RadioButton RadioButtonSendNow;
    @FXML
    private TextField textFieldHour;
    @FXML
    private TextField textFieldNumber;
    @FXML
    private TextField textFieldPortNumber;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea textAreaMessage;
    @FXML
    private ListView<String> listViewStatus;
    @FXML
    private ListView<String> listView_info;
    @FXML
    private Label label_ipAddress;
    @FXML
    private Button buttonAddContact;
    @FXML
    private ListView<String> list_view_contacts;
    @FXML
    private CheckBox checkBoxDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            label_ipAddress.setText(Variables.getCurrentIp());
            updateDate();
            System.out.println(currentDate);
            Connection userCon = DatabaseHelper.connect(Variables.USERDB);
            Connection orderCon = DatabaseHelper.connect(Variables.ORDERDB);
            Connection contactCon = DatabaseHelper.connect(Variables.CONTACTSDB);
            DatabaseHelper.createTableContacts(contactCon, Variables.CONTACTSDB);
            DatabaseHelper.createTableUser(userCon, Variables.USERDB);
            DatabaseHelper.createTableOrder(orderCon, Variables.ORDERDB);
            statusSetText();
            setContactList();
            RadioButtonSendNow.setOnAction(event -> {
                Boolean confirm = false;
                confirm = RadioButtonSendNow.isSelected();

                datePicker.setDisable(confirm);
                textFieldHour.setDisable(confirm);
            });
            checkBoxDelete.setOnAction(event -> {
                if (checkBoxDelete.isSelected())
                    popupWarning("Choose contact to delete");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void pickContact(MouseEvent e) throws IOException {
        listViewContactSelected();
    }

    private void updateDate() {
        Calendar now = Calendar.getInstance();
        currentDate = FULLDATE_FORMAT.format(now.getTime());
    }

    private void listViewContactSelected() {
        String item;
        String c = "";
        int i = 0;
        if (checkBoxDelete.isSelected()) {
            item = list_view_contacts.getSelectionModel().getSelectedItem();
            while (Variables.isInteger(Character.toString(item.charAt(i)))) {
                c += Character.toString(item.charAt(i));
                i++;
            }
            DatabaseHelper.deleteContact(Variables.CONTACTSDB, c);
            setContactList();

        } else {
            item = list_view_contacts.getSelectionModel().getSelectedItem();
            System.out.println(item);
            while (Variables.isInteger(Character.toString(item.charAt(i)))) {
                c += Character.toString(item.charAt(i));
                i++;
            }
            System.out.println(c);
            item = DatabaseHelper.getContactNumber(Variables.CONTACTSDB, c);
            System.out.println(item);
            textFieldNumber.clear();
            textFieldNumber.setText(item);
        }


    }

    public void buttonAddAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("new_contact.fxml"));
            loader.setController(controller);
            loader.setRoot(controller);
            Parent root = loader.load();
            childStage = new Stage();
            childStage.setScene(new Scene(root));
            childStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        childStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                setContactList();
            }
        });
    }

    public void buttonSendAction() {
        String date = null;
        String hour_mins_str = null;
        String phoneNumber = textFieldNumber.getText();
        String messageText = textAreaMessage.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fullDate;
        if (datePicker.getValue() != null && textFieldHour.getText() != null) {
            hour_mins_str = textFieldHour.getText();
            date = datePicker.getValue().format(formatter);
            fullDate = date + " " + hour_mins_str;
        } else {
            updateDate();
            fullDate = currentDate;
        }
        System.out.println(fullDate);
        if (!phoneNumber.isEmpty()) {
            if (!messageText.isEmpty()) {
                if (RadioButtonSendNow.isSelected()) {
                    System.out.print("Attempt to send ");
                    String check = DatabaseHelper.getUser(Variables.USERDB);
                    System.out.println(check);
                    DatabaseHelper.addDatasOrder(Variables.ORDERDB, fullDate, phoneNumber, messageText, 1);

                    GCMMessageSend.SendMessage(messageText, phoneNumber, DatabaseHelper.getUser(Variables.USERDB));
                    infoSetText("Message send");
                    statusSetText();
                } else if (!hour_mins_str.isEmpty() && !date.isEmpty()) {
                    String id = DatabaseHelper.addDatasOrder(Variables.ORDERDB, fullDate, phoneNumber, messageText, 0);
                    Timer timer = new Timer();
                    try {
                        timer.schedule(new Scheduler(id, listViewStatus), FULLDATE_FORMAT.parse(fullDate));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    statusSetText();
                } else
                    popupWarning("Pick send now or set date to send message");
            } else
                popupWarning("Message cannot be empty");
        } else
            popupWarning("Give phone number");

        datePicker.setDisable(false);
        textFieldHour.setDisable(false);
    }

    public void buttonConnectAction() {
        String portNumber = textFieldPortNumber.getText();
        if (!portNumber.isEmpty()) {
            if (Variables.isInteger(portNumber)) {
                if (Variables.isRange(portNumber)) {
                    infoSetText("Connecting...");
                    System.out.println("S: Connecting...");
                    buttonConnect.setDisable(true);
                    server = new Server(portNumber, new Server.OnMessageReceived() {

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
                } else {
                    popupWarning("Range is from 1024 to 65535");
                }
            } else {
                popupWarning("It must be number!");
            }
        } else {
            popupWarning("Choose port number!");

        }
    }

    private void popupWarning(String warning) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(warning);
        alert.showAndWait();
    }

    private void infoSetText(String message) {
        listView_info.getItems().add(infoListItemMumber, message);
        infoListItemMumber++;
    }

    private void statusSetText() {
        listViewStatus.getItems().clear();
        ArrayList<String> arrayList = DatabaseHelper.getAllOrders(Variables.ORDERDB);
        int i = 0;
        for (String res : arrayList) {
            listViewStatus.getItems().add(i, res);
            i++;
        }
    }

    private void setContactList() {
        list_view_contacts.getItems().clear();
        ArrayList<String> arrayList = DatabaseHelper.getAllContacts(Variables.CONTACTSDB);
        int i = 0;
        for (String res : arrayList) {
            list_view_contacts.getItems().add(i, res);
            i++;
        }
    }

    private static class Scheduler extends TimerTask {

        private final String identifier;
        ListView list;

        Scheduler(String i, ListView list) {
            this.identifier = i;
            this.list = list;
        }

        @Override
        public void run() {
            System.out.println("Sending scheduled message");
            ArrayList<String> recQuer = new ArrayList<>();
            recQuer = DatabaseHelper.getOrderOnID(identifier, Variables.ORDERDB);
            String id = recQuer.get(0);
            String text = recQuer.get(1);
            String number = recQuer.get(2);
            System.out.println(id + " " + text + " " + number);
            DatabaseHelper.setOrderSend(Variables.ORDERDB, id);
            GCMMessageSend.SendMessage(text, number, DatabaseHelper.getUser(Variables.USERDB));
            DatabaseHelper.setOrderSend(Variables.ORDERDB, id);
            Random rand = new Random();
            int random = rand.nextInt(100);
            try {
                wait(random);
            } catch (Exception e) {
                e.printStackTrace();
            }
            list.getItems().clear();
            ArrayList<String> arrayList = DatabaseHelper.getAllOrders(Variables.ORDERDB);

            int i = 0;
            for (String res : arrayList) {
                list.getItems().add(i, res);
                i++;
            }

        }
    }

}

package sample;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by pmisi on 12.08.2016.
 */
class DatabaseHelper {
    public static Connection connect(String dbname) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbname + ".db");
            System.out.println("Connected with database");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void createTableOrder(Connection connection, String table) {
        Statement stat = null;
        try {
            stat = connection.createStatement();
            String sqlTable = "CREATE TABLE IF NOT EXISTS " + table
                    + "( ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " DATE      DATETIME    NOT NULL,"
                    + " NUMBER    CHAR(20)    NOT NULL,"
                    + " MESSAGE_TEXT TEXT NOT NULL,"
                    + " SEND      INT)";
            stat.executeUpdate(sqlTable);
            stat.close();
            connection.close();
            System.out.println("Order DB created");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            System.out.println(ex.getCause());
            System.out.println(ex.getErrorCode());
        }
    }

    public static void createTableUser(Connection connection, String table) {
        Statement stat = null;
        try {
            stat = connection.createStatement();
            String sqlTable = "CREATE TABLE IF NOT EXISTS " + table
                    + " (GCM_ID      TEXT    NOT NULL)";

            stat.executeUpdate(sqlTable);
            stat.close();
            connection.close();
            System.out.println("User DB created");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            System.out.println(ex.getCause());
            System.out.println(ex.getErrorCode());
        }
    }

    public static void addDatasOrder(String dbname, String date, String number, String text, int send) {
        Connection connection = null;
        Statement stat = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbname + ".db");

            stat = connection.createStatement();
            String query = "INSERT INTO " + dbname + " (ID,DATE,NUMBER,MESSAGE_TEXT,SEND) "
                    + "VALUES (NULL,"
                    + "'" + date + "',"
                    + "'" + number + "',"
                    + "'" + text + "',"
                    + send + ");";
            stat.executeUpdate(query);
            stat.close();
            connection.close();
            System.out.println("ORDER: \n" + query + "\n done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addDatasUser(String dbname, String gcm_id) {
        Connection connection = null;
        Statement stat = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbname + ".db");

            stat = connection.createStatement();
            String query = "INSERT INTO " + dbname + " (GCM_ID) "
                    + "VALUES ("
                    + "'" + gcm_id + "');";
            stat.executeUpdate(query);
            stat.close();
            connection.close();
            System.out.println("ORDER: \n" + query + "\n done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUser(String dbname) {
        Connection connection = null;
        String result = null;
        Statement stat = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbname + ".db");

            stat = connection.createStatement();
            String query = "SELECT * FROM " + dbname + " ;";


            ResultSet res = stat.executeQuery(query);
            result = res.getString("GCM_ID");
            stat.close();
            connection.close();
            System.out.println("ORDER: \n" + query + "\n done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<String> getAllOrders(String dbname) {
        ArrayList<String> orderList = new ArrayList<String>();
        Connection connection = null;
        String result = null;
        Statement stat = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbname + ".db");

            stat = connection.createStatement();
            String query = "SELECT * FROM " + dbname + " ;";


            ResultSet res = stat.executeQuery(query);
            while (res.next()) {
                String id = res.getString("ID");
                String date = res.getString("DATE");
                String number = res.getString("NUMBER");
                int send = res.getInt("SEND");
                String sendRes = "not send";
                if (send == 1)
                    sendRes = "send";
                result = id + ". " + date + "\n" + number + " " + sendRes;

                orderList.add(result);
            }
            stat.close();
            connection.close();
            System.out.println("ORDER: \n" + query + "\n done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public static String getOrderOnTime(String time, String dbname) {
        Connection connection = null;
        String result = null;
        Statement stat = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbname + ".db");

            stat = connection.createStatement();
            String query = "SELECT * FROM " + dbname + " WHERE DATE == '" + time + "';";

            ResultSet res = stat.executeQuery(query);
            String id = res.getString("ID");
            String message_text = res.getString("MESSAGE_TEXT");
            String number = res.getString("NUMBER");

            result = id + " " + message_text + " " + number;

            stat.close();
            connection.close();
            System.out.println("ORDER: \n" + query + "\n done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
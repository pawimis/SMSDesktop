package sample;

import java.sql.*;
import java.util.ArrayList;

class DatabaseHelper {
    static Connection connect(String dbname) {
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

    static void createTableOrder(Connection connection, String table) {
        Statement stat;
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

    static void createTableUser(Connection connection, String table) {
        Statement stat;
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

    static void createTableContacts(Connection connection, String table) {
        Statement stat;
        try {
            stat = connection.createStatement();
            String sqlTable = "CREATE TABLE IF NOT EXISTS " + table
                    + "( ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " NUMBER    CHAR(20)    NOT NULL,"
                    + " NAME      TEXT    NOT NULL)";

            stat.executeUpdate(sqlTable);
            stat.close();
            connection.close();
            System.out.println("Contacts DB created");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            System.out.println(ex.getCause());
            System.out.println(ex.getErrorCode());
        }
    }

    static void addDataContacts(String dbname, String name, String number) {
        Connection connection = null;
        Statement stat = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbname + ".db");

            stat = connection.createStatement();
            String query = "INSERT INTO " + dbname + " (ID,NUMBER,NAME) "
                    + "VALUES (NULL,"
                    + "'" + number + "',"
                    + "'" + name + "');";

            stat.executeUpdate(query);
            stat.close();
            connection.close();
            System.out.println("ORDER: \n" + query + "\n done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String addDatasOrder(String dbname, String date, String number, String text, int send) {
        Connection connection;
        Statement stat;
        String id = null;
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
            query = "SELECT ID FROM " + dbname + " WHERE MESSAGE_TEXT = '" + text + "' AND NUMBER = '" + number
                    + "' AND  DATE = '"+ date +"' ;";
            ResultSet res = stat.executeQuery(query);
            id = res.getString("ID");
            stat.close();
            connection.close();
            System.out.println("ORDER: \n" + query + "\n done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    static void addDatasUser(String dbname, String gcm_id) {
        Connection connection;
        Statement stat;
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

    static ArrayList<String> getAllContacts(String dbname) {
        ArrayList<String> orderList = new ArrayList<>();
        Connection connection;
        String result;
        Statement stat;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbname + ".db");

            stat = connection.createStatement();
            String query = "SELECT * FROM " + dbname + " ;";


            ResultSet res = stat.executeQuery(query);
            while (res.next()) {
                String id = res.getString("ID");
                String number = res.getString("NUMBER");
                String name = res.getString("NAME");

                result = id + ". " + name + "\n" + number;

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

    static String getContactNumber(String dbname, char identifier) {
        Connection connection;
        String result = null;
        Statement stat;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbname + ".db");

            stat = connection.createStatement();
            String query = "SELECT * FROM " + dbname + " WHERE ID = '"+identifier+"';";


            ResultSet res = stat.executeQuery(query);
                String number = res.getString("NUMBER");


                result =  number;
            stat.close();
            connection.close();
            System.out.println("ORDER: \n" + query + "\n done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    static String getUser(String dbname) {
        Connection connection;
        String result = null;
        Statement stat;
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

    static ArrayList<String> getAllOrders(String dbname) {
        ArrayList<String> orderList = new ArrayList<>();
        Connection connection;
        String result;
        Statement stat;
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

    static ArrayList<String> getOrderOnID(String id, String dbname) {
        Connection connection;
        ArrayList<String> result = new ArrayList<>();
        Statement stat;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbname + ".db");

            stat = connection.createStatement();
            String query = "SELECT * FROM " + dbname + " WHERE ID = '" + id + "';";

            ResultSet res = stat.executeQuery(query);
            result.add(res.getString("ID"));
            result.add(res.getString("MESSAGE_TEXT"));
            result.add(res.getString("NUMBER"));


            stat.close();
            connection.close();
            System.out.println("ORDER: \n" + query + "\n done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    static void setOrderSend(String dbname, String identify) {
        Connection connection;
        Statement stat;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbname + ".db");

            stat = connection.createStatement();
            String query = "UPDATE " + dbname + " SET SEND = 1 WHERE ID = '" + identify + "';";

            stat.executeUpdate(query);
            connection.close();
            System.out.println("ORDER: \n" + query + "\n done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
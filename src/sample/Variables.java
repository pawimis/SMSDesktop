package sample;

/**
 * Created by pmisi on 12.08.2016.
 */
public class Variables {
    public static final String SERVERAPIKEY = "xxxx";
    public static final String USERDB = "userDb";
    public static final String ORDERDB = "orderDb";
    public static final String CONTACTSDB= "contactsDb";

    public static   boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }

        return true;
    }
    public static boolean isRange(String s){
        int conf = Integer.parseInt(s);
        return (conf >= 1024) && (conf <= 65535);
    }
}

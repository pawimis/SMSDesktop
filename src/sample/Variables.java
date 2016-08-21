package sample;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

class Variables {
    static final String SERVERAPIKEY = "xxxx";
    static final String USERDB = "userDb";
    static final String ORDERDB = "orderDb";
    static final String CONTACTSDB = "contactsDb";

    static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    static boolean isRange(String s) {
        int conf = Integer.parseInt(s);
        return (conf >= 1024) && (conf <= 65535);
    }

    static String getCurrentIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces
                        .nextElement();
                Enumeration<InetAddress> nias = ni.getInetAddresses();
                while (nias.hasMoreElements()) {
                    InetAddress ia = nias.nextElement();
                    if (!ia.isLinkLocalAddress()
                            && !ia.isLoopbackAddress()
                            && ia instanceof Inet4Address) {
                        return ia.toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("unable to get current IP");
        }
        return null;
    }
}

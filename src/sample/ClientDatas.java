package sample;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pmisi on 12.08.2016.
 */
public class ClientDatas implements Serializable {
    private String client_GCM_ID;
    private ArrayList<String> contact_List;


    public ClientDatas(String Gcm_id) {
        System.out.print("Client constructed");
        this.client_GCM_ID = Gcm_id;
    }

    @Override
    public String toString() {
        return new StringBuffer("Client gcm_id :")
                .append(this.client_GCM_ID)
                .append("Contact list")
                .append(this.contact_List).toString();
    }

    public String getClient_GCM_ID() {
        return client_GCM_ID;
    }
}

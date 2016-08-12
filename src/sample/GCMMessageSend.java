package sample;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pmisi on 12.08.2016.
 */
public class GCMMessageSend {


    public static void SendMessage(String messageText, String number, String clientGCM) {
        try {
            System.out.print("attempt to send");
            JSONObject jsonObject = new JSONObject();
            JSONObject jData = new JSONObject();
            jData.put("message", messageText);
            jData.put("number", number);
            jData.put("topic", "sms");
            jsonObject.put("to", clientGCM);
            jsonObject.put("data", jData);

            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + Variables.SERVERAPIKEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jsonObject.toString().getBytes());

            InputStream inputStream = conn.getInputStream();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String inp;
                String output = "";
                while ((inp = bufferedReader.readLine().toString()) != null)
                    output = inp;

                if (!output.isEmpty())
                    System.out.println(output);
                else
                    System.out.print("no output");

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

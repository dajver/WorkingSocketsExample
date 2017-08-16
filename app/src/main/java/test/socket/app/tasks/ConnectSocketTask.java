package test.socket.app.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

import static android.content.ContentValues.TAG;

/**
 * Created by gleb on 8/16/17.
 */

public class ConnectSocketTask extends AsyncTask<Void, String, String> {

    public static String URL = "54.86.103.211";
    public static int PORT = 8080;

    private Socket socket;
    private OnConnectPostExecuteListener onConnectPostExecuteListener;

    public ConnectSocketTask(OnConnectPostExecuteListener onConnectPostExecuteListener) {
        this.onConnectPostExecuteListener = onConnectPostExecuteListener;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            socket = new Socket(URL, PORT);
            if(socket.isConnected()) {
                Log.e(TAG, "connected");
                return "connected";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("connected"))
            onConnectPostExecuteListener.onConnectPostExecute();
        super.onPostExecute(result);
    }

    public Socket getSocket() {
        return socket;
    }

    public interface OnConnectPostExecuteListener {
        void onConnectPostExecute();
    }
}
package test.socket.app.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import static android.content.ContentValues.TAG;

/**
 * Created by gleb on 8/16/17.
 */

public class SendMessageTask extends AsyncTask<String, String, String> {

    private boolean connected = false;
    private Socket socket;
    private OnSendPostExecuteListener onSendPostExecuteListener;

    public SendMessageTask(boolean connected, Socket socket, OnSendPostExecuteListener onSendPostExecuteListener) {
        this.connected = connected;
        this.socket = socket;
        this.onSendPostExecuteListener = onSendPostExecuteListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String st = null;
        connected = true;
        while(connected) {
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "windows-1251")), true);
                out.println(" { " + params[0] + " }");
                InputStreamReader streamReader = new InputStreamReader(socket.getInputStream(), "windows-1251");
                BufferedReader reader = new BufferedReader(streamReader);
                st = reader.readLine();
                Log.e(TAG, "message: "+st);
            } catch (Exception e) {
                Log.e(TAG, "Error ", e);
            }
        }
        return st;
    }

    @Override
    protected void onPostExecute(String result) {
        onSendPostExecuteListener.onSendPostExecute(result);
        super.onPostExecute(result);
    }

    public interface OnSendPostExecuteListener {
        void onSendPostExecute(String text);
    }
}
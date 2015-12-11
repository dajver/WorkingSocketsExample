package test.socket.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "test.socket.app";

    public String URL = "92.47.46.118";
    public int PORT = 1169;
    private Button button;
    private Button connect;
    private TextView textView;
    private EditText editText;
    private boolean connected = false;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        connect = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendMessage().execute(editText.getText().toString());
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConnectSocket().execute();
            }
        });
    }

    class ConnectSocket extends AsyncTask<Void, String, String> {

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
            if(result.equals("connected")) {
                connect.setVisibility(View.GONE);
            }
            super.onPostExecute(result);
        }
    }

    class SendMessage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String st = null;
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            connected = true;
//            while(connected) {
                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "windows-1251")), true);
                    out.println(" { " + params[0] + " }");
                    InputStreamReader streamReader = new InputStreamReader(socket.getInputStream(), "windows-1251");
                    BufferedReader reader = new BufferedReader(streamReader);
                    st = reader.readLine();
                    Log.e(TAG, st);
                } catch (Exception e) {
                    Log.e(TAG, "Error ", e);
                }
//            }
            return st;
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
            super.onPostExecute(result);
        }
    }

    @Override
    public void onStop() {
        try {
            if(socket != null) {
                socket.close();
                Log.e(TAG, "disconnected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }
}

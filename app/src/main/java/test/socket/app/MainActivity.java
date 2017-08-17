package test.socket.app;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String HOST = "ws://stark-caverns-76076.herokuapp.com";

    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.textView)
    TextView textView;

    private WebSocketClient mWebSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        connectWebSocket();
    }

    private void connectWebSocket() {
        try {
            URI uri = new URI(HOST);
            mWebSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    mWebSocketClient.send("Connecting from " + Build.MANUFACTURER + " " + Build.MODEL);
                }

                @Override
                public void onMessage(String s) {
                    final String message = s;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(message);
                        }
                    });
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.i("Websocket", "Connection Closed " + s);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            };
            mWebSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
    }

    @OnClick(R.id.button)
    public void onSendClick() {
        mWebSocketClient.send(editText.getText().toString());
        editText.setText("");
    }
}

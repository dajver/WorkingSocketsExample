package test.socket.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.socket.app.tasks.ConnectSocketTask;
import test.socket.app.tasks.SendMessageTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SendMessageTask.OnSendPostExecuteListener, ConnectSocketTask.OnConnectPostExecuteListener {

    private static final String TAG = "test.socket.app";

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.button2)
    Button connect;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.editText)
    EditText editText;

    private boolean connected = false;
    private SendMessageTask sendMessage;
    private ConnectSocketTask connectSocketTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        button.setOnClickListener(this);
        connect.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                sendMessage = new SendMessageTask(connected, connectSocketTask.getSocket(), this);
                sendMessage.execute(editText.getText().toString());
                break;
            case R.id.button2:
                connectSocketTask = new ConnectSocketTask(this);
                connectSocketTask.execute();
                break;
        }
    }

    @Override
    public void onSendPostExecute(String text) {
        textView.setText(text);
    }

    @Override
    public void onConnectPostExecute() {
        connect.setVisibility(View.GONE);
    }
}

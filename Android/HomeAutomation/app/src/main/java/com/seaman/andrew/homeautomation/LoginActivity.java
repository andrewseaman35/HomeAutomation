package com.seaman.andrew.homeautomation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;

public class LoginActivity extends Activity {

    // used to send username to main activity via intent
    public final static String USER = "com.seaman.andrew.homeautomation.Main.USER";
    private TextView loadingText;
    private EditText username;
    private EditText password;

    ActivityLogger log = new ActivityLogger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // two fields holding log in information
        username = (EditText)findViewById(R.id.inUsername);
        password = (EditText)findViewById(R.id.inPassword);

        // log in button logs in
        final Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn(view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int logIn(View v)
    {
        // gets edit text input
        String userString = username.getText().toString();
        String passString = password.getText().toString();

        // change this when we get to it, if log in is accepted, start main activity
        if(userString.equals("Andrew") && passString.equals("pass")) {

            log.addLog(Constants.USER_LOGIN + userString);
            startService(new Intent(this, ConnectionService.class));

            Intent intent = new Intent(this, Main.class);
            intent.putExtra(USER, userString);
            startActivity(intent);
           }

        else
        {
            Toast.makeText(v.getContext(), Constants.LOGIN_FAIL, Toast.LENGTH_SHORT).show();
        }

        return 0;
    }
}

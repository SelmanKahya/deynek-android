package com.deynek.app.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.deynek.app.R;
import com.deynek.app.activity.MainActivity;
import com.deynek.app.model.MyActivity;
import com.deynek.app.session.SessionManager;
import com.deynek.app.util.AlertDialogManager;
import com.deynek.app.util.Utility;

public class LoginActivity extends MyActivity {

    // Email, password edit text
    private EditText textEmail, textPassword;

    // login button
    private Button btnLogin;

    // Alert Dialog Manager
    public AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_login);

        // Session Manager
        session = new SessionManager();

        // Email, Password input text
        textEmail = (EditText) findViewById(R.id.textEmail);
        textPassword = (EditText) findViewById(R.id.textPassword);

        // Login button
        btnLogin = (Button) findViewById(R.id.buttonLogin);

        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get username, password from EditText
                String username = textEmail.getText().toString();
                String password = textPassword.getText().toString();
                String hashedPassword = Utility.computeMD5Hash(password);

                if(session.login(username, hashedPassword)){
                    // Staring MainActivity
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }

                else {
                    alert.showAlertDialog(LoginActivity.this, "Login failed..", "Username or password was incorrect.", false);
                }
            }
        });
    }
}

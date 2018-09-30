package com.at.safechat;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private static final String LOG = MainActivity.class.getSimpleName();

    private Boolean signUpModeActive = true;

    private ConstraintLayout rootLayout;

    private ImageView logoImageView;

    private Button signUpButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.mainActivityRootLayout);

        logoImageView = findViewById(R.id.logoImageView);
        signUpButton = findViewById(R.id.buttonSignUp);
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginTextView = findViewById(R.id.textViewLogin);

        if(ParseUser.getCurrentUser() != null) {
            showUserList();
        }

        // Login text view set onClick
        loginTextView.setOnClickListener(this);
        // Key pressed listener
        passwordEditText.setOnKeyListener(this);
        rootLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);

    }

    public void onSignUpButtonClicked(View view) {
        if ((usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals(""))) {
            Toast.makeText(this, "Username and Password are required!", Toast.LENGTH_SHORT).show();
        } else {
            if (signUpModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            showUserList();
                            Log.i(LOG, "User signUp is Successful!");
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null && user != null) {
                            showUserList();
                            Log.i(LOG, "Login successful!");
                        } else if (e != null) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == loginTextView) {
            if (signUpModeActive) {
                signUpModeActive = false;
                signUpButton.setText(R.string.login_button_text);
                loginTextView.setText(R.string.or_sign_up_text_view);
            } else {
                signUpModeActive = true;
                signUpButton.setText(R.string.sign_up);
                loginTextView.setText(R.string.or_login_button_text);
            }
        } else if (view == rootLayout || view == logoImageView) {
            // Hide keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            onSignUpButtonClicked(view);
        }
        return false;
    }

    public void showUserList() {
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
    }
}

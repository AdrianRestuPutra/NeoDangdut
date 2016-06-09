package com.pitados.neodangdut.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.UserLoginData;
import com.pitados.neodangdut.util.ApiManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private UserLoginData userLoginData;

    // PAGE
    private RelativeLayout panelLogin, panelSignupContainer, panelSignupPage1, panelSignupPage2;

    // WIDGETS
    // LOGIN
    private EditText loginUsername, loginPassword;
    private Button loginbutton, goToSignupButton, facebookButton;
    // TODO signup page 1

    // TODO signup page 2

    // SIGNUP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userLoginData = new UserLoginData(getBaseContext());
        ApiManager.getInstance().setContext(getBaseContext());

        if(userLoginData.getUsername().length() > 0) {
            // TODO intent to main activity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
        } else {
            initPanel();
            initLoginPage();
            // TODO init signup
        }
    }

    private void initPanel() {
        panelLogin = (RelativeLayout) findViewById(R.id.panel_login);
        panelSignupContainer = (RelativeLayout) findViewById(R.id.panel_signup);
        panelSignupPage1 = (RelativeLayout) findViewById(R.id.signup_first_page);
        panelSignupPage2 = (RelativeLayout) findViewById(R.id.signup_second_page);

        panelLogin.setVisibility(View.VISIBLE);

        panelSignupContainer.setVisibility(View.INVISIBLE);
        panelSignupPage1.setVisibility(View.VISIBLE);
        panelSignupPage2.setVisibility(View.INVISIBLE);
    }

    private void initLoginPage() {
        loginUsername = (EditText) findViewById(R.id.login_form_username);
        loginPassword = (EditText) findViewById(R.id.login_form_password);
        loginbutton = (Button) findViewById(R.id.login_button_login);
        facebookButton = (Button) findViewById(R.id.login_facebook_button);
        goToSignupButton = (Button) findViewById(R.id.login_signup);

        loginbutton.setOnClickListener(this);
        facebookButton.setOnClickListener(this);
        goToSignupButton.setOnClickListener(this);
    }

    private void validateLoginForm() {
        String username = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();

        if(username.length() > 0 && password.length() > 0) {
            userLoginData.setUsername(username);
            userLoginData.setPassword(password);

            ApiManager.getInstance().getUserAccessToken();
            ApiManager.getInstance().setOnUserAccessTokenReceved(new ApiManager.OnUserAccessTokenReceived() {
                @Override
                public void onUserAccessTokenSaved() {
                    String refreshToken = ApiManager.getInstance().REFRESH_TOKEN;

                    userLoginData.setRefreshToken(refreshToken);

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            });

        } else {
            Toast.makeText(getBaseContext(), "Username or Password incorrect", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        // LOGIN
        if(view == loginbutton) {
            validateLoginForm();
        }

        if(view == facebookButton) {

        }

        if(view == goToSignupButton) {
            panelLogin.setVisibility(View.INVISIBLE);
            panelSignupContainer.setVisibility(View.VISIBLE);
        }
        // LOGIN End
    }
}

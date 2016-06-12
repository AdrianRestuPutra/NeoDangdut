package com.pitados.neodangdut.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pitados.neodangdut.Popup.PopupLoading;
import com.pitados.neodangdut.R;
import com.pitados.neodangdut.model.RegisterModel;
import com.pitados.neodangdut.model.UserLoginData;
import com.pitados.neodangdut.util.ApiManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private enum PagePosition {
        LOGIN,
        SIGNUP_1,
        SIGNUP_2
    }
    private PagePosition position;

    private UserLoginData userLoginData;

    // PAGE
    private RelativeLayout panelLogin, panelSignupContainer, panelSignupPage1, panelSignupPage2;

    // WIDGETS
    // LOGIN
    private EditText loginUsername, loginPassword;
    private Button loginbutton, goToSignupButton, facebookButton;
    // TODO signup page 1
    private EditText signupUsername, signupPassword, signupConfirmPassword,
                    signupEmail, signupConfirmEmail;
    private Button signupNext;

    // TODO signup page 2
    private EditText signupFirstName, signupLastName, signupCity, signupCountry,
                    signupBirthDay, signupBirthMonth, signupBirthYear;
    private CheckBox genderMale, genderFemale, signupAgree;
    private Button signupButton;

    // Data
    String username, password, email,
            firstName, lastName, city, country, birthday, gender;

    private PopupLoading popupLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userLoginData = new UserLoginData(getBaseContext());
        ApiManager.getInstance().setContext(getBaseContext());
        position = PagePosition.LOGIN;

        if(userLoginData.getUsername().length() > 0) {
            // TODO intent to main activity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
        } else {
            popupLoading = new PopupLoading(LoginActivity.this, R.style.custom_dialog);

            initPanel();
            initLoginPage();
            initSignupPage();

            ApiManager.getInstance().setContext(LoginActivity.this);
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

    private void initSignupPage() {
        signupUsername = (EditText) findViewById(R.id.signup_username);
        signupPassword = (EditText) findViewById(R.id.signup_password);
        signupConfirmPassword = (EditText) findViewById(R.id.signup_confirm_password);
        signupEmail = (EditText) findViewById(R.id.signup_email);
        signupConfirmEmail = (EditText) findViewById(R.id.signup_confirm_email);
        signupNext = (Button) findViewById(R.id.signup_button_next);

        signupFirstName = (EditText) findViewById(R.id.signup_firstname);
        signupLastName = (EditText) findViewById(R.id.signup_lastname);
        signupCity = (EditText) findViewById(R.id.signup_city);
        signupCountry = (EditText) findViewById(R.id.signup_country);
        signupBirthDay = (EditText) findViewById(R.id.signup_birth_day);
        signupBirthMonth = (EditText) findViewById(R.id.signup_birth_month);
        signupBirthYear = (EditText) findViewById(R.id.signup_birth_year);

        // Checkbox
        genderMale = (CheckBox) findViewById(R.id.signup_gender_male);
        genderFemale = (CheckBox) findViewById(R.id.signup_gender_female);
        signupAgree = (CheckBox) findViewById(R.id.signup_agree);

        signupButton = (Button) findViewById(R.id.signup_button_signup);

        gender = "";

        genderMale.setOnClickListener(this);
        genderFemale.setOnClickListener(this);
        signupAgree.setOnClickListener(this);

        signupNext.setOnClickListener(this);
        signupButton.setOnClickListener(this);
    }

    private void validateLoginForm() {
        String username = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();

        if(username.length() > 0 && password.length() > 0) {
            userLoginData.setUsername(username);
            userLoginData.setPassword(password);

            popupLoading.showPopupLoading("Logging in..");
            ApiManager.getInstance().getUserAccessToken();
            ApiManager.getInstance().setOnUserAccessTokenReceved(new ApiManager.OnUserAccessTokenReceived() {
                @Override
                public void onUserAccessTokenSaved() {
                    String refreshToken = ApiManager.getInstance().REFRESH_TOKEN;

                    userLoginData.setRefreshToken(refreshToken);

                    popupLoading.closePopupLoading();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                }
            });

        } else {
            Toast.makeText(getBaseContext(), "Username or Password incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateSignupPage1() {
        username = signupUsername.getText().toString();
        password = signupPassword.getText().toString();
        String confirmPassword = signupConfirmPassword.getText().toString();
        email = signupEmail.getText().toString();
        String confirmEmail = signupConfirmEmail.getText().toString();

        if(username.length() < 1 || password.length() < 1 || email.length() < 1 ||
                (!password.equals(confirmPassword)) || (!email.equals(confirmEmail))) {
            return false;
        }

        return true;
    }

    private boolean validateSignupPage2() {
        firstName = signupFirstName.getText().toString();
        lastName = signupLastName.getText().toString();
        city = signupCity.getText().toString();
        country = signupCountry.getText().toString();

        String day = signupBirthDay.getText().toString();
        String month = signupBirthMonth.getText().toString();
        String year = signupBirthYear.getText().toString();

        int dayValue = (day.length() > 0) ? Integer.valueOf(day) : 0;
        int monthValue = (month.length() > 0) ? Integer.valueOf(month) : 0;

        if( firstName.length() < 1 || (!genderMale.isChecked() && !genderFemale.isChecked()) ||
            !signupAgree.isChecked()) {
            return false;
        }

        if(day.length() > 0) {
            if(day.length() < 1 || month.length() < 1 || year.length() < 1 ||
                (dayValue < 1 || dayValue > 31) || (monthValue < 1 || monthValue > 12) || year.length() > 4)
                return false;
        }

        birthday = year + "-" + month + "-" + day;
        gender = (genderMale.isChecked()) ? "male" : "female";

        return true;
    }

    private void clearForm() {
        signupUsername.setText("");
        signupPassword.setText("");
        signupConfirmPassword.setText("");
        signupEmail.setText("");
        signupConfirmEmail.setText("");

        signupFirstName.setText("");
        signupLastName.setText("");
        signupCity.setText("");
        signupCountry.setText("");
        signupBirthDay.setText("");
        signupBirthMonth.setText("");
        signupBirthYear.setText("");
        genderMale.setChecked(false);
        genderFemale.setChecked(false);
        signupAgree.setChecked(false);
    }

    @Override
    public void onBackPressed() {
        if(position == PagePosition.LOGIN) {
            LoginActivity.this.finish();
        }

        if(position == PagePosition.SIGNUP_1) {
            panelLogin.setVisibility(View.VISIBLE);
            panelSignupContainer.setVisibility(View.INVISIBLE);
            position = PagePosition.LOGIN;
        }

        if(position == PagePosition.SIGNUP_2) {
            panelSignupPage1.setVisibility(View.VISIBLE);
            panelSignupPage2.setVisibility(View.INVISIBLE);
            position = PagePosition.SIGNUP_1;
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
            panelSignupPage1.setVisibility(View.VISIBLE);
            position = PagePosition.SIGNUP_1;

            username = password = email = firstName = lastName = gender = "";
        }
        // LOGIN End

        if(view == signupNext) {
            if(validateSignupPage1() == true) {
                panelSignupPage1.setVisibility(View.INVISIBLE);
                panelSignupPage2.setVisibility(View.VISIBLE);
                position = PagePosition.SIGNUP_2;

                gender = "";
            } else {
                popupLoading.showPopupLoading("Incorrect Input!");
            }
        }

        if(view == signupButton) {
            // TODO validate
            if(validateSignupPage2() == true) {
                RegisterModel registerData = new RegisterModel(username, password, email,
                                                            firstName, lastName, gender, birthday, city, country);

                ApiManager.getInstance().getRegisterToken(registerData);
                ApiManager.getInstance().setOnRegisterListener(new ApiManager.OnRegister() {
                    @Override
                    public void onSucceed() {
                        Toast.makeText(getBaseContext(), "Signed Up!", Toast.LENGTH_SHORT).show();
                        panelLogin.setVisibility(View.VISIBLE);
                        panelSignupContainer.setVisibility(View.INVISIBLE);

                        clearForm();
                    }

                    @Override
                    public void onFailed(String message) {

                    }
                });
            } else {
                popupLoading.showPopupLoading("Incorrect Input!");
            }
        }

        // Checkbox
        if(view == genderMale) {
            boolean state = genderMale.isChecked();
            genderMale.setChecked(state);

            if(state == true) {
                genderFemale.setChecked(false);
            }
        }

        if(view == genderFemale) {
            boolean state = genderFemale.isChecked();
            genderFemale.setChecked(state);

            if(state == true) {
                genderMale.setChecked(false);
            }
        }

        if(view == signupAgree) {
            boolean state = signupAgree.isChecked();
            signupAgree.setChecked(state);
        }
    }
}

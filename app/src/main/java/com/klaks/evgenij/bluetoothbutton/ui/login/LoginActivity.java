package com.klaks.evgenij.bluetoothbutton.ui.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.klaks.evgenij.bluetoothbutton.QueryPreferences;
import com.klaks.evgenij.bluetoothbutton.R;
import com.klaks.evgenij.bluetoothbutton.model.Auth;
import com.klaks.evgenij.bluetoothbutton.network.ApiFactory;
import com.klaks.evgenij.bluetoothbutton.ui.BaseActivity;
import com.klaks.evgenij.bluetoothbutton.ui.main.MainActivity;
import com.klaks.evgenij.bluetoothbutton.util.HelpTransformer;

import io.reactivex.functions.Consumer;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    private AutoCompleteTextView phone;
    private EditText password;
    private View progressView;
    private View loginFormView;
    private TextView mainError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainError = findViewById(R.id.main_error);

        phone =  findViewById(R.id.email);

        password = findViewById(R.id.password);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button signInButton = findViewById(R.id.email_sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);

        String[] data = QueryPreferences.getPhoneAndPassword(this);
        if (data != null) {
            phone.setText(data[0]);
            password.setText(data[1]);
        }
    }

    private void attemptLogin() {

        phone.setError(null);
        password.setError(null);
        setMainError(0);

        String phone = this.phone.getText().toString();
        String password = this.password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            this.password.setError(getString(R.string.error_invalid_password));
            focusView = this.password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            this.phone.setError(getString(R.string.error_field_required));
            focusView = this.phone;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            this.phone.setError(getString(R.string.error_invalid_phone));
            focusView = this.phone;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            /*mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*/
            singIn(phone, password);
        }
    }

    private boolean isPhoneValid(String phone) {
        return phone.length() < 20;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 1;
    }

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        loginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }

    private void singIn(final String phone, final String password) {
        /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();*/
        if (checkNetworkState()) {
            ApiFactory.getService()
                    .signIn(phone, password)
                    .compose(new HelpTransformer<Auth>())
                    .subscribe(
                            new Consumer<Auth>() {
                                @Override
                                public void accept(Auth auth) throws Exception {
                                    System.out.println(auth);
                                    if (auth.isError()) {
                                        setMainError(R.string.error_phone_or_password);
                                        showProgress(false);
                                    } else {
                                        QueryPreferences.setPhoneAndPassword(LoginActivity.this, phone, password);
                                        QueryPreferences.setToken(LoginActivity.this, auth.getToken());
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        LoginActivity.this.finish();
                                    }
                                }
                            },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    throwable.printStackTrace();
                                    setMainError(R.string.error_noname);
                                }
                            });
        }
    }

    private void setMainError(int error) {
        if (error == 0) {
            mainError.setVisibility(View.GONE);
        } else {
            mainError.setText(error);
            mainError.setVisibility(View.VISIBLE);
        }
    }
}


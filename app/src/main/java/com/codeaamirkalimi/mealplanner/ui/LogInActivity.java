package com.codeaamirkalimi.mealplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.datamodel.GoogleAccountData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogInActivity extends AppCompatActivity implements
        LogInFragment.OnLogInFragmentInteractionListener,
        SignUpFragment.OnSignUpFragmentInteractionListener,
        ForgotPasswordFragment.OnForgotPasswordInteractionListener {

    private static final String FRAGMENT_NAME_SAVE_INSTANCE_KEY = "fragment-name";

    private static final String GOOGLE_ACCOUNT_DATA_KEY = "google-account-data";

    private static final String TAG = LogInActivity.class.getSimpleName();

    @BindView(R.id.fl_log_in_screen_fragment_container)
    FrameLayout flLogInScreenFragmentContainer;

    private String sFragmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Set the view now
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        // Handle screen rotation
        if (savedInstanceState == null) {
            sFragmentName = getString(R.string.login_screen_screen_title);
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.fl_log_in_screen_fragment_container,
                    new LogInFragment(), sFragmentName).commit();
        } else {
            sFragmentName = savedInstanceState.getString(FRAGMENT_NAME_SAVE_INSTANCE_KEY);
            getSupportFragmentManager().findFragmentByTag(sFragmentName);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FRAGMENT_NAME_SAVE_INSTANCE_KEY, sFragmentName);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSuccessfulMailLogIn() {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onFailMailLogIn() {
        Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onOpenSignUpFragment() {
        sFragmentName = getString(R.string.sign_up_screen_title);
        getSupportFragmentManager().
                beginTransaction().replace(R.id.fl_log_in_screen_fragment_container,
                new SignUpFragment(), sFragmentName).commit();
    }

    @Override
    public void onOpenForgotPasswordFragment() {
        sFragmentName = getString(R.string.forgot_password_screen_title);
        getSupportFragmentManager().
                beginTransaction().replace(R.id.fl_log_in_screen_fragment_container,
                new ForgotPasswordFragment(), sFragmentName).commit();
    }

    @Override
    public void onSuccessfulMailSignUp() {
        // TODO Send email verification and implement identity verification
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onFailMailSignUp(String msg) {
        Toast.makeText(this, getString(R.string.sign_up_screen_authentication_failed, msg)
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOpenLoginFragment() {
        sFragmentName = getString(R.string.login_screen_screen_title);
        getSupportFragmentManager().
                beginTransaction().replace(R.id.fl_log_in_screen_fragment_container,
                new LogInFragment(), sFragmentName).commit();
    }

    @Override
    public void onSucessfulResetPasswordMailSent() {
        Toast.makeText(this, getString(R.string.forgot_password_reset_mail_sent)
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailResetPasswordMailSent() {
        Toast.makeText(this, getString(R.string.forgot_password_reset_mail_not_sent)
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackButtonPressed() {
        sFragmentName = getString(R.string.login_screen_screen_title);
        getSupportFragmentManager().
                beginTransaction().replace(R.id.fl_log_in_screen_fragment_container,
                new LogInFragment(), sFragmentName).commit();
    }

    @Override
    public void onSuccessfulGMailLogIn(GoogleAccountData data) {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(GOOGLE_ACCOUNT_DATA_KEY, data);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onFailedGMailLogIn() {
        Toast.makeText(this, getString(R.string.login_screen_failed_google_sign_in)
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

}

package com.codeaamirkalimi.mealplanner.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.codeaamirkalimi.mealplanner.R;
import com.codeaamirkalimi.mealplanner.application.MyMealPlanner;
import com.codeaamirkalimi.mealplanner.datamodel.GoogleAccountData;
import com.codeaamirkalimi.mealplanner.global.MyMealPlannerGlobals;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLogInFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LogInFragment extends Fragment {

    public static final int RC_GMAIL_SIGN_IN = 100;

    private static final String TAG = LogInFragment.class.getSimpleName();

    @BindView(R.id.et_login_screen_email)
    EditText etLoginScreenEmail;
    @BindView(R.id.et_login_screen_password)
    EditText etLoginScreenPassword;
    @BindView(R.id.bt_login_screen_mail_login)
    Button btLoginScreenMailLogin;
    @BindView(R.id.bt_login_screen_forgot_pass)
    Button btLoginScreenForgotPass;
    @BindView(R.id.bt_login_screen_sign_up)
    Button btLoginScreenSignUp;
    @BindView(R.id.bt_login_screen_google_login)
    SignInButton btLoginScreenGoogleLogin;
    @BindView(R.id.pb_login_screen_progress)
    ProgressBar pbLoginScreenProgress;

    private Activity mContext;
    Unbinder unbinder;
    private OnLogInFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences mSharedPreferences;

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mContext = getActivity();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_google_sign_in_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);
        ((MyMealPlanner) mContext.getApplication()).setmGoogleSignInClient(mGoogleSignInClient);

        // Get SharedPreferences
        mSharedPreferences = mContext.getSharedPreferences(
                getString(R.string.app_shared_preferences), Context.MODE_PRIVATE);

        // Request default focus
        etLoginScreenEmail.requestFocus();

        // Return rootView
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLogInFragmentInteractionListener) {
            mListener = (OnLogInFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLogInFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        String loginType = mSharedPreferences.getString(getString(R.string.login_type), MyMealPlannerGlobals.NOT_LOGGED);
        switch(loginType) {
            case MyMealPlannerGlobals.MAIL_LOGGED:
                 // Auto log-in
                 if (mAuth.getCurrentUser() != null) {
                     if(mListener != null) {
                         // TODO Personal data for mail log-in?
                         mListener.onSuccessfulMailLogIn();
                     }
                 }
                break;
            case MyMealPlannerGlobals.GOOGLE_LOGGED:
                // Check for existing Google Sign In account, if the user is already signed in
                // the GoogleSignInAccount will be non-null.
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mContext);
                if(account != null) {
                    final GoogleAccountData googleAccountData = retrieveGoogleAccountData(account);
                    if(mListener != null) {
                        mListener.onSuccessfulGMailLogIn(googleAccountData);
                    }
                }
                break;
            default:
            case MyMealPlannerGlobals.NOT_LOGGED:
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.bt_login_screen_mail_login, R.id.bt_login_screen_forgot_pass,
            R.id.bt_login_screen_sign_up, R.id.bt_login_screen_google_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login_screen_mail_login:
                handleMailLogIn();
                break;
            case R.id.bt_login_screen_forgot_pass:
                if (mListener != null) {
                    mListener.onOpenForgotPasswordFragment();
                }
                break;
            case R.id.bt_login_screen_sign_up:
                if (mListener != null) {
                    mListener.onOpenSignUpFragment();
                }
                break;
            case R.id.bt_login_screen_google_login:
                signInWithGoogle();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLogInFragmentInteractionListener {
        void onSuccessfulMailLogIn();
        void onFailMailLogIn();
        void onOpenSignUpFragment();
        void onOpenForgotPasswordFragment();
        void onSuccessfulGMailLogIn(GoogleAccountData data);
        void onFailedGMailLogIn();
    }

    private void handleMailLogIn() {

        String email = etLoginScreenEmail.getText().toString();
        final String password = etLoginScreenPassword.getText().toString();

        // Remove previous displayed errors
        if(etLoginScreenEmail.getError() != null) {
            etLoginScreenEmail.setError(null);
        }
        if(etLoginScreenPassword.getError() != null) {
            etLoginScreenPassword.setError(null);
        }

        // Check consistency of email and password
        if (TextUtils.isEmpty(email)) {
            etLoginScreenEmail.setError(getString(R.string.enter_valid_email));
            etLoginScreenEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etLoginScreenPassword.setError(getString(R.string.minimum_password));
            etLoginScreenPassword.requestFocus();
            return;
        }

        // Set progress bar visible
        pbLoginScreenProgress.setVisibility(View.VISIBLE);

        // Authenticate user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) mContext,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pbLoginScreenProgress.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                etLoginScreenPassword.setError(getString(R.string.minimum_password));
                                etLoginScreenPassword.requestFocus();
                            } else {
                                if (mListener != null) {
                                    mListener.onFailMailLogIn();
                                }
                            }
                        } else {
                            if (mListener != null) {
                                mListener.onSuccessfulMailLogIn();
                                // Save Google Sign-in flag to shared preferences
                                // TODO Enum / Hash-table implementation?
                                SharedPreferences.Editor editor = mSharedPreferences.edit();
                                editor.putString(getString(R.string.login_type), MyMealPlannerGlobals.MAIL_LOGGED);
                                editor.commit();
                            }
                        }
                    }
                });
    }

    private void signInWithGoogle() {
        pbLoginScreenProgress.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GMAIL_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_GMAIL_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, getString(R.string.login_screen_failed_google_sign_in), e);
                if(mListener != null) {
                    mListener.onFailedGMailLogIn();
                }
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            final GoogleAccountData googleAccountData = retrieveGoogleAccountData(account);
            // Signed in successfully, show authenticated UI.
            if(mListener != null) {
                mListener.onSuccessfulGMailLogIn(googleAccountData);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            if(mListener != null) {
                mListener.onFailedGMailLogIn();
            }
        }
    }

    private GoogleAccountData retrieveGoogleAccountData(GoogleSignInAccount account) {
        // Retrieve Google Account Data
        String id = account.getId();
        String displayName = account.getDisplayName();
        String email = account.getEmail();
        String familyName = account.getFamilyName();
        String givenName = account.getGivenName();
        Uri photoUrl = account.getPhotoUrl();

        return new GoogleAccountData(id, displayName, email, familyName, givenName, photoUrl);
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pbLoginScreenProgress.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    final GoogleAccountData googleAccountData = retrieveGoogleAccountData(acct);
                    if(mListener != null) {
                        mListener.onSuccessfulGMailLogIn(googleAccountData);
                        // Save Google Sign-in flag to shared preferences
                        // TODO Enum / Hash-table implementation?
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString(getString(R.string.login_type), MyMealPlannerGlobals.GOOGLE_LOGGED);
                        editor.commit();
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    if(mListener != null) {
                        mListener.onFailedGMailLogIn();
                    }
                }
            }
        });
    }

}

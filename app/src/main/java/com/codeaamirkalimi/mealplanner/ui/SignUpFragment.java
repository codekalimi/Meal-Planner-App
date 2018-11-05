package com.codeaamirkalimi.mealplanner.ui;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.codeaamirkalimi.mealplanner.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSignUpFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SignUpFragment extends Fragment {

    private static final String TAG = SignUpFragment.class.getSimpleName();

    @BindView(R.id.et_sign_up_screen_email)
    EditText etSignUpScreenEmail;
    @BindView(R.id.et_sign_up_screen_password)
    EditText etSignUpScreenPassword;
    @BindView(R.id.bt_sign_up_screen_mail_login)
    Button btSignUpScreenMailLogin;
    @BindView(R.id.bt_sign_up_screen_forgot_pass)
    Button btSignUpScreenForgotPass;
    @BindView(R.id.bt_sign_up_screen_login)
    Button btSignUpScreenLogin;
    @BindView(R.id.pb_login_screen_progress)
    ProgressBar pbLoginScreenProgress;

    Unbinder unbinder;
    private FirebaseAuth auth;
    private Activity mContext;


    private OnSignUpFragmentInteractionListener mListener;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mContext = getActivity();

        // Request default focus
        etSignUpScreenEmail.requestFocus();

        // Return rootView
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignUpFragmentInteractionListener) {
            mListener = (OnSignUpFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSignUpFragmentInteractionListener");
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

    @OnClick({R.id.bt_sign_up_screen_mail_login, R.id.bt_sign_up_screen_forgot_pass,
            R.id.bt_sign_up_screen_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sign_up_screen_mail_login:
                handleMailSignUp();
                break;
            case R.id.bt_sign_up_screen_forgot_pass:
                if (mListener != null) {
                    mListener.onOpenForgotPasswordFragment();
                }
                break;
            case R.id.bt_sign_up_screen_login:
                if (mListener != null) {
                    mListener.onOpenLoginFragment();
                }
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
    public interface OnSignUpFragmentInteractionListener {
        void onSuccessfulMailSignUp();
        void onFailMailSignUp(String msg);
        void onOpenLoginFragment();
        void onOpenForgotPasswordFragment();
    }

    private void handleMailSignUp() {

        String email = etSignUpScreenEmail.getText().toString().trim();
        String password = etSignUpScreenPassword.getText().toString().trim();

        // Remove previous displayed errors
        if(etSignUpScreenEmail.getError() != null) {
            etSignUpScreenEmail.setError(null);
        }
        if(etSignUpScreenPassword.getError() != null) {
            etSignUpScreenPassword.setError(null);
        }

        // Check consistency of email and password
        if (TextUtils.isEmpty(email)) {
            etSignUpScreenEmail.setError(getString(R.string.enter_valid_email));
            etSignUpScreenEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etSignUpScreenPassword.setError(getString(R.string.minimum_password));
            etSignUpScreenPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etSignUpScreenPassword.setError(getString(R.string.minimum_password));
            etSignUpScreenPassword.requestFocus();
            return;
        }

        // Set progress bar visible
        pbLoginScreenProgress.setVisibility(View.VISIBLE);

        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pbLoginScreenProgress.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            if (mListener != null) {
                                mListener.onFailMailSignUp(task.getException().toString());
                            }
                        } else {
                            if (mListener != null) {
                                mListener.onSuccessfulMailSignUp();
                            }
                        }
                    }
                });

    }

}

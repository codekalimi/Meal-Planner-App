package com.codeaamirkalimi.mealplanner.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.codeaamirkalimi.mealplanner.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnForgotPasswordInteractionListener} interface
 * to handle interaction events.
 */
public class ForgotPasswordFragment extends Fragment {

    @BindView(R.id.et_forgot_password_screen_email)
    EditText etForgotPasswordScreenEmail;
    @BindView(R.id.bt_forgot_password_screen_reset_password)
    Button btForgotPasswordScreenResetPassword;
    @BindView(R.id.bt_forgot_password_screen_back)
    Button btForgotPasswordScreenBack;
    @BindView(R.id.pb_forgot_password_screen_progress)
    ProgressBar pbForgotPasswordScreenProgress;

    Unbinder unbinder;
    private OnForgotPasswordInteractionListener mListener;
    private FirebaseAuth auth;
    private Activity mContext;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mContext = getActivity();

        // Request default focus
        etForgotPasswordScreenEmail.requestFocus();

        // Return rootView;
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnForgotPasswordInteractionListener) {
            mListener = (OnForgotPasswordInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnForgotPasswordInteractionListener");
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

    @OnClick({R.id.bt_forgot_password_screen_reset_password, R.id.bt_forgot_password_screen_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_forgot_password_screen_reset_password:
                handleResetPassword();
                break;
            case R.id.bt_forgot_password_screen_back:
                if(mListener != null) {
                    mListener.onBackButtonPressed();
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
    public interface OnForgotPasswordInteractionListener {
        void onSucessfulResetPasswordMailSent();
        void onFailResetPasswordMailSent();
        void onBackButtonPressed();
    }

    private void handleResetPassword() {

        String email = etForgotPasswordScreenEmail.getText().toString().trim();

        // Check consistency of mail
        if (TextUtils.isEmpty(email)) {
            etForgotPasswordScreenEmail.setError(getString(R.string.forgot_password_invalid_email));
            etForgotPasswordScreenEmail.requestFocus();
            return;
        }

        pbForgotPasswordScreenProgress.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if(mListener != null) {
                                mListener.onSucessfulResetPasswordMailSent();
                            }
                        } else {
                            if(mListener != null) {
                                mListener.onFailResetPasswordMailSent();
                            }
                        }
                        pbForgotPasswordScreenProgress.setVisibility(View.GONE);
                    }
                });
    }
}

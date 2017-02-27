package com.agenthun.eseallite.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.agenthun.eseallite.R;
import com.agenthun.eseallite.activity.ScanNfcDeviceActivity;
import com.agenthun.eseallite.bean.User;
import com.agenthun.eseallite.connectivity.manager.RetrofitManager;
import com.agenthun.eseallite.connectivity.service.PathType;
import com.agenthun.eseallite.utils.NetUtil;
import com.agenthun.eseallite.utils.PreferencesHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * @project ESeal
 * @authors agenthun
 * @date 2017/2/15 15:40.
 */

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    private static final String ARG_SAVE = "SAVE";
    private static final boolean ARG_SAVE_DEFAULT = false;

    @Bind(R.id.login_name)
    AppCompatEditText loginName;
    @Bind(R.id.login_password)
    AppCompatEditText loginPassword;

    private AppCompatDialog mProgressDialog;

    private PreferencesHelper.User mUser = null;

    private boolean save;

    public static LoginFragment newInstance(boolean save) {

        Bundle args = new Bundle();
        args.putBoolean(ARG_SAVE, save);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        assureUserInit();
        checkIsInSaveMode();

        if (mUser == null || save) {
            initContents();
        } else {
            loginName.setText(mUser.getUsername());
            loginPassword.setText(mUser.getPassword());
            attemptLogin(mUser);
        }

        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.sign_in_button)
    public void onSignInBtnClick() {
        if (isInputDataValid()) {
            saveUser(getActivity());
            attemptLogin(mUser);
        } else {
            Snackbar snackbar = Snackbar.make(loginName, getString(R.string.error_invalid_user), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null);
            ((TextView) (snackbar.getView().findViewById(R.id.snackbar_text))).setTextColor(ContextCompat.getColor(getContext(), R.color.blue_grey_100));
            snackbar.show();
        }
    }

    private void assureUserInit() {
        if (mUser == null) {
            mUser = PreferencesHelper.getUser(getContext());
        }
    }

    private void checkIsInSaveMode() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            save = ARG_SAVE_DEFAULT;
        } else {
            save = arguments.getBoolean(ARG_SAVE, ARG_SAVE_DEFAULT);
        }
    }

    private void initContents() {
        assureUserInit();
        if (mUser != null) {
            loginName.setText(mUser.getUsername());
            loginName.setSelection(mUser.getUsername().length());
            loginName.setFocusable(true);
            loginPassword.setText(mUser.getPassword());
            loginPassword.setFocusable(true);
        }
    }

    private void attemptLogin(PreferencesHelper.User user) {
        if (NetUtil.isConnected(getContext())) { //已连接网络
            String name = user.getUsername();
            String password = user.getPassword();

            getProgressDialog().show();

            RetrofitManager.builder(PathType.WEB_SERVICE_V2_TEST).getTokenObservable(name, password)
                    .subscribe(new Subscriber<User>() {
                        @Override
                        public void onCompleted() {
                            getProgressDialog().cancel();
                        }

                        @Override
                        public void onError(Throwable e) {
                            getProgressDialog().cancel();
                            Toast.makeText(getContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(User user) {
                            if (user == null) return;
                            if (user.getEFFECTIVETOKEN() != 1) return;

                            String token = user.getTOKEN();
                            Log.d(TAG, "token: " + token);
                            if (token != null) {
                                PreferencesHelper.writeTokenToPreferences(getContext(), token);

                                ScanNfcDeviceActivity.start(getContext());
                                ActivityCompat.finishAfterTransition(getActivity());
                            } else {
                                Toast.makeText(getContext(), R.string.error_invalid_null, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Snackbar snackbar = Snackbar.make(loginName, getString(R.string.error_network_not_open), Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_hint_open_network), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                        }
                    });
            ((TextView) (snackbar.getView().findViewById(R.id.snackbar_text))).setTextColor(ContextCompat.getColor(getContext(), R.color.blue_grey_100));
            snackbar.show();
        }
    }

    private void saveUser(Activity activity) {
        PreferencesHelper.clearUser(getContext());
        mUser = new PreferencesHelper.User(loginName.getText().toString(), loginPassword.getText().toString());
        PreferencesHelper.writeUserInfoToPreferences(activity, mUser);
    }

    private boolean isInputDataValid() {
        return PreferencesHelper.isInputDataValid(loginName.getText(), loginPassword.getText());
    }

    private AppCompatDialog getProgressDialog() {
        if (mProgressDialog != null) {
            return mProgressDialog;
        }
        mProgressDialog = new AppCompatDialog(getContext(), AppCompatDelegate.MODE_NIGHT_AUTO);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setContentView(R.layout.dialog_logging_in);
        mProgressDialog.setTitle(getString(R.string.action_login));
        mProgressDialog.setCancelable(false);
        return mProgressDialog;
    }
}

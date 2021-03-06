package com.kidsdynamic.swing.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kidsdynamic.commonlib.utils.ObjectUtils;
import com.kidsdynamic.commonlib.utils.SoftKeyBoardUtil;
import com.kidsdynamic.data.net.ApiGen;
import com.kidsdynamic.data.net.event.EventApi;
import com.kidsdynamic.data.net.event.model.EventWithTodo;
import com.kidsdynamic.data.net.user.UserApiNeedToken;
import com.kidsdynamic.data.net.user.UserApiNoNeedToken;
import com.kidsdynamic.data.net.user.model.LoginEntity;
import com.kidsdynamic.data.net.user.model.LoginSuccessRep;
import com.kidsdynamic.data.net.user.model.UserProfileRep;
import com.kidsdynamic.data.utils.LogUtil2;
import com.kidsdynamic.swing.BaseFragment;
import com.kidsdynamic.swing.BuildConfig;
import com.kidsdynamic.swing.R;
import com.kidsdynamic.swing.domain.EventManager;
import com.kidsdynamic.swing.domain.LoginManager;
import com.kidsdynamic.swing.net.BaseRetrofitCallback;
import com.kidsdynamic.swing.utils.SwingFontsCache;
import com.kidsdynamic.swing.view.ViewUtils;
import com.yy.base.utils.Functions;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * SignupLoginFragment
 * <p>
 * Created by Stefan on 2017/10/23.
 */

public class SignupLoginFragment extends BaseFragment {

    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.signup_login_hint_text)
    TextView tvHint;

    @BindView(R.id.signup_login_reset_pwd)
    TextView btn_resetPsw;

    public static SignupLoginFragment newInstance() {
        Bundle args = new Bundle();
        SignupLoginFragment fragment = new SignupLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_signup_login, container, false);
        ButterKnife.bind(this, layout);

        initEditTextStyle();
        return layout;
    }

    private void initEditTextStyle() {
        et_email.setTypeface(SwingFontsCache.getNormalType(getContext()));
        et_password.setTypeface(SwingFontsCache.getNormalType(getContext()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(BuildConfig.DEBUG){
            et_email.setText("123@qq.com");
            et_password.setText("123456");
        }
/*
        et_email.setText("lwz3@swing.com");
        et_password.setText("1");
*/
    }

    @OnClick(R.id.ib_back)
    public void back() {
        SignupActivity signupActivity = (SignupActivity) getActivity();
        signupActivity.setFragment(SignupStartFragment.newInstance());
    }

    @OnClick(R.id.signup_login_login)
    public void login() {
        //查询账户是否注册；如果未注册则展示注册界面（输入last name, first name;avatar）;注册成功后，再次执行
        //登录流程；登录成功后，开始同步数据（同步那些？）
        //如果是已经注册账户，则尝试登陆，如果登陆失败，则显示找回密码按钮

        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (!Functions.isValidEmail(email)) {
            showErrInfo(R.string.error_api_unknown);
            return;
        }
        if (ObjectUtils.isStringEmpty(password)) {
            showErrInfo(R.string.error_api_unknown);
            return;
        }

        SoftKeyBoardUtil.hideSoftKeyboard(getActivity());

        // TODO: 2017/11/4 test
        //执行登录业务
        exeLoginFlow(email, password);
//        showRegisterUI(email, password);
    }

    private void showErrInfo(int msgId) {
        String error = getString(msgId);
        tvHint.setText(String.format("*%s", error));
        tvHint.setVisibility(View.VISIBLE);
    }

    void exeLoginFlow(final String email, final String psw) {
        //show waiting dialog
        showLoadingDialog(R.string.signup_login_wait);

        final LoginEntity loginEntity = new LoginEntity();
        loginEntity.setEmail(email);
        loginEntity.setPassword(psw);

        //查询账户是否注册；如果未注册则展示注册界面（输入last name, first name;avatar）;注册成功后，再次执行
        //登录流程；登录成功后，开始同步数据（同步那些？）
        final UserApiNoNeedToken userApi = ApiGen.getInstance(getActivity().getApplicationContext()).
                generateApi(UserApiNoNeedToken.class, false);

        userApi.checkEmailAvailableToRegister(email).enqueue(new BaseRetrofitCallback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                LogUtil2.getUtils().d("check mail onResponse ");

                if (response.code() == 200) {
                    SignupLoginFragment.this.finishLoadingDialog();
                    //邮箱未注册，则展示注册界面
                    showRegisterUI(email, psw);

                } else if (response.code() == 409) {
                    //邮箱已经注册，则执行登录流程
                    loginByEmail(userApi, loginEntity);
                }

                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                super.onFailure(call, t);

                //dismiss waiting dialog,show error; terminate exe
                SignupLoginFragment.this.finishLoadingDialog();

                t.printStackTrace();
                LogUtil2.getUtils().d("check mail fail, ");

                showErrInfo(R.string.signup_profile_login_failed);
            }
        });

    }

    private void showRegisterUI(String email, String pwd) {
        SignupActivity signupActivity = (SignupActivity) getActivity();
        signupActivity.setFragment(SignupProfileFragment.newInstance(email, pwd));
    }

    //开始执行登录接口，如果登录成功，则同步数据
    private void loginByEmail(UserApiNoNeedToken userApi, LoginEntity loginEntity) {
        userApi.login(loginEntity).enqueue(new BaseRetrofitCallback<LoginSuccessRep>() {
            @Override
            public void onResponse(Call<LoginSuccessRep> call, Response<LoginSuccessRep> response) {

                if (response.code() == 200) {
                    LogUtil2.getUtils().d("login success");
                    LogUtil2.getUtils().d(response.body().getAccess_token());
                    //缓存token
                    new LoginManager().cacheToken(response.body().getAccess_token());

                    //同步数据
                    syncData();

                } else {
                    SignupLoginFragment.this.finishLoadingDialog();
                    LogUtil2.getUtils().d("login error, code: " + response.code());
                    btn_resetPsw.setVisibility(View.VISIBLE);

                    showErrInfo(R.string.signup_profile_login_failed);
                }

                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<LoginSuccessRep> call, Throwable t) {
                super.onFailure(call, t);
                //fail, dismiss dialog, show error info
                SignupLoginFragment.this.finishLoadingDialog();
                showErrInfo(R.string.signup_profile_login_failed);
            }
        });
    }

    private void syncData() {
        //2017/10/17 同步账户数据 成功后关闭等待对话框，跳转到主界面；失败提醒
        Log.w("login", "login ok, start sync data");

        //登陆成功后，需要获取两个业务数据：
        //“/v1/user/retrieveUserProfile”
        //“/v1/event/retrieveAllEventsWithTodo”

        final UserApiNeedToken userApiNeedToken = ApiGen.getInstance(getActivity().getApplicationContext()).
                generateApi(UserApiNeedToken.class, true);

        userApiNeedToken.retrieveUserProfile().enqueue(new BaseRetrofitCallback<UserProfileRep>() {
            @Override
            public void onResponse(Call<UserProfileRep> call, Response<UserProfileRep> response) {
                LogUtil2.getUtils().d("retrieveUserProfile onResponse");

                if (response.code() == 200) {//获取到用户信息
                    LogUtil2.getUtils().d("onResponse: " + response.body());

                    //先清除本地数据，然后再保存
                    new LoginManager().saveLoginData(getContext(), response.body());
                    //继续获取信息: 获取event信息
                    getEventInfos(response.body().getUser());

                } else {
                    finishLoadingDialog();
                    LogUtil2.getUtils().d("onResponse not 200");

                    showErrInfo(R.string.signup_profile_login_failed);
                }

                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<UserProfileRep> call, Throwable t) {
                Log.d("syncData", "retrieveUserProfile error, ");
                super.onFailure(call, t);

                showErrInfo(R.string.signup_profile_login_failed);
                finishLoadingDialog();
            }
        });
    }


    private void getEventInfos(final UserProfileRep.UserEntity userEntity) {
        LogUtil2.getUtils().d("getEventInfo");
        final EventApi eventApi = ApiGen.getInstance(getActivity().getApplicationContext()).
                generateApi(EventApi.class, true);

        eventApi.retrieveAllEventsWithTodo().enqueue(new BaseRetrofitCallback<List<EventWithTodo>>() {
            @Override
            public void onResponse(Call<List<EventWithTodo>> call, Response<List<EventWithTodo>> response) {

                if (response.code() == 200) {//获取成功
                    LogUtil2.getUtils().d("onResponse: " + response.body());

                    Log.w("login", " sync data ok, show next UI");
                    new EventManager().saveEventForLogin(getContext(), response.body());

                    finishLoadingDialog();
                    loginFlowOK(userEntity);
                } else {
                    LogUtil2.getUtils().d("login error, code: " + response.code());

                    finishLoadingDialog();
                    showErrInfo(R.string.signup_profile_login_failed);
                }

                super.onResponse(call, response);

            }

            @Override
            public void onFailure(Call<List<EventWithTodo>> call, Throwable t) {
                Log.d("syncData", "retrieveAllEventsWithTodo error, ");
                super.onFailure(call, t);

                finishLoadingDialog();
                showErrInfo(R.string.signup_profile_login_failed);
            }
        });
    }

    private void loginFlowOK(UserProfileRep.UserEntity userEntity) {
        getActivity().finish();
        startActivity(new Intent(getActivity(), MainFrameActivity.class));

        //记录状态
        new LoginManager().cacheLoginOK(getContext(),userEntity);
    }

    @OnClick(R.id.signup_login_reset_pwd)
    public void resetPsw() {
        final String email = et_email.getText().toString();

        if (!Functions.isValidEmail(email)) {
            showErrInfo(R.string.error_api_unknown);
            return;
        }

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.profile_option_password)
                .setMessage(R.string.profile_reset_password_confirmation)
                .setNegativeButton(R.string.profile_request_to_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(R.string.watch_have_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //调用接口重置
                        dialogInterface.dismiss();
                        exeResetPswFlow(email);
                    }
                }).show();

    }

    private void exeResetPswFlow(@NonNull String email) {

        showLoadingDialog(R.string.signup_login_wait);

        final UserApiNeedToken userApiNeedToken = ApiGen.getInstance(getActivity().getApplicationContext()).
                generateApi(UserApiNeedToken.class, false);

        HashMap<String, String> resetPswParam = new HashMap<>(1);
        resetPswParam.put("email", email);
        userApiNeedToken.sendResetPasswordEmail(resetPswParam).enqueue(new BaseRetrofitCallback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                finishLoadingDialog();

                int code = response.code();
                if (code == 200) {
                    ViewUtils.showMsgDialog(getActivity(),
                            getString(R.string.profile_reset_password_note, et_email.getText().toString()));
                } else {
                    Toast.makeText(getContext(), R.string.error_api_unknown + " " + code, Toast.LENGTH_SHORT).show();
                }

                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                super.onFailure(call, t);
                finishLoadingDialog();

                Toast.makeText(getContext(), R.string.net_err, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

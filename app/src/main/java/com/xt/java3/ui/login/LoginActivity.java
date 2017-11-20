package com.xt.java3.ui.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xt.java3.App;
import com.xt.java3.R;
import com.xt.java3.modules.User;
import com.xt.java3.base.BaseActivity;
import com.xt.java3.base.BaseError;
import com.xt.java3.modules.request.LoginBody;
import com.xt.java3.service.WebService;
import com.xt.java3.ui.main.MainActivity;
import com.xt.java3.ui.register.RegisterActivity;
import com.xt.java3.util.PreferenceMgr;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    LoginContract.Presenter mPresenter;


    @BindView(R.id.account)
    EditText account;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.login)
    AppCompatTextView login;

    @BindView(R.id.forget)
    Button forget;

    @BindView(R.id.remenber)
    CheckBox checkBox;

    PreferenceMgr mgr = new PreferenceMgr("remenber");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPresenter= new LoginPresenter(this);
        mPresenter.judgeState();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        account.setText("111");
        password.setText("123456");

        account.setText(mgr.get("id",""));
        password.setText(mgr.get("password",""));
        if(!account.getText().toString().equals("")){
            checkBox.setChecked(true);
        }

    }

    @OnClick(R.id.login)
    public void login(View v){
        String account = this.account.getText().toString();
        String password = this.password.getText().toString();

        if(checkBox.isChecked()) {
            mgr.save("id", account);
            mgr.save("password", password);
        }

        if( RegexUtils.isMatch("^[0-9]*$",account) && !password.equals("") ) {

            LoginBody request = new LoginBody();
            request.setPassword(password);
            request.setId(Integer.parseInt(account));
            mPresenter.login(request);

        }else{
            ToastUtils.showShort("请重新输入");
        }

    }

    @OnClick(R.id.forget)
    public void forget(View view ){
        startActivity(new Intent(this, RegisterActivity.class));
    }


    Dialog progress;
    @Override
    public void onLoginStart() {
        progress = ProgressDialog.show(this,null,"登录中");
    }

    @Override
    public void onLoginSuccess(User user) {

        App.mUser = user;

        Log.e("LoginActivity","success");

        Intent activityIntent = new Intent(this, MainActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activityIntent);

        new PreferenceMgr("lastid").save("id",user.getId());

        Intent intent = new Intent(this, WebService.class);
        intent.putExtra("id",user.getId());
        startService(intent);

    }

    @Override
    public void onLoginError(Throwable e) {
        if(e instanceof BaseError){
            int code = ((BaseError)e).getStatus();
            switch(code){
                case -1 : ToastUtils.showShort("账号不存在");break;
                case -2:ToastUtils.showShort("密码错误");break;
                case -3:
                    if(User.firstLogin) {
                        ToastUtils.showShort("登录过时");
                        User.firstLogin = false;
                    }

            }

        }else{
            Log.e("LoginActivity",e.getMessage());
        }
        if(progress!= null)
        progress.dismiss();
    }

    @Override
    public void onStateCallback(User user) {
       onLoginSuccess(user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progress!=null)
            progress.dismiss();
    }
}

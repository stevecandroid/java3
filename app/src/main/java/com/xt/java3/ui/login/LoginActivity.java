package com.xt.java3.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.xt.java3.App;
import com.xt.java3.R;
import com.xt.java3.User;
import com.xt.java3.base.BaseError;
import com.xt.java3.modules.request.LoginBody;
import com.xt.java3.modules.response.LoginResponse;
import com.xt.java3.service.WebService;
import com.xt.java3.ui.main.MainActivity;
import com.xt.java3.ui.register.RegisterActivity;
import com.xt.java3.util.PreferenceMgr;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    LoginContract.Presenter mPresenter;


    @BindView(R.id.account)
    EditText account;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.login)
    AppCompatTextView login;

    @BindView(R.id.forget)
    Button forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mPresenter= new LoginPresenter(this);
        mPresenter.judgeState();

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        account.setText("111");
        password.setText("123456");




    }

    @OnClick(R.id.login)
    public void login(View v){
        String account = this.account.getText().toString();
        String password = this.password.getText().toString();

        if( !account.equals("") && !password.equals("") ) {
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


    @Override
    public void onLoginSuccess(User user) {

        App.mUser = user;
        new PreferenceMgr("lastid").save("id",user.getId());
        Intent intent = new Intent(this, WebService.class);
        intent.putExtra("id",user.getId());
        startService(intent);
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onLoginError(Throwable e) {
        if(e instanceof BaseError){
            int code = ((BaseError)e).getStatus();
            if(code == -1)ToastUtils.showShort("账号不存在");
            if(code == -2)ToastUtils.showShort("密码错误");
        }else{
            Log.e("LoginActivity",e.getMessage());
        }
    }

    @Override
    public void onStateCallback(LoginResponse response) {
        Log.e("LoginActivity",response.getStatus()+"STAUTS");
        if(response.getStatus() == 0  ){
            onLoginSuccess(response.getUser());

        }else{
            onLoginError(new RuntimeException("登录过期"));
        }
    }
}

package com.xt.java3.ui.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.xt.java3.R;
import com.xt.java3.base.BaseActivity;
import com.xt.java3.modules.request.RegisterBody;
import com.xt.java3.ui.login.LoginActivity;
import com.xt.java3.util.dialog.DialogHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements RegisterContract.View{

    private RegisterContract.Presetner mPresenter;

    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.register)
    AppCompatTextView register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mPresenter = new RegisterPresenter(this);

    }

    @OnClick(R.id.register)
    public void register(View v){
        String account = this.account.getText().toString();
        String password = this.password.getText().toString();

        if( !account.equals("") && !password.equals("") ) {
            RegisterBody request = new RegisterBody(account, password);
            mPresenter.register(request);
        }else{
            ToastUtils.showShort("请重新输入");
        }

    }

    @Override
    public void onRegisterSuccess(int id ) {
        DialogHelper.showEnsureDialog(this, "请记住你的ID：" + id + "登录请使用该id", new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    @Override
    public void onRegisterError(Throwable e) {
        Log.e("RegisterActivity","register fail " + e.toString());
    }
}

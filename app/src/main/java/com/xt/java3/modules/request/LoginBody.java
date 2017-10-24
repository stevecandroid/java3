package com.xt.java3.modules.request;

/**
 * Created by steve on 17-10-22.
 */
public  class LoginBody {

    private String email;
    private int id ;
    private String password ;

    public LoginBody() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passworld) {
        this.password = passworld;
    }
}

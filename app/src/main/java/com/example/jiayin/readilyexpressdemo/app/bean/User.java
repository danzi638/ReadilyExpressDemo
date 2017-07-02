package com.example.jiayin.readilyexpressdemo.app.bean;

import java.io.Serializable;

/**
 * Created by jiayin on 2017/7/2.
 */



public class User implements Serializable {
    private int imag1;
    private int imag2;
    private String email;
    private String  cellphone;
    private String username;



    public User(String username,String cellphone,String email,int imag1,int imag2)
    {
        this.username=username;
        this.email=email;
        this.imag1=imag1;
        this.cellphone=cellphone;
        this.imag2=imag2;

    }



    public int getImag1() {
        return imag1;
    }



    public void setImag1(int imag1) {
        this.imag1 = imag1;
    }



    public int getImag2() {
        return imag2;
    }



    public void setImag2(int imag2) {
        this.imag2 = imag2;
    }



    public String getEmail() {
        return email;
    }



    public void setEmail(String email) {
        this.email = email;
    }



    public String getCellphone() {
        return cellphone;
    }



    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }



    public String getUsername() {
        return username;
    }



    public void setUsername(String username) {
        this.username = username;
    }

}

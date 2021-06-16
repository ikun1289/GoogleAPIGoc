package com.example.googleapi.Models;

import java.util.Date;

public class BinhLuan {
    public String IDBinhLuan;
    public String IDNguoiGui;
    public String email;
    public String noiDung;
    public String time;
    public Date date;

    public BinhLuan(){}

    public BinhLuan(String IDNguoiGui,String email, String noiDung, String time, Date date) {
        this.IDNguoiGui = IDNguoiGui;
        this.noiDung = noiDung;
        this.time = time;
        this.email = email;
        this.date = date;
    }
}

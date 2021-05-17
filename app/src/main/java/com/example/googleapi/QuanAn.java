package com.example.googleapi;

import java.io.Serializable;

public class QuanAn implements Serializable {
    public int IDQuanAn;
    public String TenQuan;
    public String GioMoCua;
    public String GioDongCua;
    public String MoTa;
    public String Lat,Lng;

    public QuanAn(int IDQuanAn,String tenQuan, String gioMoCua, String gioDongCua, String moTa, String lat, String lng) {
        this.IDQuanAn = IDQuanAn;
        TenQuan = tenQuan;
        GioMoCua = gioMoCua;
        GioDongCua = gioDongCua;
        MoTa = moTa;
        Lat = lat;
        Lng = lng;
    }
}

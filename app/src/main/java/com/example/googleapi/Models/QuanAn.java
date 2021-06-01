package com.example.googleapi.Models;

import java.io.Serializable;

public class QuanAn implements Serializable {
    public String ID;
    public String TenQuan;
    public String GioMoCua;
    public String GioDongCua;
    public String MoTa;
    public String Lat,Lng;

    public QuanAn(){}

    public QuanAn(String IDQuanAn,String tenQuan, String gioMoCua, String gioDongCua, String moTa, String lat, String lng) {
        this.ID = IDQuanAn;
        TenQuan = tenQuan;
        GioMoCua = gioMoCua;
        GioDongCua = gioDongCua;
        MoTa = moTa;
        Lat = lat;
        Lng = lng;
    }
}
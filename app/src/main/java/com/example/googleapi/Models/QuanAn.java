package com.example.googleapi.Models;

import java.io.Serializable;

public class QuanAn implements Serializable{
    public String ID;
    public String TenQuan;
    public String GioMoCua;
    public String GioDongCua;
    public String MoTa;
    public String Lat,Lng;
    public String imgQuanAn;

    public QuanAn(){}

    public QuanAn(String IDQuanAn,String tenQuan, String gioMoCua, String gioDongCua, String moTa, String lat, String lng, String imgQuanAn) {
        this.ID = IDQuanAn;
        TenQuan = tenQuan;
        GioMoCua = gioMoCua;
        GioDongCua = gioDongCua;
        MoTa = moTa;
        Lat = lat;
        Lng = lng;
        this.imgQuanAn = imgQuanAn;
    }

    public QuanAn(QuanAn quanAn)
    {
        this.ID = quanAn.ID;
        TenQuan = quanAn.TenQuan;
        GioMoCua = quanAn.GioMoCua;
        GioDongCua = quanAn.GioDongCua;
        MoTa = quanAn.MoTa;
        Lat = quanAn.Lat;
        Lng = quanAn.Lng;
        imgQuanAn = quanAn.imgQuanAn;
    }
}

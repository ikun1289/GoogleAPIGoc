package com.example.googleapi.Models;

public class MonAn {
    public String IDMonAn;
    public String TenMonAn;
    public String MoTa;
    public String Gia;
    public String ImgMonAn ="";

    public MonAn(){}

    public MonAn(String IDMonAn, String tenMonAn, String moTa, String gia, String ImgMonAn) {
        this.IDMonAn = IDMonAn;
        TenMonAn = tenMonAn;
        MoTa = moTa;
        Gia = gia;
        this.ImgMonAn = ImgMonAn;
    }
}

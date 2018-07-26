package com.example.m0z.list_and_adapter;


/**
 * SelectSheetListViewに必要なデータを取得するクラス
 */
public class MyListItem {

    protected int id;           // id
    protected String product;   // 品名
    protected String madeIn;    // 産地
    protected String number;    // 個数
    protected String price;     // 単価

    //-----------------
    //　コンストラクタ
    //-----------------
    public MyListItem(int id, String product, String madeIn, String number, String price){
        this.id = id;
        this.product = product;
        this.madeIn = madeIn;
        this.number = number;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public String getMadeIn() {
        return madeIn;
    }

    public String getNumber() {
        return number;
    }

    public String getPrice() {
        return price;
    }
}

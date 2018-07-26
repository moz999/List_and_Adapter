package com.example.m0z.list_and_adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

    public static final String DB_NAME  = "sample.db";  //DB名
    public static final String DB_TABLE = "mySheet";    //DBテーブル名
    public static final int DB_VERSION  = 1;            //DBバージョン

    //DBのカラム名
    public static final String COL_ID       = "_id";        // id
    public static final String COL_PRODUCT  = "product";    //品名
    public static final String COL_MADEIN   = "madein";     //産地
    public static final String COL_NUMBER   = "number";     //個数
    public static final String COL_PRICE    = "price";      //単価

    private SQLiteDatabase db = null;   //SQLiteDataBase
    private DBHelper dbHelper = null;   //DBHelper;
    protected Context context;          //Context

    //-----------------------------
    // コンストラクタ
    //-----------------------------
    public DBAdapter(Context context){
        this.context = context;
        dbHelper = new DBHelper(this.context);
    }

    /**
     * DataBaseの読み書き
     * @return this 自身のオブジェクト
     */
    public DBAdapter openDB(){
        db = dbHelper.getWritableDatabase();    //DBの読み書き
        return this;
    }

    /**
     * DataBaseの読み込み
     * @return this 自身のオブジェクト
     */
    public DBAdapter readDB(){
        db = dbHelper.getReadableDatabase();
        return this;
    }

    /**
     * DBを閉じる
     */
    public void closeDB(){
        db.close();
        db = null;
    }

    /**
     *  DBのレコードへ登録
     * @param product    品名
     * @param madeIn　   産地
     * @param number　   個数
     * @param price　    単価
     */
    public void saveDB(String product, String madeIn, int number, int price){

        db.beginTransaction();  //トランザクション開始

        try{
            ContentValues values = new ContentValues();
            values.put(COL_PRODUCT, product);
            values.put(COL_MADEIN, madeIn);
            values.put(COL_NUMBER, number);
            values.put(COL_PRICE, price);

            //insertメソッド　データ登録
            db.insert(DB_TABLE, null, values);

            //トランザクションの成功を通知する
            db.setTransactionSuccessful();

        }catch(Exception e){
            e.printStackTrace();

        }finally{
            //トランザクションの処理は呼び出さず、トランザクションを終了させる
            db.endTransaction();
        }
    }

    /**
     * DBの情報を取得する
     * @param columns　取得するカラム名　nullの場合は全カラム取得
     * @return DBのデータ
     */
    public Cursor getDB(String[] columns){
        // queryメソッド DBのデータを取得
        // 第1引数：DBのテーブル名
        // 第2引数：取得するカラム名
        // 第3引数：選択条件(WHERE句)
        // 第4引数：第3引数のWHERE句において?を使用した場合に使用
        // 第5引数：集計条件(GROUP BY句)
        // 第6引数：選択条件(HAVING句)
        // 第7引数：ソート条件(ODERBY句)
        return db.query(DB_TABLE, columns, null, null, null, null, null);

    }

    /**
     * DBの検索したデータを取得
     * @param columns   取得するカラム名
     * @param column    選択するカラム名
     * @param name      取り出したいデータの文字列
     * @return  DBの検索したいデータ
     */
    public Cursor searchDB(String[] columns, String column, String name[]){
        return db.query(DB_TABLE, columns, column + " like ?", name, null, null, null);
    }

    /**
     * DBのレコードを全削除する
     */
    public void allDelete(){

        db.beginTransaction();  //トランザクション開始

        try{
            // 1 : Table Name
            // 2 : WHERE句
            // 3 : WHERE句の詳細
            db.delete(DB_TABLE, null, null); //レコード全削除

            // トランザクションが成功したことを通知する
            db.setTransactionSuccessful();

        }catch(Exception e){
            e.printStackTrace();

        }finally{
            // トランザクションを終了させる
            db.endTransaction();
        }
    }

    /**
     * DBのレコードを1行削除する
     * @param position String
     */
    public void selectDelete(String position){

        db.beginTransaction();

        try{
            db.delete(DB_TABLE, COL_ID + " = ?", new String[]{position});

            db.setTransactionSuccessful();

        }catch(Exception e){
            e.printStackTrace();

        }finally{
            db.endTransaction();
        }
    }


//-----------------------------------------------------------
//
// Class of SQLiteOpenHelper
//
//-----------------------------------------------------------
    public static class DBHelper extends SQLiteOpenHelper{

        //コンストラクタ
        public DBHelper(Context context){
            // 1 : コンテキスト
            // 2 : DB Name
            // 3 : factory nullで良い
            // 4 : DB Version
            super(context, DB_NAME, null, DB_VERSION);
        }

        //-------------------------------------------
        // implements of SQLiteOpenHelper
        @Override
        public void onCreate(SQLiteDatabase db) {
            //テーブルを生成するSQL文の定義　＊スペースに気をつける！
            String createTable = "CREATE TABLE " + DB_TABLE + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_PRODUCT + " TEXT NOT NULL,"
                    + COL_MADEIN + " TEXT NOT NULL,"
                    + COL_NUMBER + " INTEGER NOT NULL,"
                    + COL_PRICE + " INTEGER NOT NULL"
                    + ");";

            db.execSQL(createTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}

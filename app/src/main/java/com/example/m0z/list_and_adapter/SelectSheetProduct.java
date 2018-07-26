package com.example.m0z.list_and_adapter;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectSheetProduct extends AppCompatActivity {

    private DBAdapter dbAdapter;            //DBAdapter
    private ArrayAdapter<String> adapter;   //ArrayAdapter
    private ArrayList<String> items;        //ArrayList

    private ListView mListView02Product;    //ListView
    private Button mButton02AllDelete;      //全削除ボタン


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_sheet_product);
        //タイトルバーの文言セット
        setTitle(R.string.text02_title);

        dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();     // DataBaseオープン（読み書き）

        findView();     // 各部品の呼び込み

        // ArrayListを生成
        items = new ArrayList<>();

        //DBのデータを取得
        String[] columns ={DBAdapter.COL_PRODUCT};  // DBのカラム：品名
        Cursor c = dbAdapter.getDB(columns);

        if(c.moveToFirst()){
            do{
                items.add(c.getString(0));

            } while(c.moveToNext());
        }

        c.close();
        dbAdapter.closeDB();

        //----------------------------------------------------------
        // ArrayAdapterのコンストラクタ
        //
        // １：Context
        // ２：リソースとして登録されたTextViewに対するリソースID　
        // 　　※今回は元々用意されている定義済みのレイアウトファイルのID
        // ３：一覧させたいデータの配列
        //----------------------------------------------------------
        adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_selectable_list_item, items);
        mListView02Product.setAdapter(adapter); //ListViewにAdapterをセット

        //　ArrayAdapterに対してListViewのリスト(items)の更新
        adapter.notifyDataSetChanged();


        //全削除ボタンを押した時の処理
        mButton02AllDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!items.isEmpty()){
                    dbAdapter.openDB();
                    dbAdapter.allDelete();
                    dbAdapter.closeDB();

                    //ArrayAdapterの更新
                    //ClearしてからAddすること
                    adapter.clear();
                    adapter.addAll(items);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void findView(){
        mListView02Product = (ListView)findViewById(R.id.listView02Product);
        mButton02AllDelete = (Button)findViewById(R.id.button02AllDeleter);
    }
}

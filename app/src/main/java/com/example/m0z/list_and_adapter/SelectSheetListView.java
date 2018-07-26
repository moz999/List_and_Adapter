package com.example.m0z.list_and_adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SelectSheetListView extends AppCompatActivity {

    private DBAdapter dbAdapter;
    private MyBaseAdapter myBaseAdapter;
    private List<MyListItem> items;
    private ListView mListView03;
    protected MyListItem myListItem;

    //参照するDBのカラム：ID, 品名, 産地, 個数, 単価の全部なのでnullを指定
    private String[] columns = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_sheet_listview);

        //DBAdapterのコンストラクタ呼び出し
        dbAdapter = new DBAdapter(this);
        //itemsのArrayListを作成
        items = new ArrayList<>();
        //MyBaseAdapterのコンストラクタ呼び出し
        myBaseAdapter = new MyBaseAdapter(this, items);
        //ListViewの結びつけ
        mListView03 = (ListView)findViewById(R.id.listView03);

        loadMyList();

        //行を長押しした時の処理
        mListView03.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                //アラートダイアログ表示
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectSheetListView.this);
                builder.setTitle("削除");
                builder.setMessage("削除しますか？");
                //OKのときの処理
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //IDを取得する
                        myListItem = items.get(position);
                        int listId = myListItem.getId();

                        dbAdapter.openDB();
                        dbAdapter.selectDelete(String.valueOf(listId));
                        dbAdapter.closeDB();
                        loadMyList();
                    }
                });

                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                //ダイアログの表示
                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });


    }

    /**
     * DBを読み込む＆更新する処理
     */
    private void loadMyList(){

        //ArrayAdapterに対してListViewのリスト(items)の更新
        items.clear();

        dbAdapter.openDB();
        //DBのデータを取得
        Cursor c = dbAdapter.getDB(columns);

        if(c.moveToFirst()){
            do{
                //MyListItemのコンストラクタ呼び出し
                myListItem = new MyListItem(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4));

                        Log.d(" 取得したCursor(ID) : ", String.valueOf(c.getInt(0)));
                        Log.d("取得したCursor(品名) ： ", c.getString(1));

                        items.add(myListItem);

            }while(c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();
        mListView03.setAdapter(myBaseAdapter); // ListViewにmyBaseAdapterをセット
        myBaseAdapter.notifyDataSetChanged(); // ListViewの更新

    }

    public class MyBaseAdapter extends BaseAdapter{

        private Context context;
        private List<MyListItem> items;

        //毎回findViewByIdをすることなく、高速化が出来るようにするクラス
        private class ViewHolder{
            TextView text05Product;
            TextView text05MadeIn;
            TextView text05Number;
            TextView text05Price;
        }

        //コンストラクタ
        public MyBaseAdapter(Context context, List<MyListItem> items){
            this.context = context;
            this.items = items;
        }

        //Listの要素数を返す
        @Override
        public int getCount() {
            return items.size();
        }

        //indexやオブジェクトを返す
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        //IDを他のindexに渡す
        @Override
        public long getItemId(int position) {
            return position;
        }

        //新しいデータが表示するタイミングで呼び出される
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            ViewHolder holder;

            //データを取得
            myListItem = items.get(position);

            if(view == null){
                LayoutInflater inflater =
                        (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.row_sheet_listview, parent, false);

                TextView text05Product = (TextView)view.findViewById(R.id.text05Product);
                TextView text05MadeIn = (TextView)view.findViewById(R.id.text05MadeIn);
                TextView text05Number = (TextView)view.findViewById(R.id.text05Number);
                TextView text05Price = (TextView)view.findViewById(R.id.text05Price);

                //HolderにViewを持たせて置く
                holder = new ViewHolder();
                holder.text05Product = text05Product;
                holder.text05MadeIn = text05MadeIn;
                holder.text05Number = text05Number;
                holder.text05Price = text05Price;
                view.setTag(holder);

            }else{
                //初めて表示されるときにつけておいたtagを元にviewを取得する
                holder = (ViewHolder)view.getTag();
            }

            //取得した各データをTextViewにセット
            holder.text05Product.setText(myListItem.getProduct());
            holder.text05MadeIn.setText(myListItem.getMadeIn());
            holder.text05Number.setText(myListItem.getNumber());
            holder.text05Price.setText(myListItem.getPrice());

            return view;
        }
    }
}

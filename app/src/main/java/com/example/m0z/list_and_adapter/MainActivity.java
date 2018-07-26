package com.example.m0z.list_and_adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{


    private EditText mEditText01Product;    //品名
    private EditText mEditText01MadeIn;     //産地
    private EditText mEditText01Number;     //個数
    private EditText mEditText01Price;      //価格

    private TextView mText01Kome01;         //品名の*印
    private TextView mText01Kome02;         //産地の*印
    private TextView mText01Kome03;         //個数の*印
    private TextView mText01Kome04;         //価格の*印

    private Button mButton01Register;       //登録ボタン
    private Button mButton01Show;           //表示ボタン

    private RadioGroup mRadioGroup01Show;   //選択用ラジオボタングループ

    private Intent intent;                  //インテント

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();    //アクティビティの部品取得
        init();         //初期処理

        mRadioGroup01Show.setOnCheckedChangeListener(this); //ラジオボタン選択時

        //登録ボタン押下時処理
        mButton01Register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //DBに登録
                saveList();
            }
        });

        //表示ボタン押下時処理
        mButton01Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intent != null){
                    startActivity(intent);
                }else{
                    Toast toast = Toast.makeText(MainActivity.this,
                                    "ラジオボタンが選択されていません", Toast.LENGTH_SHORT);
                }
            }
        });

    }

    /**
     * アクティビティの部品を取得
     */
    private void findViews(){
        mEditText01Product  = (EditText)findViewById(R.id.editText01product);
        mEditText01MadeIn   = (EditText)findViewById(R.id.editText01MadeIn);
        mEditText01Number   = (EditText)findViewById(R.id.editText01Number);
        mEditText01Price    = (EditText)findViewById(R.id.editText01Price);

        mText01Kome01 = (TextView)findViewById(R.id.text01Kome01);
        mText01Kome02 = (TextView)findViewById(R.id.text01kome02);
        mText01Kome03 = (TextView)findViewById(R.id.text01kome03);
        mText01Kome04 = (TextView)findViewById(R.id.text01kome04);

        mButton01Register   = (Button)findViewById(R.id.button01register);
        mButton01Show       = (Button)findViewById(R.id.button01show);

        mRadioGroup01Show   = (RadioGroup)findViewById(R.id.radioGroup01);
    }

    /**
     * アクティビティの初期処置
     */
    private void init() {
        mEditText01Product.setText("");
        mEditText01MadeIn.setText("");
        mEditText01Number.setText("");
        mEditText01Price.setText("");

        mText01Kome01.setText("");
        mText01Kome02.setText("");
        mText01Kome03.setText("");
        mText01Kome04.setText("");

        mEditText01Product.requestFocus();  //フォーカスは品名のEditTextに当てる
    }

    /**
     * EditTextに入力したテキストをDBに登録
     */
    private void saveList(){
        //各EditTextで入力されたテキストを取得
        String strProduct   = mEditText01Product.getText().toString();
        String strMadeIn    = mEditText01MadeIn.getText().toString();
        String strNumber    = mEditText01Number.getText().toString();
        String strPrice     = mEditText01Price.getText().toString();

        //EditTextが空白の場合
        if(strProduct.equals("") || strMadeIn.equals("") || strNumber.equals("") || strPrice.equals("")){

            if(strProduct.equals("")){
                mText01Kome01.setText("*");
            }else{
                mText01Kome01.setText("");
            }

            if(strMadeIn.equals("")){
                mText01Kome02.setText("*");
            }else{
                mText01Kome02.setText("");
            }

            if(strNumber.equals("")){
                mText01Kome03.setText("*");
            }else{
                mText01Kome03.setText("");
            }

            if(strPrice.equals("")){
                mText01Kome04.setText("*");
            }else{
                mText01Kome04.setText("");
            }

            Toast toast = Toast.makeText(MainActivity.this, "*の箇所を入力してください", Toast.LENGTH_SHORT);
            toast.show();

        }else{ //EditTextが全て入力されている場合

            //入力された単価と個数は文字列からint型へ変換
            int iNumber = Integer.parseInt(strNumber);
            int iPrice  = Integer.parseInt(strPrice);

            //DBへの登録処理
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.openDB();
            dbAdapter.saveDB(strProduct, strMadeIn, iNumber, iPrice);
            dbAdapter.closeDB();

            init();
        }

    }


    //-----------------------------------------------------
    // implements of RadioGroup.OnCheckedChangeListener
    //-----------------------------------------------------
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.radioButton01Product: // 品名一覧(ArrayAdapter)
                intent = new Intent(MainActivity.this, SelectSheetProduct.class);
                break;

            case R.id.radioButton01ListView: // ListViewの表示
                intent = new Intent(MainActivity.this, SelectSheetListView.class);
                break;
        }
    }
}

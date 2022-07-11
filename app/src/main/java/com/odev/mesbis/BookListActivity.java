package com.odev.mesbis;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

public class BookListActivity extends AppCompatActivity {

    VeriTabani vt;//veritabani tanimlamasi
    LinearLayout rezervasyonLinerL;//kamelya rezervasyon listemiz

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        vt = new VeriTabani(getApplicationContext());//yeni bir veritabani tanimlamasi

        init();
        initList();
    }

    void init() {
        rezervasyonLinerL = findViewById(R.id.bookLayout);
    }

    void initList() {
        rezervasyonLinerL.removeAllViews();//tum listeyi temizliyoruz
        for (HashMap<String, String> map : vt.kamelyalar()) {
            TextView valueTV = new TextView(this);//yeni bir text objesi olusturuyoruz
            String s;
            //veritabanindan rezerve edilen bilgilere gore kamelya numarasi ve rezerve eden bilgisini geciyoruz
            s = map.get("kamelya_number") + " Numaralı Kamelya " + String.valueOf(vt.kullaniciGetir(Integer.parseInt(map.get("kamelya_sahip_id")))) + " tarafından rezerve edildi.";
            valueTV.setText(s);//text objemize olusan stringi atiyoriz
            valueTV.setTextSize(16);//text objemize size veriyourz
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(30, 20, 30, 10);//margin bilgisi ayarlaniyor
            valueTV.setLayoutParams(params);

            int seatID= Integer.parseInt(map.get("kamelya_sahip_id"));//silme isleminde idye gore silecegiz burada tutuyoruz
            valueTV.setOnClickListener(new View.OnClickListener() {//silme islemi icin onclick ekliyoruz
                @Override
                public void onClick(View view) {
                    //silme islemi icin popup
                    AlertDialog.Builder builder = new AlertDialog.Builder(BookListActivity.this);

                    builder.setTitle("Uyarı");//popup in basligi
                    builder.setMessage("Bu kamelya rezervasyonu silinsin mi ?");//poupin aciklamasi

                    //evet e basilirsa
                    builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
vt.kamelyaSil(seatID);//veritabanindan idye gore kamelyayi siliyoruz
initList();//listemizi guncelluyoruz
                            dialog.dismiss();//popupi kapatiyoruz
                        }
                    });
                    //hayir basilirsa herhangibir islem yapmadan popupi kapatiyoruz
                    builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();//burada popup olusyor
                    alert.show();//popupi ekrana basiyoruz

                }
            });
            rezervasyonLinerL.addView(valueTV);//olusan text objesini listemize ekliyourz

        }

    }
}


package com.odev.mesbis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList<ImageView> kamelyaList = new ArrayList<ImageView>();//Ekranda gorunen kamelyalarin imageViewlerini buradaki listede tutuyoruz
    VeriTabani veritabani;//veritabanimizi kullanmak icin tanimliyoruz

    int kullaniciID;//giris yapan kullanicinin id sini burada tutuyoruz veritabanina kayit icin kullanicagiz
    int rezervasyonID = -1;//kullanicinin rezerve ettigi bir kamelya varsa burada idsi tutuluyor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        veritabani = new VeriTabani(getApplicationContext());//yeni bir veritabani classi olusturuyoruz
        init();

        Bundle extras = getIntent().getExtras();//kullanici id sini login activitesinden gelen id ile yakalayip dolduruyoruz
        if (extras != null) {
            kullaniciID = extras.getInt("userID");

        }

        kamelyalariDoldur();//daha onceki kullanicilar tarafindan rezerve edilen kolduklari ve kendi koltugumuz varsa burada onlari isaretliyoruz
    }

    void init() {
        for (int i = 1; i <= 34; i++) {//34 adet kamelyayi burada okuyup listemize ekliyoruz
            int resID = getResources().getIdentifier("seat" + i, "id", getPackageName());
            kamelyaList.add(findViewById(resID));
        }
    }

    void kamelyalariDoldur() {//veritabanindan daha once rezerve edilen tup kamelyalari aliyor ve renklerini kirmizi yapiyoruz
        for (HashMap<String, String> map : veritabani.kamelyalar()) {
            if (kullaniciID == Integer.parseInt(map.get("kamelya_sahip_id"))) {//eger kamelya id si bizi kullanici adimiz ile ayni ise onun rengini turuncu yapiyoruz
                rezervasyonID = Integer.parseInt(map.get("kamelya_number")) - 1;
                kamelyaList.get(rezervasyonID).setImageResource(R.color.myseat);
            } else {//ayni degilse rengini kirmizi yapiyoruz
                kamelyaList.get(Integer.parseInt(map.get("kamelya_number")) - 1).setImageResource(R.color.book);

            }
        }
    }

    public void onRezervationClicked(View view) {//tiklanan kamelyanin durumuna gore islem yapiyoruz
        int seatNumber = Integer.parseInt(String.valueOf(view.getTag()));
        if (!veritabani.kamelyaKontrol(seatNumber)) {//eger kamelya veritabaninda eklenmemisse bunu kullanici adi ile beraber veritabanina ekliyoruz
            veritabani.kamelyaSil(kullaniciID);
            veritabani.kamelyaEkle(seatNumber, kullaniciID);
            kamelyayiRezerveEt(seatNumber - 1);//rezerve ederken bu fonksiyondan  yararlaniyoruz
        } else {//daha once veritabanina eklenmisse uyari veriyoruz
            Toast.makeText(this, "Bu Kamelya zaten rezerve edilmiş", Toast.LENGTH_LONG).show();
        }

    }

    void kamelyayiRezerveEt(int kamelyaNumarasi) {
        if (rezervasyonID != -1) {//rezervasyon id -1 degilse burayi rezervasyonid nin oldugu kamelyayi bosa aliyor ve yeni kemalya numarasini turuncuya boyuyoruz
            kamelyaList.get(rezervasyonID).setImageResource(R.color.empty);
            kamelyaList.get(kamelyaNumarasi).setImageResource(R.color.myseat);
        } else {//aksi durumda ilk tercif oldugundan kamelyayi turuncuya boyuyoruz
            kamelyaList.get(kamelyaNumarasi).setImageResource(R.color.myseat);
        }
        rezervasyonID = kamelyaNumarasi;//kamelya numaramizda yeni siyle degisiyor
    }

}
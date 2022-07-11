package com.odev.mesbis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GirisActivity extends AppCompatActivity {

    EditText kullaniciAdiEt, parolaEt;//kullanici id edittext ve parola edittext tanimlamasi
    Button girisYapBtn, kayitOlBtn, hakkindaBtn;//giris kayit ol ve hakkinda buton tanimlamalari
    VeriTabani veritabani;//veritabani tanimlamasi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        veritabani = new VeriTabani(getApplicationContext());//yeni bir veritabani obhesi olusturuyoruz
        init();
        initListeners();
    }

    void init() {
        kullaniciAdiEt = findViewById(R.id.unameEt);
        parolaEt = findViewById(R.id.passwordEt);
        girisYapBtn = findViewById(R.id.loginBtn);
        kayitOlBtn = findViewById(R.id.registerBtn);
        hakkindaBtn =findViewById(R.id.aboutBtn);
    }

    void initListeners() {
        //giris butonuna basilirsa
        girisYapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//kullanici adi ve parola bosmu degilmi kontrol ediyoruz
                if (!kullaniciAdiEt.getText().toString().isEmpty() && !parolaEt.getText().toString().isEmpty()) {
                   //veritabanindan girilen bilgileri sorguluyoruz
                    if (veritabani.kullaniciKontrol(kullaniciAdiEt.getText().toString(), parolaEt.getText().toString())) {
                        //kullanici adminmi degilmi kontrol ediyoruz
                        if(kullaniciAdiEt.getText().toString().equals("admin")){//admin ise rezerve edilen kamelyalarin listelendigi sayfaya yonlendiriyoruz
                            Intent myadminIntent = new Intent(GirisActivity.this, BookListActivity.class);
                            GirisActivity.this.startActivity(myadminIntent);

                        }else{//admin degilse normal giris yaptiriyoruz
                            Intent myIntent = new Intent(GirisActivity.this, MainActivity.class);
                            myIntent.putExtra("userID", veritabani.kullaniciIdGetir(kullaniciAdiEt.getText().toString(), parolaEt.getText().toString()));
                            GirisActivity.this.startActivity(myIntent);
                        }

                    }else{//kullanici adi veya sifre hatali ilse bu uyariyi veriyoruz
                        Toast.makeText(getApplicationContext(), "Hatalı Kullanıcı Adı veya Şifre", Toast.LENGTH_LONG).show();

                    }
                } else {//bilgiler eksik ise bu hatayi veriyoruz
                    Toast.makeText(getApplicationContext(), "Lütfen Kullanıcı Adı ve Şifre Giriniz.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //kayit ol basildiginda
        kayitOlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kullanici adi ve parola bosmu ona bakiyoruz
                if (!kullaniciAdiEt.getText().toString().isEmpty() && !parolaEt.getText().toString().isEmpty()) {
                 //girilen bilgileri veritabanindan soruluyoruz varmi yokmu diye ayni kayit olmasin diye
                    //yoksa kullaniciyi veritabanina ekliyoruz
                    if (!veritabani.kullaniciKontrol(kullaniciAdiEt.getText().toString(), parolaEt.getText().toString())){
                        veritabani.kullaniciEkle(kullaniciAdiEt.getText().toString(), parolaEt.getText().toString());
                        kullaniciAdiEt.setText("");
                        parolaEt.setText("");
                        Toast.makeText(getApplicationContext(), "Kayıt Başarılı !", Toast.LENGTH_LONG).show();

                    }else{//kullanici zaten varsa bu uyariyi basiyoruz
                        Toast.makeText(getApplicationContext(), "Bu hesap zaten kayıtlı !", Toast.LENGTH_LONG).show();
                    }
                } else {//bilgiler eksikse bu uyariyi basiyoruz bossa yani
                    Toast.makeText(getApplicationContext(), "Lütfen Kullanıcı Adı ve Şifre Giriniz.", Toast.LENGTH_LONG).show();
                }
            }
        });
        //hakkinda butonu basildiginda hakkinda sayfasina yonlendiriyoruz
        hakkindaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myadminIntent = new Intent(GirisActivity.this, HakkindaActivity.class);
                GirisActivity.this.startActivity(myadminIntent);
            }
        });
    }

}
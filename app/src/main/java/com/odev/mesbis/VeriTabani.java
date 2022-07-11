package com.odev.mesbis;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class VeriTabani extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    private static final String DATABASE = "sqlliteDB";

    private static final String KULLANICI_TABLO = "kullanici_tablo";
    private static String KULLANICI_ID = "kullanici_id";
    private static String KULLANICI_AD = "kullanici_ad";
    private static String KULLANICI_PAROLA = "kullanici_parola";

    private static final String KAMELYA_TABLO = "kamelya_tablo";
    private static String KAMELYA_ID = "kamelya_id";
    private static String KAMELYA_NUMARA = "kamelya_number";
    private static String KAMELYA_SAHIP_ID = "kamelya_sahip_id";

    public VeriTabani(Context context) {
        super(context, DATABASE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //sql komutu seklinde kullanici tablosu olusturuyoruz
        String KULLANICI_TABLOSU = "CREATE TABLE " + KULLANICI_TABLO + "("
                + KULLANICI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KULLANICI_AD + " TEXT,"
                + KULLANICI_PAROLA + " TEXT" + ")";
        db.execSQL(KULLANICI_TABLOSU);
//        //sql komutu seklinde kamelya tablosu olusturuyoruz
        String KAMELYA_TABLOSU = "CREATE TABLE " + KAMELYA_TABLO + "("
                + KAMELYA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KAMELYA_NUMARA + " TEXT,"
                + KAMELYA_SAHIP_ID + " TEXT" + ")";
        db.execSQL(KAMELYA_TABLOSU);
    }
    /////////////////////////////////////////////////////////////////////////

    //kullanici eklemk icin kullanilan fonksiyorun kullanici adi ve parola ile veritabanina ekliyor
    public void kullaniciEkle(String kullanici_adi, String parola) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KULLANICI_AD, kullanici_adi);
        values.put(KULLANICI_PAROLA, parola);
        db.insert(KULLANICI_TABLO, null, values);
        db.close();
    }

    //kullanici varmi yokmu girilen bilgilere gore true false donen foksiyorun
    public boolean kullaniciKontrol(String email, String password) {

        String[] columns = {
                KULLANICI_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = KULLANICI_AD + " = ?" + " AND " + KULLANICI_PAROLA + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(KULLANICI_TABLO,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    //kullanicinin adi ve parolasina gore id gisini donen fonksiyon
    @SuppressLint("Range")
    public int kullaniciIdGetir(String kullanici_adi, String parola) {
        int userID = 0;

        String selectQuery = "SELECT * FROM " + KULLANICI_TABLO + " WHERE " + KULLANICI_AD + " = '" + kullanici_adi + "'" + " AND " + KULLANICI_PAROLA + " = '" + parola + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            userID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KULLANICI_ID)));
        }
        cursor.close();
        db.close();
        return userID;
    }

    //kullanicinin id sine gore o kullanicinin adini veren fonksiyon
    @SuppressLint("Range")
    public String kullaniciGetir(int kullaniciID) {
        String userName="";
        String selectQuery = "SELECT * FROM " + KULLANICI_TABLO + " WHERE " + KULLANICI_ID + " = '" + kullaniciID + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            userName = cursor.getString(cursor.getColumnIndex(KULLANICI_AD));
        }
        cursor.close();
        db.close();
        return userName;
    }
    ///////////////////////////////////////////////////////////////////////////

    //kamelya eklemek icin kullanilan fonksiyon kamelya numarasini hangi kullanici rezerve etmisse onun idsi ile veritabanina ekliyor
    public void kamelyaEkle(int kamelya_numarasi, int kamelya_sahip_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KAMELYA_NUMARA, kamelya_numarasi);
        values.put(KAMELYA_SAHIP_ID, kamelya_sahip_id);
//        values.put(SEAT_STATUE, 1);

        db.insert(KAMELYA_TABLO, null, values);
        db.close();
    }

    //tum kamelya bilgisini veritabanindan okuyup donen fonksiyon
    public ArrayList<HashMap<String, String>> kamelyalar() {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + KAMELYA_TABLO;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> seatlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                seatlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return seatlist;
    }


    //kamelya numarasina gore kamelyayikontrol eden ve ture false donen fonksiyon
    public boolean kamelyaKontrol(int seatNumber) {

        String[] columns = {
                KAMELYA_NUMARA
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KAMELYA_NUMARA + " = ?";

        String[] selectionArgs = {String.valueOf(seatNumber)};

        Cursor cursor = db.query(KAMELYA_TABLO,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    //kamelyani reerve eden kullanicinin idsine gore kamelyayi silen fonksiyon
    public void kamelyaSil(int kamelya_sahip_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(KAMELYA_TABLO, KAMELYA_SAHIP_ID + " = ?",
                new String[]{String.valueOf(kamelya_sahip_id)});
        db.close();
    }

    ///////////////////////////////////////////////////////////////////////////


    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }
}

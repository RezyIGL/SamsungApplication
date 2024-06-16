package com.rezyjs.samsungapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "MoneyDataBase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создаём БД MoneyDataBase, с параметрами:
        // - id Integer primary key
        // - operation TEXT
        // - money FLOAT
        db.execSQL("create Table MoneyDataBase(id INTEGER primary key autoincrement, operation TEXT, money FLOAT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists MoneyDataBase");
    }

    // Функция внесения новой информации
    public Boolean insertData(String operation, String money) {

        // Если нет операции - вернуть "Ложь"
        if (operation.isEmpty()) {
            return false;
        }

        // Если введено не число
        // Если значение пусто
        // Если число вышло за диапазон Float
        // Вернуть "Ложь"
        try {
            if (Float.parseFloat(money) == 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        // Получаем БД, которую можно менять
        SQLiteDatabase db = this.getWritableDatabase();

        // Создаём новый объект, который будем отправлять в БД
        ContentValues contentValues = new ContentValues();
        contentValues.put("operation", operation);
        contentValues.put("money", money);

        // Помещаем этот объект в БД и смотрим на результат помещения
        long result = db.insert("MoneyDataBase", null, contentValues);

        // Если результат не -1, то всё хорошо
        // Иначе вернуть "Ложь"
        return result != -1;
    }

    // Функция получения только Трат
    public Cursor getLosses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("Select * from MoneyDataBase where operation='Трата'", null);
    }

    // Функция получения только Пополнения
    public Cursor getAdditions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("Select * from MoneyDataBase where operation='Пополнение'", null);
    }

    // Функция получения всех операций
    public Cursor getAllOperations() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("Select * from MoneyDataBase", null);
    }
}

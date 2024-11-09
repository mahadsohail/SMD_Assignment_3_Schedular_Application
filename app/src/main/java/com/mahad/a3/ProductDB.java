package com.mahad.a3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductDB {
    public final String DATABASE_NAME = "products_db";
    public final String DATABASE_TABLE_NAME = "products";
    public final String KEY_ID = "id";
    public final String KEY_NAME = "name";  // Changed title to name
    public final String KEY_DATE = "date";
    public final String KEY_PRICE = "price";
    public final String KEY_STATUS = "status";

    private final int DB_VERSION = 1;
    private final Context context;
    private DBHelper dbHelper;

    public ProductDB(Context context) {
        this.context = context;
    }

    public void open() {
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DB_VERSION);
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(String name, String date, int price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);  // Insert name
        cv.put(KEY_DATE, date);  // Insert date
        cv.put(KEY_PRICE, price); // Insert price
        cv.put(KEY_STATUS, "new"); // Default status is "new"
        return db.insert(DATABASE_TABLE_NAME, null, cv);
    }

    public int updatePrice(int id, int price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_PRICE, price);
        return db.update(DATABASE_TABLE_NAME, cv, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    // New method to update product status
    public int updateProductStatus(int id, String newStatus) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_STATUS, newStatus);
        return db.update(DATABASE_TABLE_NAME, cv, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }


    // Fetch all products
    public ArrayList<Product> fetchProducts() {
        return fetchProductsByStatus(null); // Pass null to get all products
    }

    // Fetch products with "new" status
    public ArrayList<Product> fetchNewProducts() {
        return fetchProductsByStatus("new");
    }

    // Fetch products with "scheduled" status
    public ArrayList<Product> fetchScheduledProducts() {
        return fetchProductsByStatus("scheduled");
    }

    // Helper method to fetch products by status
    ArrayList<Product> fetchProductsByStatus(String status) {
        SQLiteDatabase readDb = dbHelper.getReadableDatabase();
        ArrayList<Product> products = new ArrayList<>();
        String[] columns = new String[]{KEY_ID, KEY_NAME, KEY_DATE, KEY_PRICE, KEY_STATUS};
        String selection = status != null ? KEY_STATUS + "=?" : null;
        String[] selectionArgs = status != null ? new String[]{status} : null;

        Cursor cursor = readDb.query(DATABASE_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int dateIndex = cursor.getColumnIndex(KEY_DATE);
            int priceIndex = cursor.getColumnIndex(KEY_PRICE);
            int statusIndex = cursor.getColumnIndex(KEY_STATUS);

            while (cursor.moveToNext()) {
                Product p = new Product(cursor.getInt(idIndex), cursor.getString(nameIndex), cursor.getString(dateIndex),
                        cursor.getInt(priceIndex), cursor.getString(statusIndex));
                products.add(p);
            }
            cursor.close();
        }
        return products;
    }

    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String query = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_NAME + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_NAME + " TEXT NOT NULL," +
                    KEY_DATE + " TEXT NOT NULL," +
                    KEY_PRICE + " INTEGER, " +
                    KEY_STATUS + " TEXT NOT NULL);";
            sqLiteDatabase.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}

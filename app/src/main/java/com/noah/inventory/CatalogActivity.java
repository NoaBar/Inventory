package com.noah.inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.noah.inventory.Data.ItemContract.ItemEntry;
import com.noah.inventory.Data.ItemDbHelper;

public class CatalogActivity extends AppCompatActivity {

    /** Database helper to provide access to the database */
    private ItemDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB (floating action button) to open AddItemActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new ItemDbHelper(this);
        insertData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void insertData() {
        ItemDbHelper mDbHelper = new ItemDbHelper(this);
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_NAME, "Primitivo");
        values.put(ItemEntry.COLUMN_ITEM_PRICE, 58);
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, 3);
        values.put(ItemEntry.COLUMN_ITEM_CATEGORY, ItemEntry.ITEM_CATEGORY_DRINK);
        values.put(ItemEntry.COLUMN_ITEM_SUPPLIER_NAME, "Jimmy");
        values.put(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE_NUMBER, "+41789999999");

        // Insert a new row for new item in the database.
        db.insert(ItemEntry.TABLE_NAME, null, values);
    }


        /**
         * Helper method to display information in the onscreen TextView about the state of
         * the items database.
         */
    private void displayDatabaseInfo() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_PRICE,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_CATEGORY,
                ItemEntry.COLUMN_ITEM_SUPPLIER_NAME,
                ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE_NUMBER
        };

        Cursor cursor = db.query(
                ItemEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = (TextView) findViewById(R.id.text_view_items);

        try {
            displayView.setText("The items table contains " + cursor.getCount() + " items.\n\n");
            displayView.append(ItemEntry._ID + " - " +
                    ItemEntry.COLUMN_ITEM_NAME + " - " +
                    ItemEntry.COLUMN_ITEM_PRICE + " - " +
                    ItemEntry.COLUMN_ITEM_QUANTITY + " - " +
                    ItemEntry.COLUMN_ITEM_CATEGORY + " - " +
                    ItemEntry.COLUMN_ITEM_SUPPLIER_NAME + " - "+
                    ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE_NUMBER + "\n");

            int idColumnIndex = cursor.getColumnIndex(ItemEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
            int categoryColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_CATEGORY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE_NUMBER);

            while (cursor.moveToNext()) {
                          int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                int currentCategory = cursor.getInt(categoryColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentCategory + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone));
            }
        } finally {
            cursor.close();
        }
    }
}

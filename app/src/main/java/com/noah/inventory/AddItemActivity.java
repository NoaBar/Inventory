package com.noah.inventory;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.noah.inventory.Data.ItemContract.ItemEntry;
import com.noah.inventory.Data.ItemDbHelper;

public class AddItemActivity extends AppCompatActivity {

    /**
     * Fields to enter the item info
     */
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private Spinner mCategorySpinner;
    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneEditText;

    /**
     * Category of the item. The possible values are:
     * 0 for other, 1 for food, 2 for drink.
     */
    private int mCategory = ItemEntry.ITEM_CATEGORY_OTHER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_item_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_item_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_item_quantity);
        mCategorySpinner = (Spinner) findViewById(R.id.spinner_category);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSupplierPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone_number);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the category of the item.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_category_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mCategorySpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.category_food))) {
                        mCategory = ItemEntry.ITEM_CATEGORY_FOOD; // Food
                    } else if (selection.equals(getString(R.string.category_drink))) {
                        mCategory = ItemEntry.ITEM_CATEGORY_DRINK; // Drink
                    } else {
                        mCategory = ItemEntry.ITEM_CATEGORY_OTHER; // Other
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCategory = ItemEntry.ITEM_CATEGORY_OTHER; // Other
            }
        });
    }

    /**
     * Get user input from editor and save new item into database.
     */
    private void insertItem() {
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierName = mSupplierNameEditText.getText().toString().trim();
        String supplierPhone = mSupplierPhoneEditText.getText().toString().trim();

        //changes string to int
        int quantity = Integer.parseInt(quantityString);

        ItemDbHelper mDbHelper = new ItemDbHelper(this);
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_NAME, nameString);
        values.put(ItemEntry.COLUMN_ITEM_PRICE, priceString);
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantity);
        values.put(ItemEntry.COLUMN_ITEM_CATEGORY, mCategory);
        values.put(ItemEntry.COLUMN_ITEM_SUPPLIER_NAME, supplierName);
        values.put(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE_NUMBER, supplierPhone);

        // Insert a new row for new item in the database, returning the ID of that new row.
        long newRowId = db.insert(ItemEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving item", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Item saved with row id " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                insertItem();
                //Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

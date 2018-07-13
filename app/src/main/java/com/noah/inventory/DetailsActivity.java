package com.noah.inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.noah.inventory.data.ItemContract.ItemEntry;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

@EActivity
@OptionsMenu(R.menu.menu_details)
public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindViews({R.id.details_name, R.id.details_price, R.id.details_quantity,
            R.id.details_category, R.id.details_supplier_name, R.id.details_supplier_phone_number})
    List<TextView> textViewList;

    @BindView(R.id.details_category)
    TextView categoryTextView;

    @BindView(R.id.details_quantity)
    TextView quantityTextView;

    //@BindViews({R.id.decrease_button, R.id.increase_button, R.id.call_button})
    //List<Button>buttonsList;

    @BindView(R.id.decrease_button)
    Button decreaseButton;
    @BindView(R.id.increase_button)
    Button increaseButton;
    @BindView(R.id.call_button)
    Button callButton;

    String[] projection = {
            ItemEntry.COLUMN_ITEM_NAME,
            ItemEntry.COLUMN_ITEM_PRICE,
            ItemEntry.COLUMN_ITEM_QUANTITY,
            ItemEntry.COLUMN_ITEM_CATEGORY,
            ItemEntry.COLUMN_ITEM_SUPPLIER_NAME,
            ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE_NUMBER,
            ItemEntry._ID
    };

    private Uri mCurrentItemUri;

    private static final int EXISTING_ITEM_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        //Get a URI of a specific item from the database.
        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        // Initialize a loader to read the item data from the database
        // and display the current values in the details activity
        getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
    }

    @OptionsItem(R.id.action_edit)
    public void edit() {
        //create new intent to go to {@link AddItemActivity}
        Intent intent = new Intent(this, AddItemActivity.class);

        //Set the URI on the data field of the intent
        intent.setData(mCurrentItemUri);

        //Launch the {@link EditorActivity} to display the data for the current item.
        startActivity(intent);
    }

    @OptionsItem(R.id.action_delete)
    public void deleteItemDetails() {
        showDeleteConfirmationDialog();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                mCurrentItemUri,         // Query the content URI for the current item
                projection,             // Columns to include in the resulting Cursor
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        if (cursor.moveToFirst()) {
            // Find the columns of item attributes that we're interested in
            for (int i = 0; i < 6; i++) {
                int columnIndex = cursor.getColumnIndex(projection[i]);
                String detail = cursor.getString(columnIndex);
                textViewList.get(i).setText(detail);
            }

            // Translate the category into a readable string.
            switch (categoryTextView.getText().toString()) {
                case "1":
                    categoryTextView.setText(R.string.category_food);
                    break;
                case "2":
                    categoryTextView.setText(R.string.category_drink);
                    break;
                default:
                    categoryTextView.setText(R.string.category_other);
                    break;
            }

            int idColumnIndex = cursor.getColumnIndex(ItemEntry._ID);
            final int itemId = cursor.getInt(idColumnIndex);

            increaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //query 2 specific columns in the item table
                    String[] projection = {
                            ItemEntry._ID,
                            ItemEntry.COLUMN_ITEM_QUANTITY};
                    Uri currentItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, itemId);
                    Cursor cursor = getContentResolver().query(currentItemUri, projection, null, null, null);

                    // if value is greater than 0, decrease by 1.
                    if (cursor.moveToFirst()) {
                        final int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
                        int quantityInt = cursor.getInt(quantityColumnIndex);

                        quantityInt++;

                        //updates the value and put the new value in the database.
                        ContentValues values = new ContentValues();
                        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantityInt);
                        getContentResolver().update(currentItemUri, values, null, null);

                        //updates the UI WITH THE NEW quantity value.
                        String quantityString = String.valueOf(quantityInt);
                        quantityTextView.setText(quantityString);
                    }
                }
            });
            
            decreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //query 2 specific columns in the item table
                    String[] projection = {
                            ItemEntry._ID,
                            ItemEntry.COLUMN_ITEM_QUANTITY};
                    Uri currentItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, itemId);
                    Cursor cursor = getContentResolver().query(currentItemUri, projection, null, null, null);

                    // if value is greater than 0, decrease by 1.
                    if (cursor.moveToFirst()) {
                        final int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
                        int quantityInt = cursor.getInt(quantityColumnIndex);
                        if (quantityInt == 0) {
                            return;
                        }
                        quantityInt--;

                        //updates the value and put the new value in the database.
                        ContentValues values = new ContentValues();
                        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantityInt);
                        getContentResolver().update(currentItemUri, values, null, null);

                        //updates the UI WITH THE NEW quantity value.
                        String quantityString = String.valueOf(quantityInt);
                        quantityTextView.setText(quantityString);
                    }
                }
            });
        }
    }

    // Reset all textViews to "".
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        for (TextView detail : textViewList) {
            detail.setText("");
        }
    }

    /**
     * Prompt the user to confirm that they want to delete this item.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the item.
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the item.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the item in the database.
     */
    private void deleteItem() {
        if (mCurrentItemUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.addItem_delete_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.addItem_delete_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}

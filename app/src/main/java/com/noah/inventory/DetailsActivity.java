package com.noah.inventory;

import android.Manifest;
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
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
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
import butterknife.OnClick;
import butterknife.OnItemClick;

@EActivity
@OptionsMenu(R.menu.menu_details)
public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    //The list of textViews is in the same order as the projection array.
    // That's how we know what should the text view contain.
    @BindViews({R.id.details_name,
            R.id.details_price,
            R.id.details_quantity,
            R.id.details_category,
            R.id.details_supplier_name,
            R.id.details_supplier_phone_number})
    List<TextView> textViewList;

    String[] projection = {
            ItemEntry.COLUMN_ITEM_NAME,
            ItemEntry.COLUMN_ITEM_PRICE,
            ItemEntry.COLUMN_ITEM_QUANTITY,
            ItemEntry.COLUMN_ITEM_CATEGORY,
            ItemEntry.COLUMN_ITEM_SUPPLIER_NAME,
            ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE_NUMBER,
            ItemEntry._ID
    };

    @BindView(R.id.details_category)
    TextView categoryTextView;

    @BindView(R.id.details_quantity)
    TextView quantityTextView;

    public Uri mCurrentItemUri;

    private static final int EXISTING_ITEM_LOADER = 0;

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 5;

    @OnClick(R.id.increase_button)
    public void onIncrease(View v) {
        Cursor cursor = getContentResolver().query(mCurrentItemUri, projection, null, null, null);

        // if value is greater than 0, decrease by 1.
        if (cursor.moveToFirst()) {
            final int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
            int quantityInt = cursor.getInt(quantityColumnIndex);

            quantityInt++;

            //updates the value and put the new value in the database.
            ContentValues values = new ContentValues();
            values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantityInt);
            getContentResolver().update(mCurrentItemUri, values, null, null);

            //updates the UI WITH THE NEW quantity value.
            String quantityString = String.valueOf(quantityInt);
            quantityTextView.setText(quantityString);
        }
    }


    @OnClick(R.id.decrease_button)
    public void onDecrease(View v) {
        Cursor cursor = getContentResolver().query(mCurrentItemUri, projection, null, null, null);

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
            getContentResolver().update(mCurrentItemUri, values, null, null);

            //updates the UI WITH THE NEW quantity value.
            String quantityString = String.valueOf(quantityInt);
            quantityTextView.setText(quantityString);
        }
    }


    @OnClick(R.id.call_button)
    public void onCall(View call) {
        Cursor cursor = getContentResolver().query(mCurrentItemUri, projection,
                null, null, null);

        if (cursor.moveToFirst()) {
            final int supplierPhone = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE_NUMBER);
            String supplierPhoneString = cursor.getString(supplierPhone);

            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + supplierPhoneString));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CALL_PHONE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted.
                    Cursor cursor = getContentResolver().query(mCurrentItemUri, projection,
                            null, null, null);

                    if (cursor.moveToFirst()) {
                        final int supplierPhone = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE_NUMBER);
                        String supplierPhoneString = cursor.getString(supplierPhone);

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + supplierPhoneString));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);
                    }
                }
                return;
            }
        }
    }


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

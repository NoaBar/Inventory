package com.noah.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.noah.inventory.data.ItemContract.ItemEntry;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;

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
    public  void edit(){
        //create new intent to go to {@link AddItemActivity}
        Intent intent = new Intent(this, AddItemActivity.class);

        //Set the URI on the data field of the intent
        intent.setData(mCurrentItemUri);

        //Launch the {@link EditorActivity} to display the data for the current item.
        startActivity(intent);
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
}

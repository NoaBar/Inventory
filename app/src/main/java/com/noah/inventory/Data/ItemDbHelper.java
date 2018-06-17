package com.noah.inventory.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.noah.inventory.Data.ItemContract.ItemEntry;


/**
 * Database helper for Inventory app. Manages database creation and version management.
 */
public class ItemDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = ItemDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "inventory.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link ItemDbHelper}.
     *
     * @param context of the app
     */
    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override



}

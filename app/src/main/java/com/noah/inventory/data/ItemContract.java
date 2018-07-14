package com.noah.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ItemContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private ItemContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.noah.inventory";

    /**
     * CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     */
    public static final String PATH_ITEMS = "items";

    /**
     * Inner class that defines constant values for the items database table.
     * Each entry in the table represents a single item.
     */
    public static final class ItemEntry implements BaseColumns {

        /**
         * The content URI to access the item data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        /**
         * Name of database table for items
         */
        public final static String TABLE_NAME = "items";

        /**
         * Unique ID number for the item (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the item.
         * Type: TEXT
         */
        public final static String COLUMN_ITEM_NAME = "name";

        /**
         * Price of the item.
         * Type: INTEGER
         */
        public final static String COLUMN_ITEM_PRICE = "price";

        /**
         * Quantity of the item.
         * Type: INTEGER
         */
        public final static String COLUMN_ITEM_QUANTITY = "quantity";

        /**
         * Category of the item.
         * The only possible values are {@link #ITEM_CATEGORY_OTHER}, {@link #ITEM_CATEGORY_FOOD},
         * or {@link #ITEM_CATEGORY_DRINK}.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_ITEM_CATEGORY = "category";

        /**
         * Possible values for the category of the item.
         */
        public final static int ITEM_CATEGORY_OTHER = 0;
        public final static int ITEM_CATEGORY_FOOD = 1;
        public final static int ITEM_CATEGORY_DRINK = 2;

        /**
         * Returns whether or not the given gender is {@link #ITEM_CATEGORY_OTHER}, {@link #ITEM_CATEGORY_FOOD},
         * or {@link #ITEM_CATEGORY_DRINK}.
         */
        public static boolean isValidCategory(int category) {
            return category == ITEM_CATEGORY_OTHER || category == ITEM_CATEGORY_FOOD || category == ITEM_CATEGORY_DRINK;
        }

        /**
         * Name of supplier.
         * Type: TEXT
         */
        public final static String COLUMN_ITEM_SUPPLIER_NAME = "supplier_name";

        /**
         * Phone number of supplier.
         * Type: TEXT
         */
        public final static String COLUMN_ITEM_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

    }
}


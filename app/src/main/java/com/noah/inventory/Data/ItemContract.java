package com.noah.inventory.Data;

import android.provider.BaseColumns;

public final class ItemContract {

    private ItemContract(){}

    public static final class ItemEntry implements BaseColumns{

        public final static String TABLE_NAME = "Items";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ITEM_NAME = "name";
        public final static String COLUMN_ITEM_PRICE = "price";
        public final static String COLUMN_ITEM_QUANTITY = "quantity";
        public final static String COLUMN_ITEM_CATEGORY = "category";
        public final static String COLUMN_ITEM_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_ITEM_SUPPLIER_PHONE_NUMBER ="supplier_phone_number";

        public final static int ITEM_CATEGORY_OTHER = 0;
        public final static int ITEM_CATEGORY_FOOD = 1;
        public final static int ITEM_CATEGORY_DRINK = 2;
    }

}


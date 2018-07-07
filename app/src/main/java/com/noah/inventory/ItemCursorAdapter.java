package com.noah.inventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.noah.inventory.data.ItemContract.ItemEntry;



public class ItemCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ItemCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new blank list item view.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the item data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView categoryTextView = (TextView) view.findViewById(R.id.category);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity_value);
        TextView priceTextView = (TextView) view.findViewById(R.id.price_value);

        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
        int categoryColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_CATEGORY);
        int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);



        // Read the pet attributes from the Cursor for the current pet
        String itemName = cursor.getString(nameColumnIndex);
        String itemCategory = cursor.getString(categoryColumnIndex);
        String itemQuantity = cursor.getString(quantityColumnIndex);
        String itemPrice = cursor.getString(priceColumnIndex);

        // Update the TextViews with the attributes for the current item
        nameTextView.setText(itemName);
        categoryTextView.setText(itemCategory);
        quantityTextView.setText(itemQuantity);
        priceTextView.setText(itemPrice);
    }

}

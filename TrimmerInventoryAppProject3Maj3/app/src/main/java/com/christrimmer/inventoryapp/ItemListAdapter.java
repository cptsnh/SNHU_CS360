package com.christrimmer.inventoryapp;

/******************************************************
 *
 * Author: Chris Trimmer
 * Course: CS360 Mobile Development
 * Project3: Inventory App
 * Date: 12/05/2023
 *
 * References and coding inspiration:
 *  CS360: Mobile Architecture and Programming, zyBooks
 *  Android Studio 4.2 Development Essentials, Packt Publishing, by Neil Smyth
 *  Navigation and SafeArgs: https://developer.android.com/codelabs/android-navigation#0
 *  SMS: https://google-developer-training.github.io/android-developer-phone-sms-course/Lesson%202/2_p_sending_sms_messages.html
 *  Dialogs: https://developer.android.com/develop/ui/views/components/dialogs#java
 *  RecyclerView: https://google-developer-training.github.io/android-developer-fundamentals-course-concepts-v2/unit-2-user-experience/lesson-4-user-interaction/4-5-c-recyclerview/4-5-c-recyclerview.html
 *  AppBar: https://developer.android.com/develop/ui/views/components/menus
 *  FAB: https://developer.android.com/develop/ui/views/components/floating-action-button
 *  Database/SQLite: https://www.codeproject.com/Articles/119293/Using-SQLite-Database-with-Android
 *  Room: https://developer.android.com/reference/android/arch/persistence/room/Room
 *  DAO: https://developer.android.com/training/data-storage/room/accessing-data
 *
 ***********************************************************/

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.widget.Toast;


import java.util.List;

// This is the adapter class for the RecyclerView
// An adapter enables two incompatible classes to work together
// Here it is connects the data (items) with the view
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private final int itemLayout;
    private List<Item> itemList;
    private ItemViewModel iViewModel;
    private Context context;
    private CustomerViewModel cViewModel;

    private Customer customer;

    // Constructors
    public ItemListAdapter(int layoutId, ItemViewModel ivm) {
        itemLayout = layoutId;
        iViewModel = ivm;
    }

    public ItemListAdapter(int layoutId, ItemViewModel ivm, Customer customer) {
        itemLayout = layoutId;
        iViewModel = ivm;
        this.customer = customer;
    }

    public ItemListAdapter(int layoutId) {
        itemLayout = layoutId;
    }

    // fill the item list and setup the notification when data is changed in the list
    public void setItemList(List<Item> items) {
        itemList = items;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view).linkAdapter(this);

        // remove this on final
//        View view = LayoutInflater.from(
//                parent.getContext()).inflate(itemLayout, parent, false);
//
//        return new ViewHolder(view);
    }

    // update the text and quantity of an item
    // this is also where I test for qty being set to 0 and if so, we send the SMS message (if enabled)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView name = holder.itemName;
        name.setText(itemList.get(position).getName());

        TextView qty = holder.itemQuantity;
        qty.setText(String.valueOf(itemList.get(position).getQuantity()));

        if (itemList.get(position).getQuantity() <= 0)
            ListFragment.SendMessage(context.getApplicationContext());

    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }



    // The viewholder defines the view that will be used to present the data
    // This includes the buttons for deletion, and incrementing and decrementing quantities
    // Each view item can be tapped to edit/view its details
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView itemQuantity;
        private ItemListAdapter adapter;
        private Context context;
        private InventoryDatabase database;


        // Constructor sets the textviews and sets the click listeners on the buttons
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.idTextView);
            itemQuantity = itemView.findViewById(R.id.qtyTextView);


            editItemSetup();
            deleteItemSetup();
            incrementSetup();
            decrementSetup();

        }

        // When clicking the increment button, increase the quantity by one, then update the view model
        private void incrementSetup() {
            itemView.findViewById(R.id.increaseButton).setOnClickListener(view -> {

                adapter.itemList.get(getAdapterPosition()).incrementQuantity();
                Item item = adapter.itemList.get(getAdapterPosition());
                adapter.iViewModel.updateItem(item);

            });
        }

        // When clicking the decrement button, decrease the quantity by one, then update the view model
        private void decrementSetup() {
            itemView.findViewById(R.id.decreaseButton).setOnClickListener(view -> {
                adapter.itemList.get(getAdapterPosition()).decrementQuantity();
                adapter.iViewModel.updateItem(adapter.itemList.get(getAdapterPosition()));
            });
        }

        // When clicking on the delete button, get the item name and the update the view model
        // the database and view are updated
        private void deleteItemSetup() {
            itemView.findViewById(R.id.deleteButton).setOnClickListener(view -> {

                long itemid = adapter.itemList.get(getAdapterPosition()).getId();
                String name = adapter.itemList.get(getAdapterPosition()).getName();
                adapter.iViewModel.deleteItem(name);

            });
        }

        // When clicking on an itemview, we navigate to the EditItem fragment
        // We pass the item through SafeArg so that the fields can be changed
        private void editItemSetup() {
            itemView.setOnClickListener(view -> {

                ListFragmentDirections.ActionListFragmentToEditItemFragment action =
                        ListFragmentDirections.actionListFragmentToEditItemFragment();

                Item item = adapter.itemList.get(getAdapterPosition());

                // set the customer and item objects to be sent via SafeArgs
                action.setCustomerEditItem(adapter.customer);
                action.setItemEditItem(item);

                Navigation.findNavController(view).navigate(action);

            });
        }




        // set up reference to the ItemListAdapter so that we can use it in the internal viewholder class
        // this enables us to perform operations on the list data supporting the button functions
        public ViewHolder linkAdapter(ItemListAdapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }

}

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

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.Toast;

import com.christrimmer.inventoryapp.databinding.FragmentAddItemBinding;

import java.util.List;

// This fragment is used to get details of an item the customer wants to add
// It passes back an Item object to the list fragment where it is inserted via the view model
public class AddItemFragment extends Fragment {

    // Member variables
    private FragmentAddItemBinding addItemBinding;
    private View view;
    private Context context;
    private ItemViewModel iViewModel;
    private Customer customer;
    private String itemName;
    private String itemDesc;
    private int itemQty;

    private CustomerViewModel cViewModel;
    private List<Customer> customerList;
    private long mCustId;

    public AddItemFragment() {
        // Required empty public constructor
    }

    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        // get reference to customer passed from list fragment through SafeArg
        customer = AddItemFragmentArgs.fromBundle(getArguments()).getCustomerAddItem();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        addItemBinding = FragmentAddItemBinding.inflate(inflater, container, false);
        view = addItemBinding.getRoot();
        context = view.getContext();

        // associate this fragment with the customer view model
        cViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        // set up the listeners and observers
        setUpListeners();
        setUpObservers();

        return view;
    }

    // This functions sets values based on user input
    // If any edit text views are empty, then i set a default value
    private void setItemValues() {

        // set item name
        if (addItemBinding.addItemName1.getText().toString().equals(""))
            itemName = "default item";
        else {
            itemName = addItemBinding.addItemName1.getText().toString();
        }

        // set item description
        if (addItemBinding.addDesc1.getText().toString().equals(""))
            itemDesc = "default desc";
        else {
            itemDesc = addItemBinding.addDesc1.getText().toString();
        }

        // need to make sure item quantity is set to an integer
        String temp = String.valueOf(addItemBinding.addQuantity1.getText()).toString();
        if (temp.equals("") || temp.length() == 0)
            itemQty = 0;
        else
            itemQty = Integer.valueOf(temp);

    }

    private void setUpListeners() {
        // set click listener on the done button
        addItemBinding.addDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // sets all values from text fields
                setItemValues();

                // create action to navigate back to list fragment
                AddItemFragmentDirections.ActionAddItemFragmentToListFragment action =
                        AddItemFragmentDirections.actionAddItemFragmentToListFragment();

                // send the customer and new item object through SafeArg back to list fragment
                Item item = new Item(customer.getId(), itemName, itemDesc, itemQty);

                action.setItemAdd(item);
                action.setCustomer1(customer);

                Navigation.findNavController(view).navigate(action);

            }
        });
    }

    // used for testing purposes for now
    private void setUpObservers() {

        cViewModel.getAllCustomers().observe(getViewLifecycleOwner(), new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {

                customerList = customers;

            }
        });

        cViewModel.getSearchResults().observe(getViewLifecycleOwner(), new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                if (customers.size() > 0 ) {
                    // testing
                }
                else {
                }
            }
        });

    }
}
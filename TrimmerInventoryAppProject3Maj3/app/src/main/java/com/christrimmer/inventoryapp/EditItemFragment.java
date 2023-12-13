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

import com.christrimmer.inventoryapp.databinding.FragmentEditItemBinding;

import java.util.List;

// This fragment enables the customer to edit the name, description, and/or quantity of an item
public class EditItemFragment extends Fragment {

    private FragmentEditItemBinding editItemBinding;
    private View view;
    private Context context;
    private Customer customer;
    private Item item;

    private CustomerViewModel cViewModel;


    public EditItemFragment() {
        // Required empty public constructor
    }

    public static EditItemFragment newInstance(String param1, String param2) {
        EditItemFragment fragment = new EditItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        // fix this when we get pk/fk working
        customer = EditItemFragmentArgs.fromBundle(getArguments()).getCustomerEditItem();


        // create reference to the item sent here from list fragment through SafeArg
        item = EditItemFragmentArgs.fromBundle(getArguments()).getItemEditItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        editItemBinding = FragmentEditItemBinding.inflate(inflater, container, false);
        view = editItemBinding.getRoot();
        context = view.getContext();

        cViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        setUpObservers();


        //cViewModel.findCustomer(item.getCustomerId());


        // set the edit textviews with the current item data passed from SafeArg
        editViewSetup();

        // set up the click listener on the done button
        doneSetup();

        return view;
    }

    private void setUpObservers() {

        cViewModel.getSearchResults().observe(getViewLifecycleOwner(), new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                if (customers.size() > 0 ) {
                    Log.i("cvmedit", "customer: " + customers.get(0).getName());

                    customer = customers.get(0);

                }
                else {

                }
            }
        });
    }

    // set the edit textviews with the current item data passed from SafeArg
    private void editViewSetup() {
        editItemBinding.editItemName1.setText(item.getName());
        editItemBinding.editDesc1.setText(item.getDescription());
        editItemBinding.editQuantity1.setText(String.valueOf(item.getQuantity()));
    }


    private void doneSetup() {

        editItemBinding.editDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set the name, description, and qty from information in the text fields
                item.setName(editItemBinding.editItemName1.getText().toString());
                item.setDescription(editItemBinding.editDesc1.getText().toString());

                // make sure quantity has an integer value
                if (editItemBinding.editQuantity1.getText().toString().equals("")) {
                    item.setQuantity(0);
                }
                else
                    item.setQuantity(Integer.parseInt(editItemBinding.editQuantity1.getText().toString()));

                // create action used to navigate back to list fragment
                EditItemFragmentDirections.ActionEditItemFragmentToListFragment action =
                        EditItemFragmentDirections.actionEditItemFragmentToListFragment();

                // need to straighten this out when pk/fk is set up
                action.setCustomer1(customer);

                // pass the item reference back to list fragment through SafeArg
                action.setItemEdit(item);

                Navigation.findNavController(view).navigate(action);

            }
        });


    }

}
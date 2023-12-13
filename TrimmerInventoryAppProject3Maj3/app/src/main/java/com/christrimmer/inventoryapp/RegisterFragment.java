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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.Toast;

import com.christrimmer.inventoryapp.databinding.FragmentRegisterBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

public class RegisterFragment extends Fragment {

    // Member variables

    private FragmentRegisterBinding registerBinding;
    View view;
    Context context;
    CustomerViewModel cViewModel;
    List<Customer> customerList;
    String name;
    String pass;
    String phone;
    Customer customer;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        registerBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        view = registerBinding.getRoot();
        context = view.getContext();

        // associate this fragment with the customer view model
        cViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        // register the observers and listeners
        setUpListeners();
        setUpObservers();

        return view;
    }

    // handle the listener objects
    private void setUpListeners() {


        // set listener on password textview so that register button stays disabled until at least eight characters are input
        registerBinding.editPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // change this to 8 in final version. Error textview appears until eight chars are input
                if (s.length() < 3 || s.equals("") || s.equals(null)) {
                    registerBinding.errorTextView2.setVisibility(View.VISIBLE);
                    registerBinding.registerButton.setEnabled(false);
                }
                else {
                    registerBinding.errorTextView2.setVisibility(View.INVISIBLE);

                    if (!registerBinding.editPassword2.getText().toString().equals("") ||
                            registerBinding.editPassword2.getText().toString().length() > 0)
                        registerBinding.registerButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // set listener on editName textview so that register button stays disabled until text is added
        registerBinding.editName2.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() <= 0 || s.equals("")) {
                            registerBinding.registerButton.setEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );

        // when a new user registers, we first test to make sure credentials are valid
        // if valid, then navigate to the list view
        // also provide cancel button to return to login fragment
        registerBinding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set the username, password, and phone number values from the edit text views
                name = registerBinding.editName2.getText().toString();
                pass = registerBinding.editPassword2.getText().toString();
                phone = registerBinding.editPhone.getText().toString();

                // call find to get the search result from db
                cViewModel.findCustomer(name);

            }
        });


        // navigate back to login fragment if cancel button is tapped
        registerBinding.registerCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment2);
            }
        });

    }

    ///////////////////////////  observers ///////////////////////////////
    private void setUpObservers() {

        // get the customerList from Room framework and database
        cViewModel.getAllCustomers().observe(getViewLifecycleOwner(), new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {

                customerList = customers;
           //     Log.i("searchlist", "new user id: " + customerList.get(customerList.size() - 1).getId());

            }
        });


        // when registering a new account, if the name is already in the database
        // then don't create a new account
        // if the name is available, then create the account
        cViewModel.getSearchResults().observe(getViewLifecycleOwner(), new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                if (customers.size() > 0 ) {
                    Log.i("searchlist", "register size: " + customers.size());
                    Toast.makeText(context, "That name is not available", Toast.LENGTH_SHORT).show();
                    clearEditFields();
                }
                else {
                    createAccount();
                }
            }
        });

    }

    // create new account using name, password, and phone from the edit text views
    private void createAccount() {
        customer = new Customer(name, pass, phone);
        cViewModel.insertCustomer(customer);

        Toast.makeText(context, "Account Created. Please Login", Toast.LENGTH_SHORT).show();

        // navigate back to the login fragment
        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment2);

    }

    // clear the edit fields
    private void clearEditFields() {
        registerBinding.editName2.setText("");
        registerBinding.editPassword2.setText("");
        registerBinding.editPhone.setText("");

    }
}
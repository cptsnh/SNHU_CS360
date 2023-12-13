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
import androidx.lifecycle.MutableLiveData;
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


import com.christrimmer.inventoryapp.databinding.FragmentLoginBinding;

import java.util.List;

public class LoginFragment extends Fragment {

    // Member variables
    private FragmentLoginBinding loginBinding;
    private View view;
    private Context context;
    private CustomerViewModel cViewModel;
    private List<Customer> customerList;
    String name;
    String pass;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        view = loginBinding.getRoot();
        context = view.getContext();

        // associate this fragment with the customer view model
        cViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        // register the observers and listeners
        setUpListeners();
        setUpObservers();


        return view;
    }


    private void setUpListeners() {

        // set listener on the login button
        loginBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get name and password values from the edit text views
                name = loginBinding.editName1.getText().toString();
                pass = loginBinding.editPassword1.getText().toString();

                validate(name, pass);

                clearEditFields();

            }
        });

        // set listener on password textview so that login button stays disabled until at least eight characters are input
        loginBinding.editPassword1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // change this to 8 in final version. Error textview appears until eight chars are input
                if (s.length() < 3 || s.equals("") || s.equals(null)) {
                    loginBinding.errorTextView.setVisibility(View.VISIBLE);
                    loginBinding.loginButton.setEnabled(false);
                }
                else {
                    loginBinding.errorTextView.setVisibility(View.INVISIBLE);

                    if (!loginBinding.editName1.getText().toString().equals("") ||
                            loginBinding.editName1.getText().toString().length() > 0)
                        loginBinding.loginButton.setEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // set listener on editName textview so that login button stays disabled until text is added
        loginBinding.editName1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0 || s.equals("")) {
                    loginBinding.loginButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // set listener on register button that navigates to the register screen
        loginBinding.registerButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(view).navigate(R.id.action_loginFragment2_to_registerFragment);
            }
        });
    }


    // using custom find function to validate username and password
    private void validate(String name, String pass) {
        cViewModel.findCustomerPlus(name, pass);
    }

    private void setUpObservers() {

        // get the updated customerList from Room framework and database
        cViewModel.getAllCustomers().observe(getViewLifecycleOwner(), new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                customerList = customers;
            }
        });


        // if the name+password combo matches a search, then customer is valid, so navigate to the list fragment
        cViewModel.getSearchResults().observe(getViewLifecycleOwner(), new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                if (customers.size() > 0 ) {

                    LoginFragmentDirections.ActionLoginFragment2ToListFragment action =
                            LoginFragmentDirections.actionLoginFragment2ToListFragment();

                    // we have a match in the list, it should be first and only result in the list
                    action.setCustomer1(customers.get(0));

                    Navigation.findNavController(view).navigate(action);
                }
                else {
                    Toast.makeText(context, "invalid username/password combo", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // clear the edit fields
    private void clearEditFields() {
        loginBinding.editName1.setText("");
        loginBinding.editPassword1.setText("");
    }

}
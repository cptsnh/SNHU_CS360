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
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.christrimmer.inventoryapp.databinding.FragmentAboutBinding;
import android.content.Context;

// The About fragment is used to provide instructions to the customer
// It also provides version number and developer info
public class AboutFragment extends Fragment {

    // Member variables
    FragmentAboutBinding aboutBinding;
    View view;
    Context context;
    Customer customer;

    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        // get reference to the customer from SafeArg passed from list fragment
        customer = AboutFragmentArgs.fromBundle(getArguments()).getCustomerAbout();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        aboutBinding = FragmentAboutBinding.inflate(inflater, container, false);
        view = aboutBinding.getRoot();
        context = view.getContext();

        // setup click listener on the done button
        // returns the customer to the list fragment, and passes customer object back
        aboutBinding.aboutDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AboutFragmentDirections.ActionAboutFragmentToListFragment action =
                        AboutFragmentDirections.actionAboutFragmentToListFragment();

                action.setCustomer1(customer);

                Navigation.findNavController(view).navigate(action);

            }
        });


        return view;
    }
}
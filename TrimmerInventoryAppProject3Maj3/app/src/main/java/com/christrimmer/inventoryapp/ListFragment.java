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

import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.resources.Compatibility;
import androidx.core.view.WindowCompat;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.Manifest;
import android.telephony.SmsManager;


import com.christrimmer.inventoryapp.databinding.FragmentListBinding;

import java.util.List;

public class ListFragment extends Fragment {

    private FragmentListBinding listBinding;
    private View view;
    private Context context;
    private Customer customer;
    private long mCustId;
    private ItemViewModel iViewModel;
    private ItemListAdapter itemListAdapter;
    private static final String TAG = "PermissionTest";
    private static final int SMS_REQUEST_CODE = 102;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        if (ListFragmentArgs.fromBundle(getArguments()).getCustomer1() != null)
            customer = ListFragmentArgs.fromBundle(getArguments()).getCustomer1();

        // call to setup the SMS permissions
        setUpSMSPermissions(getParentFragment().getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        listBinding = FragmentListBinding.inflate(inflater, container, false);
        listBinding.mainToolbar.inflateMenu(R.menu.appbar_menu);
        view = listBinding.getRoot();
        context = view.getContext();

        // associate this fragment with the item view model
        iViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        Toast.makeText(context, "Welcome, " + customer.getName(), Toast.LENGTH_SHORT).show();

        // helper functions
        setUpToolbar();
        setUplisteners();
        setUpObservers();
        setUpRecycler();
        setUpFab();

        // when we get an item returned from Add fragment, then insert the item into the database
        if (ListFragmentArgs.fromBundle(getArguments()).getItemAdd() != null) {
            Item item = ListFragmentArgs.fromBundle(getArguments()).getItemAdd();

            iViewModel.insertItem(item);
        }

        // when we get an item returned from Edit fragment, then update the item
        if (ListFragmentArgs.fromBundle(getArguments()).getItemEdit() != null ) {
            Item temp = ListFragmentArgs.fromBundle(getArguments()).getItemEdit();

            iViewModel.updateItem(temp);
        }

        return view;
    }

    // The floating action button is used for navigating to the Add fragment
    // Passes the customer object via SafeArg to the Add fragment
    private void setUpFab() {

        listBinding.listFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFragmentDirections.ActionListFragmentToAddItemFragment action =
                        ListFragmentDirections.actionListFragmentToAddItemFragment();

                action.setCustomerAddItem(customer);

                Navigation.findNavController(view).navigate(action);

            }
        });


    }

    private void setUpObservers() {

        // observer on the main list that is updated when changes are made to the list
        iViewModel.getAllItems().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                itemListAdapter.setItemList(items);

                mCustId = customer.getId();

                // This is used to seed inventory data in the app for testing purposes
//                Log.i("itemlistadaptercount", String.valueOf(itemListAdapter.getItemCount()));
//                if (itemListAdapter.getItemCount() == 0) {
//                    Item item1 = new Item(mCustId, "router", "cisco", 33);
//                    Item item2 = new Item(mCustId, "switch", "cisco", 23);
//                    Item item3 = new Item(mCustId, "cables", "cisco", 13);
//                    iViewModel.insertItem(item1);
//                    iViewModel.insertItem(item2);
//                    iViewModel.insertItem(item3);
//                }
            }
        });

        // observer on the search list when we perform a find operations
        iViewModel.getSearchResults().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                if (items.size() > 0 ) {
                    // itemListAdapter.setItemList(items);
                }
            }
        });

    }

    private void setUplisteners() {

    }

    private void setUpRecycler() {
//        itemListAdapter = new ItemListAdapter(R.layout.item_layout, iViewModel);
        itemListAdapter = new ItemListAdapter(R.layout.item_layout, iViewModel, customer);
        listBinding.itemRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        listBinding.itemRecycler.setAdapter(itemListAdapter);
    }

    private void setUpToolbar() {
        listBinding.mainToolbar.setOnMenuItemClickListener(item -> {

            // If customer taps the about icon, navigate to about screen
            if (item.getItemId() == R.id.action_about) {

                ListFragmentDirections.ActionListFragmentToAboutFragment action =
                        ListFragmentDirections.actionListFragmentToAboutFragment();

                action.setCustomerAbout(customer);
                Navigation.findNavController(view).navigate(action);

                return true;
            }

            // if customer taps the logout icon, go back to login screen
            if (item.getItemId() == R.id.action_done) {
                Navigation.findNavController(view).navigate(R.id.action_listFragment_to_loginFragment2);
                return true;
            }

            // if customer taps the sms icon, navigate to the privacy settings
            if (item.getItemId() == R.id.action_sms) {
                startActivity(new Intent(Settings.ACTION_PRIVACY_SETTINGS));
                return true;
            }

            return false;
        });
    }

    ///////////////////////////  SMS Functions //////////////////////////////


    // This function is called when an inventory item reaches quantity of zero
    public static void SendMessage(Context context) {

        // auto set the number to the phone number of the AVD emulator for testing
        // in real app, the phone number will be obtained from the customer phone field
        String testNumber = "smsto:+016505551212";
        try {
            SmsManager msg = SmsManager.getDefault();
            msg.sendTextMessage(testNumber, null, "Inventory is Low", null, null);
            Toast.makeText(context, "SMS Message: Inventory is low " + testNumber, Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex) {
            Toast.makeText(context, "SMS is not enabled", Toast.LENGTH_SHORT).show();
        }
    }

    // This function is use to setup SMS permissions
    // If customer denies the first request, they will given another notice
    // My customer SMS dialog will appear and explain why we need permission
    // If customer revokes perm request a second time, then it is permanent
    // The customer will need to enable SMS messaging from Privacy Settings
    private void setUpSMSPermissions(Context ctx) {
        int permission = ContextCompat.checkSelfPermission(ctx,
                Manifest.permission.SEND_SMS);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to send SMS message is denied");
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.SEND_SMS)) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ctx);

                // Use my custom SMS fragment layout
                builder.setView(R.layout.sms_layout);

                // Show another SMS request one more time to remind customer (one-time only)
                builder.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                makeRequestSMS();
                            }
                        });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // create instance of the dialog and use show() to display it
                AlertDialog dialog = builder.create();
                dialog.show();

            } else {

                // Show an SMS request screen to give customer option to accept or deny SMS messaging
                makeRequestSMS();
            }
        }
    }

    // Call this method to get the SMS permission request
    protected void makeRequestSMS() {
        ActivityCompat.requestPermissions(getParentFragment().getActivity(), new String[]{Manifest.permission.SEND_SMS},
                SMS_REQUEST_CODE);
    }

    private boolean checkSMSPermission(Context ctx, String perm) {
        int permCheck = ContextCompat.checkSelfPermission(ctx, perm);

        return (permCheck == PackageManager.PERMISSION_GRANTED);

    }



}
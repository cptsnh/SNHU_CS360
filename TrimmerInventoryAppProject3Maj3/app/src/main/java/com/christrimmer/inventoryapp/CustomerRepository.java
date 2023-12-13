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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Application;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * I have a repo for each model class (Item and Customer). The repo is used for obtaining
 * a database instance to access and make calls to the DAO's.
 * Note that the use of Asynchronous tasking includes using the ExecutorService.
 * Coding for asynch tasking inspired and attributed to learning from:
 * Android Studio 4.2 Development Essentials, Packt Publishing, by Neil Smyth
 */
public class CustomerRepository {

    private final MutableLiveData<List<Customer>> searchResults = new MutableLiveData<>();
    private List<Customer> customers;
    private final LiveData<List<Customer>> allCustomers;
    private final CustomerDAO customerDAO;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            searchResults.setValue(customers);
        }
    };


    public CustomerRepository(Application app) {
        InventoryDatabase db;
        db = InventoryDatabase.getInstance(app);
        customerDAO = db.getCustomerDAO();
        allCustomers = customerDAO.getAllCustomers();

    }

    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public MutableLiveData<List<Customer>> getSearchResults() {
        return searchResults;
    }

    // Perform the insert operation on a separate thread
    public void insertCustomer(Customer c) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(()->{
            customerDAO.insertCustomer(c);
        });

        executor.shutdown();
    }

    // We are not implementing delete operations on Customer class,
    // but add in the code for completeness
    public void deleteCustomer(String name) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(()->{
            customerDAO.deleteCustomer(name);
        });

        executor.shutdown();
    }

    // Perform find operation using separate thread - filter using customer name
    public void findCustomer(String name) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(()->{
            customers = customerDAO.findCustomer(name);
            handler.sendEmptyMessage(0);
        });
        executor.shutdown();
    }

    // Perform find operation using separate thread - filter on customer id
    public void findCustomer(long id) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(()-> {
            customers = customerDAO.findCustomer(id);
            handler.sendEmptyMessage(0);
        });
        executor.shutdown();
    }

    // Perform find operation using separate thread - filter on name and password
    public void findCustomerPlus(String name, String pass) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(()-> {
            customers = customerDAO.findCustomerPlus(name, pass);
            handler.sendEmptyMessage(0);
        });
        executor.shutdown();
    }

    // Testing variation of get customers (not used)
    public List<Customer> getCustomers(String name) {
        findCustomer(name);
        return customers;
    }



}

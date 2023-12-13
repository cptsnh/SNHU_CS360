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
public class ItemRepository {

    private final MutableLiveData<List<Item>> searchResults = new MutableLiveData<>();
    private List<Item> items;
    private final LiveData<List<Item>> allItems;
    private final ItemDAO itemDAO;

//    private final LiveData<List<Item>> allSearchItems;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            searchResults.setValue(items);
        }
    };


    public ItemRepository(Application app) {
        InventoryDatabase db;
        db = InventoryDatabase.getInstance(app);
        itemDAO = db.getItemDAO();
        allItems = itemDAO.getAllItems();

    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public MutableLiveData<List<Item>> getSearchResults() {
        return searchResults;
    }

    // Perform the insert operation on a separate thread
    public void insertItem(Item item) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(()->{
            itemDAO.insertItem(item);
        });

        executor.shutdown();
    }

    // Perform the update operation on a separate thread
    public void updateItem(Item item) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(()->{
            itemDAO.updateItem(item);
        });
    }

    // Perform the delete operation on a separate thread
    public void deleteItem(String name) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(()->{
            itemDAO.deleteItem(name);
        });

        executor.shutdown();
    }

    // Perform the find operation on a separate thread
    public void findItem(String name) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(()->{
            items = itemDAO.findItem(name);
            handler.sendEmptyMessage(0);
        });

        executor.shutdown();
    }

    public void findItemJoin(long id) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(()->{
            items = itemDAO.getAllItemsC(id);
            handler.sendEmptyMessage(0);
        });

        executor.shutdown();
    }



}

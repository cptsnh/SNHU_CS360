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

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;


/**
 * The ViewModel is the bridge between the UI and the underlying database. It is used
 * to keep data between the UI and database remains in synch. The functions maps the CRUD
 * operations to calls to the repo.
 *
 * Subclass to AndroidViewModel because we need to pass the App context to the repo.
 */
public class ItemViewModel extends AndroidViewModel {

    private ItemRepository repo;
    private LiveData<List<Item>> allItems;
    private MutableLiveData<List<Item>> searchResults;

    public ItemViewModel(@NonNull Application application) {
        super(application);

        repo = new ItemRepository(application);
        allItems = repo.getAllItems();
        searchResults = repo.getSearchResults();

    }


    MutableLiveData<List<Item>> getSearchResults() {
        return searchResults;
    }

    LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public void insertItem(Item item) {
        repo.insertItem(item);
    }

    public void updateItem(Item item) { repo.updateItem(item);}

    public void findItem(String name) {
        repo.findItem(name);
    }

    public void deleteItem(String name) {
        repo.deleteItem(name);
    }

    public void findItemJoin(long id) { repo.findItemJoin(id);}

}

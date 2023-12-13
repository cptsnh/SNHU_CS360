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

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * DAO's are an interface to map SQL statements using annotations.
 * These are called by the repo, which are executed on the backing SQLite database.
 * We implement the CRUD statements here.
 *
 * Note that we are only using insert and find on Customer entity for this app,
 * but update and delete are implemented for completeness.
 *
 */
@Dao
public interface CustomerDAO {

    @Insert
    void insertCustomer(Customer customer);

    @Update
    void updateCustomer(Customer customer);

    @Query("select * from customers where customerName = :name")
    List<Customer> findCustomer(String name);

    @Query("delete from customers where customerName = :name")
    void deleteCustomer(String name);

    @Query("select * from customers")
    LiveData<List<Customer>> getAllCustomers();

    @Query("select * from customers where customerName = :name and customerPass = :password")
    List<Customer> findCustomerPlus(String name, String password);

    @Query("select * from customers where customerId = :id")
    List<Customer> findCustomer(long id);


}

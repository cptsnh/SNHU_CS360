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


import java.io.Serializable;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;

// Class to model a Customer
// use serializable interface so that we can pass customer objects via SafeArgs
// use database/room/dao annotations to create table and set the db columns
@Entity(tableName = "customers")
public class Customer implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "customerId")
    private long id;

    @ColumnInfo(name = "customerName")
    @NonNull
    private String name;

    @ColumnInfo(name = "customerPass")
    @NonNull
    private String password;

    @ColumnInfo(name = "customerPhone")
    private String phoneNum;

    // Constructors
    @Ignore
    public Customer() { }

    @Ignore
    public Customer(String name) {
        this.name = name;
        this.password = "test";
        this.phoneNum = "6165551212";
    }

    public Customer(String name, String password) {
        this.name = name;
        this.password = password;
        this.phoneNum = "6165551212";
    }

    public Customer(String name, String password, String phone) {
        this.name = name;
        this.password = password;
        this.phoneNum = phone;
    }

    // setter and getter methods
    public long getId() { return this.id; }
    public void setId(long val) { this.id = val;}

    public void setName(String n) { this.name = n; }
    public String getName() { return this.name; }

    public void setPassword(String p) { this.password = p; }
    public String getPassword() { return this.password; }

    public void setPhoneNum(String p) { this.phoneNum = p; }
    public String getPhoneNum() { return this.phoneNum; }

}

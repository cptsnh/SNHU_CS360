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
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import androidx.room.Relation;

// Class to model an Item
// use serializable interface so that we can pass item objects via SafeArgs
// use database/room/dao annotations to create table and set the db columns
@Entity(tableName = "items", foreignKeys = @ForeignKey(entity = Customer.class,
        parentColumns = "customerId",
        childColumns = "customerId"))
public class Item implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "itemId")
    private long id;

//    @Relation(entity = Customer.class, parentColumn = "id", entityColumn = "customerId")
    @NonNull
    @ColumnInfo(name = "customerId")
    private long customerId;

    @NonNull
    @ColumnInfo(name = "itemName")
    private String name;

    @ColumnInfo(name = "itemDesc")
    private String description;

    @NonNull
    @ColumnInfo(name = "itemQty")
    private int quantity;

    // comeback to these on final
    // constructors
//    public Item(long id, long customerId, String name, String description, int quantity) {
//        this.id = id;
//        this.customerId = customerId;
//        this.name = name;
//        this.quantity = quantity;
//        this.description = description;
//    }

    public Item() {}

    public Item(String name, String desc, int qty) {
        this.name = name;
        this.description = desc;
        this.quantity = qty;
    }

    public Item(long custId, String name, String desc, int qty) {
        this.customerId = custId;
        this.name = name;
        this.description = desc;
        this.quantity = qty;
    }

    // comeback to these on final
//    @Ignore
//    public Item(String name, String description, int quantity) {
//        this(0, 0, name, description, quantity);
//    }

//    public Item(String name, int quantity) {
//        this(0, 0, name, "unknown", quantity);
//    }

//    @Ignore
//    public Item(String name) {
//        this(0, 0, name, "unknown", 0);
//    }


    // Getters and Setters
    public long getId() {
        return id;
    }
    public void setId(long _id) {
        this.id = _id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void incrementQuantity() { this.quantity += 1; }
    public void decrementQuantity() { this.quantity -= 1; }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}

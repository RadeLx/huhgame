package com.example.huhgame;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.List;

@Entity
public class User {
    @PrimaryKey
    public int id;
    public int huh , cat , current, take_careprice;
    public Cat cats[];
    public boolean loan, die;

    public User(int huh, int cat, int current, boolean loan, boolean die) {
        this.id = 0;
        this.huh = huh;
        this.cat = cat;
        this.current = current;
        this.loan = loan;
        this.die = die;
    }
}


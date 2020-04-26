package com.solar.pinterest.solarmobile.storage;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DBSchema {
    @Dao
    public interface UserDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(User user);

        @Delete
        void delete(User user);

        @Query("SELECT * FROM user WHERE id = :id")
        User getUser(int id);

        @Update
        void updateUser(User user);
    }

    @Entity
    public static class User {
        @PrimaryKey private int id;
        private String username;
        private String name;
        private String surname;
        private String email;
        private int age;
        private String status;
        private String avatar;
        private boolean active;
        @TypeConverters({TimestampConverter.class})
        private Date created;
        private boolean subscribed;


        public User(int id, String username, String name, String surname, String email, int age, String status, String avatar, boolean active, Date created, boolean subscribed) {
            this.id = id;
            this.username = username;
            this.name = name;
            this.surname = surname;
            this.email = email;
            this.age = age;
            this.status = status;
            this.avatar = avatar;
            this.active = active;
            this.created = created;
            this.subscribed = subscribed;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public Date getCreated() {
            return created;
        }

        public void setCreated(Date created) {
            this.created = created;
        }

        public boolean isSubscribed() {
            return subscribed;
        }

        public void setSubscribed(boolean subscribed) {
            this.subscribed = subscribed;
        }
    }

    @Dao
    public interface PinDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Pin pin);

        @Query("SELECT * FROM Pin where id = :id")
        Pin getPin(int id);

        @Delete
        void delete(Pin pin);
    }

    @Entity
    public static class Pin {
        @PrimaryKey private int id;
        private String authorUsername;
        private String ownerUsername;
        private int boardId;
        @TypeConverters({TimestampConverter.class})
        private Date created;
        private String path;
        private String title;
        private String description;
        private boolean deleted;

        public Pin(int id, String authorUsername, String ownerUsername, int boardId, Date created, String path, String title, String description, boolean deleted) {
            this.id = id;
            this.authorUsername = authorUsername;
            this.ownerUsername = ownerUsername;
            this.boardId = boardId;
            this.created = created;
            this.path = path;
            this.title = title;
            this.description = description;
            this.deleted = deleted;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAuthorUsername() {
            return authorUsername;
        }

        public void setAuthorUsername(String authorUsername) {
            this.authorUsername = authorUsername;
        }

        public String getOwnerUsername() {
            return ownerUsername;
        }

        public void setOwnerUsername(String ownerUsername) {
            this.ownerUsername = ownerUsername;
        }

        public int getBoardId() {
            return boardId;
        }

        public void setBoardId(int boardId) {
            this.boardId = boardId;
        }

        public Date getCreated() {
            return created;
        }

        public void setCreated(Date created) {
            this.created = created;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }
    }

    public static class TimestampConverter {
        static final String sDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SS'Z'";
        @TypeConverter
        public String fromDate(Date timestamp) {
            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat(sDateFormat);
            return df.format(timestamp);
        }

        @TypeConverter
        public Date toDate(String timestamp) {
            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat(sDateFormat);
            try {
                return df.parse(timestamp);
            } catch (ParseException e) {
                Log.e("Solar", "Cannot parse date");
                return new Date();
            }
        }
    }
}

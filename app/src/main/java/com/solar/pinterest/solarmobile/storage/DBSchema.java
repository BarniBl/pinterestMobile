package com.solar.pinterest.solarmobile.storage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

public class DBSchema {
    @Dao
    public interface UserDao {
        @Insert
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
        private String created;


        public User(int id, String username, String name, String surname, String email, int age, String status, String avatar, boolean active, String created) {
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

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }
}

package com.solar.pinterest.solarmobile;

import android.content.ContentValues;
import android.database.Cursor;

public class DBSchema {
    public static final class UserTable {
        public static final String NAME = "user";

        public static final class Cols {
            public static final String ID = "id";
            public static final String USERNAME = "nick";
            public static final String NAME = "name";
            public static final String SURNAME = "surname";
            public static final String EMAIL = "email";
            public static final String AGE = "age";
            public static final String STATUS = "status";
            public static final String AVATAR = "avatar";
            public static final String IS_ACTIVE = "active";
            public static final String CREATED = "created";

            private static final String SCHEMA = "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT NOT NULL," +
                        "%s INTEGER," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s INTEGER," +
                        "%s TEXT" +
                    ");";
        }

        public static String getCreateQuery() {
            return String.format(Cols.SCHEMA, NAME, Cols.ID, Cols.USERNAME, Cols.NAME,
                    Cols.SURNAME, Cols.EMAIL, Cols.AGE, Cols.STATUS, Cols.AVATAR,
                    Cols.IS_ACTIVE, Cols.CREATED);
        }

        public static ContentValues getContentValues(User user) {
            ContentValues values = new ContentValues();
            values.put(Cols.ID, user.getId());
            values.put(Cols.USERNAME, user.getUsername());
            values.put(Cols.NAME, user.getName());
            values.put(Cols.SURNAME, user.getSurname());
            values.put(Cols.EMAIL, user.getEmail());
            values.put(Cols.AGE, user.getAge());
            values.put(Cols.STATUS, user.getStatus());
            values.put(Cols.AVATAR, user.getAvatar());
            values.put(Cols.IS_ACTIVE, user.isActive());
            values.put(Cols.CREATED, user.getCreated());
            return values;
        }

        public static User inflateFromCursor(Cursor cursor) {
            return new User(
                    cursor.getInt(0),       // id
                    cursor.getString(1),    // username
                    cursor.getString(2),       // name
                    cursor.getString(3),       // surname
                    cursor.getString(4),       // email
                    cursor.getInt(5),       // age
                    cursor.getString(6),       // status
                    cursor.getString(7),       // avatar
                    cursor.getInt(8) != 0,       // active
                    cursor.getString(9)      // created
            );
        }

    }

    public static class User {
        private int id;
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

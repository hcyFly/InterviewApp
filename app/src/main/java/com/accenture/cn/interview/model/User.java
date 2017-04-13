package com.accenture.cn.interview.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chengyou.huang on 2017/2/17.
 */

public class User implements Parcelable {

//    "code": 0,
//            "message": "成功!",
//            "result": {
//        "auz": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaS5kYSIsImlkIjo0LCJleHAiOjE0ODk5MTUzNjAsImlhdCI6MTQ4NzMyMzM2MCwicm9sZXMiOlsibGkuZGEiLCJlYWJkOGNlOTQwNDUwN2FhOGMyMjcxNGQzZjVlYWRhOSJdfQ.MMlVdjJ6bz58kZHSmG-ViAdzrl8MxOMRL3xldhFkKW8",
//                "email": "li.da@accenture.com",
//                "id": 4,
//                "phone": "120120120",
//                "type": "1",
//                "usereid": "li.da",
//                "username": "li.da"

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private int code;
    private String message;
    private LoginResult result;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginResult getResult() {
        return result;
    }

    public void setResult(LoginResult result) {
        this.result = result;
    }

    /**
     * 登录返回结果
     * Created by chengyou.huang on 2017/2/20.
     */

    public static class LoginResult implements Parcelable {

//    "auz": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaS5kYSIsImlkIjo0LCJleHAiOjE0ODk5MTUzNjAsImlhdCI6MTQ4NzMyMzM2MCwicm9sZXMiOlsibGkuZGEiLCJlYWJkOGNlOTQwNDUwN2FhOGMyMjcxNGQzZjVlYWRhOSJdfQ.MMlVdjJ6bz58kZHSmG-ViAdzrl8MxOMRL3xldhFkKW8",
//                "email": "li.da@accenture.com",
//                "id": 4,
//                "phone": "120120120",
//                "type": "1",
//                "usereid": "li.da",
//                "username": "li.da"

//        id
//                name
//        usereid
//                password
//        email
//                auz
//        phone
//                type


        private String auz;
        private String email;
        private int id;
        private String phone;
        private String type;
        private String usereid;
        private String username;

        public String getAuz() {
            return auz;
        }

        public void setAuz(String auz) {
            this.auz = auz;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUsereid() {
            return usereid;
        }

        public void setUsereid(String usereid) {
            this.usereid = usereid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.auz);
            dest.writeString(this.email);
            dest.writeInt(this.id);
            dest.writeString(this.phone);
            dest.writeString(this.type);
            dest.writeString(this.usereid);
            dest.writeString(this.username);
        }

        public LoginResult() {
        }

        protected LoginResult(Parcel in) {
            this.auz = in.readString();
            this.email = in.readString();
            this.id = in.readInt();
            this.phone = in.readString();
            this.type = in.readString();
            this.usereid = in.readString();
            this.username = in.readString();
        }

        public static final Parcelable.Creator<LoginResult> CREATOR = new Parcelable.Creator<LoginResult>() {
            @Override
            public LoginResult createFromParcel(Parcel source) {
                return new LoginResult(source);
            }

            @Override
            public LoginResult[] newArray(int size) {
                return new LoginResult[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.message);
        dest.writeParcelable(this.result, flags);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.code = in.readInt();
        this.message = in.readString();
        this.result = in.readParcelable(LoginResult.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

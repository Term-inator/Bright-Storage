package com.example.bright_storage.util;

import com.example.bright_storage.model.dto.UserDTO;

public class SecurityUtil {

    private static UserDTO currentUser;

    public static UserDTO getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserDTO user){
        currentUser = user;
    }

    public static void removeCurrentUser(){
        currentUser = null;
    }

    public static boolean isLogin(){
        return currentUser != null;
    }

    public static String getCurrentUserId(){
        return currentUser == null ? null : currentUser.getId();
    }

}

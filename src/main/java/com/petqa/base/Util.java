package com.petqa.base;

import com.petqa.domain.User;
import com.petqa.dto.user.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.context.SecurityContextHolder;

public class Util {

    public static Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    public static User getCurrentUser() {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUser();
    }

}


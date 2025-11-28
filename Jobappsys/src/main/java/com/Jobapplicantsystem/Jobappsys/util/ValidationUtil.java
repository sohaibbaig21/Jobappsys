package com.Jobapplicantsystem.Jobappsys.util;

public class ValidationUtil {

    // Check if string is null or empty
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // Validate email format
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Validate password strength (you can adjust)
    public static boolean isStrongPassword(String password) {
        if (isEmpty(password)) return false;

        // Minimum 8 chars, 1 digit, 1 lowercase, 1 uppercase
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    }

    // Validate numeric positive value
    public static boolean isPositiveNumber(Number num) {
        return num != null && num.doubleValue() > 0;
    }

    // Validate salary or any number >= 0
    public static boolean isNonNegative(Number num) {
        return num != null && num.doubleValue() >= 0;
    }
}

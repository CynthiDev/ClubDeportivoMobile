package com.example.clubdeportivo.helpers;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String formatDateString(String originalDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat desiredFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date = originalFormat.parse(originalDate);
            return desiredFormat.format(date);
        } catch (ParseException e) {
            return null; // or throw an exception, depending on your requirements
        }
    }
}

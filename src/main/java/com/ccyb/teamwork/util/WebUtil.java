package com.ccyb.teamwork.util;



import com.ccyb.teamwork.exception.DataValidationFailedException;

import java.util.ArrayList;
import java.util.List;

public class WebUtil {


    public static <T> List<T> convertStringToList(String str, Class<T> clazz) {
        List<T> tList = new ArrayList<>();
        try {
            if (str != null && !str.isBlank()) {
                String[] strings = str.split(",");
                for (String string : strings) {
                    if (Integer.class.equals(clazz)) {
                        tList.add((T) Integer.valueOf(string));
                    }
                    if (Long.class.equals(clazz)) {
                        tList.add((T) Long.valueOf(string));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataValidationFailedException();
        }
        return tList;
    }
}

package com.ccyb.teamwork.util;


import com.ccyb.teamwork.exception.DataValidationFailedException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    //    List<String>转List<Long>
    public static List<Long> convertStringToLong(List<String> stringList) {
        if (null == stringList || stringList.isEmpty()) {
            return null;
        } else {
            return stringList.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        }
    }
}

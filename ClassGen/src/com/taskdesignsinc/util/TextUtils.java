package com.taskdesignsinc.util;

/**
 * Created by salma on 6/29/2017.
 */
public class TextUtils {

    private TextUtils() {
    }

    public static boolean isEmpty(String text) {
        return (text == null || text.length() == 0);
    }

    /**
     * https://stackoverflow.com/questions/5725892/how-to-capitalize-the-first-letter-of-word-in-a-string-using-java
     * By Anther
     *
     * @param originalWord
     * @return Word with first letter capitalized
     */
    public static String capitalizeFirstLetter(String originalWord) {
        if (originalWord == null || originalWord.length() == 0) {
            return originalWord;
        }
        return originalWord.substring(0, 1).toUpperCase() + originalWord.substring(1);
    }
}

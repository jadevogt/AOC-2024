package com.expedient.adventofcodejade.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTools {
    /**
     * Converts a given string to Character[]
     * @param s the string to be converted
     * @return an array of Character[] from String s
     */
   public static Character[] ToCharacterArray(String s) {
       var primitiveCharArray = s.toCharArray();
       Character[] chars = new Character[primitiveCharArray.length];
       for (int i = 0; i < primitiveCharArray.length; i++) {
           chars[i] = primitiveCharArray[i];
       }
       return chars;
   }

    /**
     * Convenience method to quickly get a regex Matcher for a given String and pattern
     * @param s string to match on
     * @param regexPattern regex pattern
     * @return Matcher object ready for use
     */
   public static Matcher getMatcher(String s, String regexPattern) {
       Pattern p = Pattern.compile(regexPattern);
       return p.matcher(s);
   }
}

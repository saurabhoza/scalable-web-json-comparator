package com.assingment.scalableweb.util;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {
	 
	private static ObjectMapper mapper = new ObjectMapper();
	
    public static String convertObjectToJsonBytes(Object object) throws IOException {
    	System.out.println("****************** : " + mapper.writeValueAsString(object));
        return mapper.writeValueAsString(object);
    }
 
    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();
 
        for (int index = 0; index < length; index++) {
            builder.append("a");
        }
 
        return builder.toString();
    }
    
    public static void main(String[] args) {
    	

    	String stringToBeChecked = "dfgkjkjhkgsuhmnb^&^*&^#*@^";
    	Base64.Decoder dec = Base64.getDecoder();
        byte[] decbytes = dec.decode(stringToBeChecked);
	}
}

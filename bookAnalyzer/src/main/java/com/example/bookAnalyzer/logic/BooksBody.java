package com.example.bookAnalyzer.logic;

import java.io.InputStream;

public class BooksBody {
    private static byte[] bytes;

    String filePath = "/books/Baldachchi_Wish-You-Well_RuLit_Me.txt";

    public void makeArrayChars() {
        InputStream in = getClass().getResourceAsStream(filePath);
        byte[] bytes = null;
        try {
            bytes = in.readAllBytes();
        }catch (Exception ignored){}
        System.out.println(bytes.length);

        System.out.println(bytes.toString().toCharArray().length);
    }


}

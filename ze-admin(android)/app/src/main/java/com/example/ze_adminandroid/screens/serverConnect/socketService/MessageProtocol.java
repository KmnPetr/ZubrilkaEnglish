package com.example.ze_adminandroid.screens.serverConnect.socketService;

import java.nio.ByteBuffer;

/**
 * класс занимается сборкой/расборкой сообщения обьекта передаваемого по webSocket
 * первые 8 байт сообщения массива - лонговое число, указывающее количество байт следующего за ним поля headers
 * headers - json строка, Map() содержащая различные проперти в любом количестве
 * оставшиеся байты - body с любым количеством байт
 */
public class MessageProtocol {
    public MessageProtocol(){
        Long l = 32467890L;
        System.out.println("init: "+ l);
        System.out.println(Long.toBinaryString(l));

        byte[] b = longToByte(l);
        System.out.println("size array: "+b.length);
        for (int i = 0; i < b.length; i++) {
            String binaryString = String.format("%8s", Integer.toBinaryString(b[i] & 0xFF)).replace(' ', '0');
            System.out.println(binaryString);
        }

        Long finalLong = byteToLong(b);
        System.out.println("final long: "+ finalLong);
        System.out.println(Long.toBinaryString(finalLong));
    }

    /**
     * переведет лонг в массив
     */
    private byte[] longToByte(Long l){
        return ByteBuffer.allocate(8).putLong(l).array();
    }

    /**
     * переведет первые 8 байт массива в long
     */
    private Long byteToLong(byte[] bytes){
        long l = 0;
        for (byte b : bytes) {
            l = (l << 8) + (b & 0xFF);
        }
        return l;
    }
}

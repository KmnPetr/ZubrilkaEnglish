package com.example.ze_adminandroid.screens.serverConnect.socketService;

import com.google.gson.Gson;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * класс занимается сборкой/расборкой сообщения обьекта передаваемого по webSocket
 * первые 8 байт сообщения массива - лонговое число, указывающее количество байт следующего за ним поля headers
 * headers - json строка, Map() содержащая различные проперти в любом количестве
 * оставшиеся байты - body с любым количеством байт
 */
public class MessageProtocol {
    public MessageProtocol(){
        long sizeHeaders;
        Map<String,String> headers = new HashMap<>();
        headers.put("type","ping");
        headers.put("some field", "value some field");

        byte[] body = new byte[300];
        for (byte b : body) {
            b = (byte) new Random().nextInt(256);
        }

        byte[] message = buildMessage(headers,body);

        String str = new String(message, StandardCharsets.UTF_8);
        System.out.println(str);

        Map<String,String> finalHeaders = getHeaders(message);
        System.out.println("//////////////////////////");
        System.out.println(finalHeaders.get("type"));
        System.out.println(finalHeaders.get("some field"));
        System.out.println("//////////////////////////");

    }

    /**
     * вычленит из переданного message заголовки
     */
    public Map<String, String> getHeaders(byte[] message) {
        byte[] sizeHeadersB = new byte[4];
        //копируем первых 4 байта в которых записан размер заголовка
        for (int i = 0; i < sizeHeadersB.length; i++) {
            sizeHeadersB[i] = message[i];
        }
        int sizeHeaders = byteToInt(sizeHeadersB);

        byte[] headersB = new byte[sizeHeaders];
        for (int i = 0; i < headersB.length; i++) {
            headersB[i] = message[i+4];
        }

        System.out.println(new String(headersB, StandardCharsets.UTF_8));
        Map<String,String> headers = stringJsonToMap(new String(headersB, StandardCharsets.UTF_8));

        return headers;
    }

    /**
     * построит обьект byte[] message для отправки по вэбсокету
     * @param headers мапа с произвольным количеством заголовком, желательно адекватно читаемым на другом конце сокет соединения
     * @param body содержит любой обьект, чьи характеристики указаны в headers, если обьекта нет, можно передать null
     */
    private byte[] buildMessage(Map<String, String> headers, byte[] body) {
        //задаем начальные массивы
        byte[] sizeHeadersB;
        byte[] headersB = mapToJson(headers).getBytes();
        sizeHeadersB = intToByte(headersB.length);
        int sizeBody = body!=null ? body.length : 0;

        int sizeMessage = sizeHeadersB.length + headersB.length + sizeBody;

        byte[] message = new byte[sizeMessage];
        //складываем все массивы в один
        int pos = 0;
        for (int i = 0; i < sizeHeadersB.length; i++) {
            message[pos] = sizeHeadersB[i];
            pos++;
        }
        for (int i = 0; i < headersB.length; i++) {
            message[pos] = headersB[i];
            pos++;
        }
        if (body!=null){
            for (int i = 0; i < body.length; i++) {
                message[pos] = body[i];
                pos++;
            }
        }
        return message;
    }


    /**
     * конвертирует строку json в мапу
     */
    private Map<String,String> stringJsonToMap(String property) {
        return new Gson().fromJson(property, Map.class);
    }

    /**
     * конвертирует строку мапу в json
     */
    private String mapToJson(Map<String,String> map){
        return new Gson().toJson(map);
    }


    /**
     * переведет лонг в массив
     */
    private byte[] intToByte(int l){
        return ByteBuffer.allocate(4).putInt(l).array();
    }

    /**
     * переведет первые 8 байт массива в long
     */
    private int byteToInt(byte[] bytes){
        int l = 0;
        for (int i = 0; i < 4; i++) {
            l = (l << 8) + (bytes[i] & 0xFF);
        }
        return l;
    }
}

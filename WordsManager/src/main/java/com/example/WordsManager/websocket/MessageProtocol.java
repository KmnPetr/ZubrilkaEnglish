package com.example.WordsManager.websocket;

import com.google.gson.Gson;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * класс занимается сборкой/расборкой сообщения обьекта передаваемого по webSocket
 * первые 8 байт сообщения массива - лонговое число, указывающее количество байт следующего за ним поля headers
 * headers - json строка, Map() содержащая различные проперти в любом количестве
 * оставшиеся байты - body с любым количеством байт
 */

public class MessageProtocol {
    private byte[] message;
    private TypeEnum type;
    private Map<String,String> headers;
    private byte[] body;
    public MessageProtocol(byte[] message){
        this.message = message;
        this.headers = getHeaders(message);
        this.type = TypeEnum.fromString(headers.get("type"));
        this.body = getBody(message);
    }
    public MessageProtocol(TypeEnum type,Map<String,String> headers, byte[] body){
        this.type = type;
        this.headers = headers;
        this.body = body;

        if (headers == null){
            this.headers = new HashMap<>();
        }
        this.headers.put("type", type.getValue());

        this.message = buildMessage(this.headers,body);
    }

    public byte[] getMessage() {return message;}
    public TypeEnum getType() {return type;}
    public Map<String, String> getHeaders() {return headers;}
    public byte[] getBody() {return body;}

    /**
     * вычленит body из message
     */
    private byte[] getBody(byte[] message) {
        int headSize = byteToInt(message) + 4;
        int sizeOfBody = message.length - headSize;
        byte[] body = null;
        if(sizeOfBody>0){
            body = new byte[sizeOfBody];
            for (int i = 0; i < sizeOfBody; i++) {
                body[i] = message[headSize+i];
            }
        }
        return body;
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
     * переведет первые 4 байтa массива в int
     */
    private int byteToInt(byte[] bytes){
        int l = 0;
        for (int i = 0; i < 4; i++) {
            l = (l << 8) + (bytes[i] & 0xFF);
        }
        return l;
    }
}
enum TypeEnum {
    PING("ping"),
    VOICE("voice"),
    SUCCESSFUL_VOICE_SAVING("successful voice saving"),
    VOICE_ERROR("voice error"),
    WORD("word"),
    SUCCESSFUL_WORD_SAVING("successful word saving");

    private String value;

    private TypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public static TypeEnum fromString(String type) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.value.equals(type)) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("the enam is not defined for the value \""+type+"\"");
    }
}

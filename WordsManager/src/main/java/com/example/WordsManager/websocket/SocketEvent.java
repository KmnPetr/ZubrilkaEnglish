package com.example.WordsManager.websocket;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SocketEvent {
    private String type;
    private String ping;
}

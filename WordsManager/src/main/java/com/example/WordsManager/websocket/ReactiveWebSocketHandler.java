package com.example.WordsManager.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

@Component
@Slf4j
public class ReactiveWebSocketHandler implements WebSocketHandler {

    private final ReceivedDataManager receivedDataManager;

    @Autowired
    public ReactiveWebSocketHandler(ReceivedDataManager receivedDataManager) {
        this.receivedDataManager = receivedDataManager;
    }


    @Override
    public Mono<Void> handle(WebSocketSession session) {

        Mono<Void> input = session.receive()
                .doOnNext(webSocketMessage -> {
                    // извлекаем байты правильно
                    DataBuffer payload = webSocketMessage.getPayload();
                    byte[] bytes = new byte[payload.readableByteCount()];//ok
                    payload.read(bytes);
                    //отправляем на дальнейшую обработку
                    receivedDataManager.processMessage(bytes);
                })
                .doOnError(e->{
                    e.printStackTrace();
                })
                .then();

        Flux<String> source = receivedDataManager.getEmitterProcessor().asFlux();
        Mono<Void> output = session.send(source.map(session::textMessage));

        return Mono.zip(input, output).then(); //закроет сессию если потоки или один поток (так я и не понял) завершатся
    }
}
package com.example.zeapp.onlineCompetition;

import com.example.zeapp.models.SocketMessage;
import com.example.zeapp.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Objects;


@Component
@Slf4j
public class CompetitionWebSocketHandler implements WebSocketHandler {

    private final JwtUtil jwtUtil;
    private final PlayerHolder playerHolder;
    private final CompetitionManager competitionManager;
    @Autowired
    public CompetitionWebSocketHandler(JwtUtil jwtUtil, PlayerHolder playerHolder, CompetitionManager competitionManager) {
        this.jwtUtil = jwtUtil;
        this.playerHolder = playerHolder;
        this.competitionManager = competitionManager;
    }


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        //к сожалению не работает
        //session.getHandshakeInfo().getPrincipal().doOnNext(it-> System.out.println("Principal "+it.getName()+" "+it.toString()+" "+it.hashCode()));

        //получаем id авторизованного пользователя
        String jwtToken = Objects.requireNonNull(session.getHandshakeInfo().getHeaders().get("authorization")).get(0).substring(7);
        String personId = jwtUtil.getClaimsFromToken(jwtToken).get("personId");
        //создаем синк
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
        //направляем поток синка пользователю
        Mono<Void> send = session.send(sink.asFlux().map(session::textMessage));
        //сохраняем синк
        playerHolder.saveSink(Long.parseLong(personId),sink);
        //направляем полученные мессаджи в менеджер
        Flux<WebSocketMessage> receive = session.receive()
                .doOnNext(message -> competitionManager.receiveMessage(Long.parseLong(personId), SocketMessage.fromJson(message.getPayloadAsText())));


//        System.out.println("A connection has been established with a user with id="+personId);

        return Flux.merge(receive,send)
                .then()
                .doFinally(signalType -> {
//                    System.out.println("Lost connection with user with id="+personId);
                    playerHolder.onCloseSession(Long.parseLong(personId));
                });
    }
}
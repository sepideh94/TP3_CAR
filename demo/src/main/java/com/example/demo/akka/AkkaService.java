package com.example.demo.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AkkaService {

    private final ActorSystem system;
    private ActorRef[] mappers;
    private ActorRef[] reducers;

    public AkkaService() {
        this.system = ActorSystem.create("MySystem");
    }

    public void initActors() {
        reducers = new ActorRef[2];
        for (int i = 0; i < 2; i++) {
            reducers[i] = system.actorOf(Props.create(ReducerActor.class), "reducer" + i);
        }

        mappers = new ActorRef[3];
        for (int i = 0; i < 3; i++) {
            mappers[i] = system.actorOf(Props.create(MapperActor.class, () -> new MapperActor(reducers)), "mapper" + i);
        }

        System.out.println("Les acteurs initialisés.");
    }

    public void uploadFile(MultipartFile file) throws IOException {
        ActorRef[] mappers = getMappers();
        int[] index = {0};

        new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
            .lines()
            .forEach(line -> {
                mappers[index[0] % mappers.length].tell(line, ActorRef.noSender());
                index[0]++;
            });
        System.out.println("Fichier reçu");
    }

    public Map<String, Integer> wordCount() throws Exception {
        Map<String, Integer> wordCounts = new HashMap<>();

        for (ActorRef reducer : reducers) {
            Future<Object> future = Patterns.ask(reducer, "result", 10000);
            ResponseMessage response = (ResponseMessage) Await.result(
                    future, FiniteDuration.create(10, TimeUnit.SECONDS)
            );

            for (Map.Entry<String, Integer> entry : response.wordCounts().entrySet()) {
                wordCounts.put(entry.getKey(), wordCounts.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
        return wordCounts;
    }

    public ActorSystem getSystem() {
        return system;
    }

    public ActorRef[] getMappers() {
        return mappers;
    }

    public ActorRef[] getReducers() {
        return reducers;
    }
}

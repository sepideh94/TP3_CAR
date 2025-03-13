package com.example.demo.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.springframework.stereotype.Service;

@Service
public class AkkaService {

    private final ActorSystem system;
    private ActorRef[] mappers;
    private ActorRef[] reducers;

    public AkkaService() {
        this.system = ActorSystem.create("MySystem");
    }

    public void initActors() {
    	
        mappers = new ActorRef[3];
        for (int i = 0; i < 3; i++) {
            mappers[i] = system.actorOf(Props.create(MapperActor.class), "mapper" + i);
        }

        reducers = new ActorRef[2];
        for (int i = 0; i < 2; i++) {
            reducers[i] = system.actorOf(Props.create(ReducerActor.class), "reducer" + i);
        }
        
        System.out.println("Les acteurs initialisés");

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

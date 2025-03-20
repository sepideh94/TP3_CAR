package com.example.demo.akka;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class MapperActor extends UntypedActor {

    private final ActorRef[] reducers;

    public MapperActor(ActorRef[] reducers) {
        this.reducers = reducers;
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof String line) {
            String[] words = line.split("\\s+"); 
            for (String word : words) {
                if (!word.isEmpty()) { 
                    ActorRef reducer = partition(word);
                    reducer.tell(new RequestMessage(word), getSelf());  
                }
            }
        } else {
            unhandled(message);
        }
    }

    private ActorRef partition(String word) {
        int index = Math.abs(word.hashCode()) % reducers.length;
        return reducers[index];
    }
}

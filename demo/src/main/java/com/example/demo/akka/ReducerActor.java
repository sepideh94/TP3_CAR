package com.example.demo.akka;

import akka.actor.UntypedActor;
import java.util.HashMap;
import java.util.Map;

public class ReducerActor extends UntypedActor {

    private final Map<String, Integer> wordCounts = new HashMap<>();

    @Override
    public void onReceive(Object message) {
        if (message instanceof RequestMessage request) {
            wordCounts.put(request.word(), wordCounts.getOrDefault(request.word(), 0) + 1);
        } else if (message.equals("result")) { 
            getSender().tell(new ResponseMessage(new HashMap<>(wordCounts)), getSelf());
        } else {
            unhandled(message);
        }
    }
}

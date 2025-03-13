package com.example.demo.akka;

import akka.actor.UntypedActor;

public class ReducerActor extends UntypedActor {

    @Override
    public void onReceive(Object message) {
        if (message instanceof String m) {
            System.out.println("Reducer received: " + m);
        } else {
            unhandled(message);
        }
    }
}

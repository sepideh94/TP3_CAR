package com.example.demo.akka;

import akka.actor.UntypedActor;

public class MapperActor extends UntypedActor {

    @Override
    public void onReceive(Object message) {
        if (message instanceof String m) {
            System.out.println("Mapper received: " + m);
        } else {
            unhandled(message);
        }
    }
}

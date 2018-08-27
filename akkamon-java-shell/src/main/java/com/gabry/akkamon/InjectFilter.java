package com.gabry.akkamon;

import akka.actor.ActorRef;

/**
 * Created by gabry on 2018/8/27 17:47
 */
public class InjectFilter {
    public static boolean inject(ActorRef actorRef) {
        return ! (actorRef.path().name().equals("InjectActor") && actorRef.path().parent().name().equals("system"));
    }
}

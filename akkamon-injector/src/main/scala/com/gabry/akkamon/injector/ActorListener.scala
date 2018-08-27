package com.gabry.akkamon.injector

import akka.actor.ActorRef
import com.gabry.akkamon.listener.ActorSystemInstrumentationListener
import com.gabry.akkamon.protocol.InjectMessage

/**
  * Created by gabry on 2018/8/27 14:56
  */
class ActorListener(actorRef:ActorRef) extends ActorSystemInstrumentationListener{
  override def onMessage(message: InjectMessage): Unit = {
    actorRef ! message
  }

  override def close(): Unit = {

  }
}

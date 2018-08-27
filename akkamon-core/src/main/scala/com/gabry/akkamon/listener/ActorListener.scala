package com.gabry.akkamon.listener

import akka.actor.ActorRef
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

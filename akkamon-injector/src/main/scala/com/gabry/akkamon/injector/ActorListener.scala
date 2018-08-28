package com.gabry.akkamon.injector

import akka.actor.ActorRef
import com.gabry.akkamon.listener.ActorSystemInstrumentationListener
import com.gabry.akkamon.protocol.InjectMessage
import com.gabry.akkamon.protocol.InjectMessage.{ActorTerminated, MessageReceived, MessageSent, MessageTold}

/**
  * Created by gabry on 2018/8/27 14:56
  */
class ActorListener(notifyActor:ActorRef) extends ActorSystemInstrumentationListener{

  override def onMessage(message: InjectMessage): Unit = {
    notifyActor ! message
  }

  override def postClose(): Unit = {
  }

  override def filter(message: InjectMessage): Boolean = message match {
      case MessageReceived(_,receiver,_) if receiver == notifyActor =>false
      case MessageSent(_,receiver,_) if receiver == notifyActor =>false
      case MessageTold(receiver,_,sender) if receiver == notifyActor || sender == notifyActor => false
      case ActorTerminated(_,terminatedActor) if terminatedActor == notifyActor =>
        close()
        false
      case _ => true
    }

}

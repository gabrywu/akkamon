package com.gabry.akkamon.injector

import akka.actor.ActorRef
import com.gabry.akkamon.listener.ActorSystemInstrumentationListener
import com.gabry.akkamon.protocol.InjectMessage
import com.gabry.akkamon.protocol.InjectMessage.{MessageReceived, MessageSent, MessageTold}

/**
  * Created by gabry on 2018/8/27 14:56
  */
class ActorListener(actorRef:ActorRef) extends ActorSystemInstrumentationListener{
  override def onMessage(message: InjectMessage): Unit = {
    actorRef ! message
  }

  override def close(): Unit = {

  }

  override def filter(message: InjectMessage): Boolean = message match {
    case MessageReceived(_,receiver,_) if receiver == actorRef =>false
    case MessageSent(_,receiver,_) if receiver == actorRef =>false
    case MessageTold(receiver,_,sender) if receiver == actorRef || sender == actorRef => false
    case MessageTold(receiver,_,sender) if receiver.path.name.equals("deadLetters") && sender.path.name.equals("deadLetters") => false
    case _ => true
  }
}

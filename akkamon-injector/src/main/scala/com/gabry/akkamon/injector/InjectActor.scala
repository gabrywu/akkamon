package com.gabry.akkamon.injector

import akka.actor.Actor
import com.gabry.akkamon.listener.ActorListener

/**
  * Created by gabry on 2018/8/27 13:59
  */

class InjectActor extends Actor{

  private var listener:Option[ActorListener] = None

  override def preStart():Unit = {
    listener = Some(new ActorListener(self))
  }

  override def postStop(): Unit = {
    listener.foreach(_.close())
  }
  override def receive: Receive = {
    case  all =>
      println(s"message $all")
  }
}

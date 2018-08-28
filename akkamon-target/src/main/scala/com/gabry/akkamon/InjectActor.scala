package com.gabry.akkamon

import akka.actor.Actor

/**
  * Created by gabry on 2018/8/27 13:59
  */

class InjectActor extends Actor{
  override def receive: Receive = {
    case  all =>
      println(s"message in ${this.getClass} $all")
  }
}

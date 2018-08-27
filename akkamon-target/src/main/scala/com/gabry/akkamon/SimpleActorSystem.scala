package com.gabry.akkamon

import akka.actor.{ActorSystem, Props}

/**
 * Hello world!
 *
 */
object SimpleActorSystem {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("SimpleActorSystem")
    val actor = system.actorOf(Props(new HelloActor),"helloActor")
    actor ! "Hello"
    Thread.sleep(10*1000)
    system.terminate()
  }
}

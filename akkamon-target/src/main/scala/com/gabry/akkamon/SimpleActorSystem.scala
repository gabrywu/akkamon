package com.gabry.akkamon

import akka.actor.{ActorSystem, Props}
import com.gabry.akkamon.injector.AkkaInjectorExtensionId

/**
 * Hello world!
 *
 */
object SimpleActorSystem {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("SimpleActorSystem")
    val extension = AkkaInjectorExtensionId(system)
    val injectActor = system.actorOf(Props(new InjectActor),"InjectActor")
    extension.bindTo(injectActor)
    val actor = system.actorOf(Props(new HelloActor),"helloActor")
    actor ! "Hello"
    Thread.sleep(10*1000)
    system.terminate()
  }
}

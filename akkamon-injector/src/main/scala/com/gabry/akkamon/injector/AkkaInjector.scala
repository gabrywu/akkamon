package com.gabry.akkamon.injector

import akka.actor.{ActorRef, ExtendedActorSystem, Extension, Props}

/**
  * Created by gabry on 2018/8/27 13:55
  */
object AkkaInjector{
  def apply(system: ExtendedActorSystem): AkkaInjector = new AkkaInjector(system)
  def Inject(actorRef: ActorRef):Boolean = ! (actorRef.path.name == "InjectActor" && actorRef.path.parent.name == "system")
}
class AkkaInjector private (system: ExtendedActorSystem) extends Extension{
  val injectActor:ActorRef = system.systemActorOf(Props(classOf[InjectActor]),"InjectActor")
}

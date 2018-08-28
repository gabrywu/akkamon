package com.gabry.akkamon.injector

import akka.actor.{ActorRef, ExtendedActorSystem, Extension, Props}

/**
  * Created by gabry on 2018/8/27 13:55
  * akkamon使用的例子，akkamon使用listener对外发布数据
  */
object AkkaInjector{
  def apply(system: ExtendedActorSystem): AkkaInjector = new AkkaInjector(system)
}
class AkkaInjector private (system: ExtendedActorSystem) extends Extension{
  val injectActor:ActorRef = system.systemActorOf(Props(classOf[InjectActor]),"InjectActor")
}

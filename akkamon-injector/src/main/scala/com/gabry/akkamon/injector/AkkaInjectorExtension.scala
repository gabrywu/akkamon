package com.gabry.akkamon.injector

import akka.actor.{ActorRef, ExtendedActorSystem, Extension}
import com.gabry.akkamon.listener.ActorSystemInstrumentationListener

/**
  * Created by gabry on 2018/8/27 13:55
  * akkamon使用的例子，akkamon使用listener对外发布数据
  */
object AkkaInjectorExtension{
  def apply(system: ExtendedActorSystem): AkkaInjectorExtension = new AkkaInjectorExtension(system)
}
class AkkaInjectorExtension private(system: ExtendedActorSystem) extends Extension{
  private var injectListeners = List.empty[ActorSystemInstrumentationListener]
  def bindTo(target:ActorRef):ActorSystemInstrumentationListener = {
    assert( target != null)
    val actorListener = new ActorListener(target)
    injectListeners = actorListener :: injectListeners
    actorListener
  }
  def unBindFrom(listener:ActorSystemInstrumentationListener):Unit = {
    listener.close()
    injectListeners = injectListeners.filter( _ != listener )
  }
}

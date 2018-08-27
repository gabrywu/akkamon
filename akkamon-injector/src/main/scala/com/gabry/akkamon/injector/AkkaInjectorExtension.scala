package com.gabry.akkamon.injector

import akka.actor.{ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}

/**
 * Hello world!
 *
 */
object AkkaInjectorExtension extends ExtensionId[AkkaInjector] with ExtensionIdProvider {

  override def createExtension(system: ExtendedActorSystem): AkkaInjector = AkkaInjector(system)

  override def lookup(): ExtensionId[_ <: Extension] = AkkaInjectorExtension

}

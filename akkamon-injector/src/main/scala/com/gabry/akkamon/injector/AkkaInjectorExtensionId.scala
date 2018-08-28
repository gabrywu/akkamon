package com.gabry.akkamon.injector

import akka.actor.{ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}

/**
 * Hello world!
 *
 */
object AkkaInjectorExtensionId extends ExtensionId[AkkaInjectorExtension] with ExtensionIdProvider {

  override def createExtension(system: ExtendedActorSystem): AkkaInjectorExtension = AkkaInjectorExtension(system)

  override def lookup(): ExtensionId[_ <: Extension] = AkkaInjectorExtensionId

}

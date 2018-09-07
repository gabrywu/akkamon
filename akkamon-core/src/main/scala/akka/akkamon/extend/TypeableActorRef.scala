package akka.akkamon.extend

import akka.actor.{Actor, ActorPath, ActorRef, ActorRefProvider, ActorRefScope, InternalActorRef}
import akka.dispatch.sysmsg.SystemMessage

/**
  * Created by gabry on 2018/9/6 17:53
  */
class TypeableActorRef(actorRef: InternalActorRef)(implicit val actor:Class[_ <: Actor]) extends InternalActorRef with ActorRefScope {
  override def start(): Unit = actorRef.start()

  override def resume(causedByFailure: Throwable): Unit = actorRef.resume(causedByFailure)

  override def suspend(): Unit = actorRef.suspend()

  override def restart(cause: Throwable): Unit = actorRef.restart(cause)

  override def stop(): Unit = actorRef.stop()

  override def sendSystemMessage(message: SystemMessage): Unit = actorRef.sendSystemMessage(message)

  override def provider: ActorRefProvider = actorRef.provider

  override def getParent: InternalActorRef = actorRef.getParent

  override def getChild(name: Iterator[String]): InternalActorRef = actorRef.getChild(name)

  override private[akka] def isTerminated = actorRef.isTerminated

  override def isLocal: Boolean = actorRef.isLocal

  override def !(message: Any)(implicit sender: ActorRef): Unit = actor.getMethod()

  override def path: ActorPath = actorRef.path
}

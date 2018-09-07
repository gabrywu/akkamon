package akka.akkamon.extend

import akka.actor.{ActorRef, ActorRefFactory, ActorRefProvider, ActorSystemImpl, InternalActorRef, Props}

import scala.concurrent.ExecutionContextExecutor

/**
  * Created by gabry on 2018/9/6 18:04
  */
class MyActorSystem(factory:ActorRefFactory) extends ActorRefFactory{
  override protected def systemImpl: ActorSystemImpl = factory.systemImpl

  override protected def provider: ActorRefProvider = factory.provider

  override implicit def dispatcher: ExecutionContextExecutor = factory.dispatcher

  override protected def guardian: InternalActorRef = factory.guardian

  override protected def lookupRoot: InternalActorRef = factory.lookupRoot

  override def actorOf(props: Props): ActorRef = {
    val ref = factory.actorOf(props).asInstanceOf[InternalActorRef]
    implicit val actor = props.actorClass()
    new TypeableActorRef(ref)
  }

  override def actorOf(props: Props, name: String): ActorRef =  factory.actorOf(props,name)

  override def stop(actor: ActorRef): Unit = factory.stop(actor)
}

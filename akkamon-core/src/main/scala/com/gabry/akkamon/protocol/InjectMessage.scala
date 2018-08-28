package com.gabry.akkamon.protocol

import akka.actor.{ActorRef, Props}
import akka.dispatch.Envelope
import com.typesafe.config.Config

/**
  * Created by gabry on 2018/8/27 14:03
  */
trait InjectMessage {
  val at:Long = System.currentTimeMillis()
}
trait ExtendedInjectMessage extends InjectMessage{
  def systemName:String
}
object InjectMessage{

  final case class ActorCellCreation(systemName:String, actorRef:ActorRef, props:Props, parent:ActorRef) extends ExtendedInjectMessage {
    override def toString: String = s"ActorCellCreation($systemName,$actorRef,${props.clazz}(${props.args.mkString(",")}),$parent)"
  }
  final case class ActorTerminated(systemName:String, actorRef:ActorRef) extends ExtendedInjectMessage {
    override def toString: String = s"ActorTerminated($systemName,$actorRef)"
  }
  final case class ActorSystemCreation(systemName:String,config:Config) extends ExtendedInjectMessage {
    override def toString: String = s"ActorSystemCreation($systemName,$config)"
  }
  final case class MessageTold(receiver:ActorRef, message:Any, sender:ActorRef) extends InjectMessage{
    override def toString: String = s"MessageTold($receiver,$message,$sender)"
  }
  final case class MessageReceived(systemName:String, receiver:ActorRef, envelope: Envelope) extends ExtendedInjectMessage {
    override def toString: String = s"MessageReceived($systemName,$receiver,$envelope)"
  }
  final case class MessageSent(systemName:String, receiver:ActorRef, envelope: Envelope) extends ExtendedInjectMessage{
    override def toString: String = s"MessageSent($systemName,$receiver,$envelope)"
  }
}
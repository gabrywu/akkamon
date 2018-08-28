package com.gabry.akkamon.listener

import com.gabry.akkamon.protocol.InjectMessage
import com.typesafe.config.ConfigFactory

import scala.annotation.tailrec
import scala.collection.immutable.{Queue => ImmutableQuque}

/**
  * Created by gabry on 2018/8/27 14:55
  */
object ActorSystemInstrumentationListeners{
  private val config = ConfigFactory.load().getConfig("listeners")

  private var listeners = List.empty[ActorSystemInstrumentationListener]
  // 在系统启动过程中，可能还没有listener启动，所以先缓存startupMessageLength长度的消息，
  // 并发送给第一个listener
  private val stashCapacity = config.getInt("stash.capacity")

  private var stashedMessages = ImmutableQuque.empty[InjectMessage]

  private def stash(message:InjectMessage):Unit = {
    if( stashedMessages.length > stashCapacity ){
      stashedMessages = stashedMessages.dropRight(1)
    }
    stashedMessages = stashedMessages.enqueue(message)
  }
  private def unStash[T](messageProcessor: InjectMessage => T ):Unit = {
    @tailrec
    def unStashAll(messages:ImmutableQuque[InjectMessage]):Unit = {
      if(messages.nonEmpty){
        val (message,tailMessages) = messages.dequeue
        messageProcessor(message)
        unStashAll(tailMessages)
      }
    }
    unStashAll(stashedMessages)

  }
  def register(newListener:ActorSystemInstrumentationListener):Unit = {
    if(!newListener.isInstanceOf[UnListenedListener]){
      listeners = newListener :: listeners
      unStash{notifyMessage}
    }
  }
  def unRegister(oldListener:ActorSystemInstrumentationListener):Unit = {
    listeners = listeners.filter( _ != oldListener )
  }
  def notifyMessage(message:InjectMessage):InjectMessage = {
    if(message != null){
      if( listeners.isEmpty ){
        stash(message)
      }else{
        listeners.foreach(listener => if(listener.filter(message)) listener.onMessage(message))
      }
    }
    message
  }
}

/**
  * 注入点监听器
  */
abstract class ActorSystemInstrumentationListener{
  /**
    * 对监听事件进行过滤
    * 有些时候防止栈溢出或死循环，会放弃对本身listener的事件监听
    * @param message 待过滤的消息
    * @return true会调用onMessage;false不调用onMessage
    */
  def filter(message:InjectMessage):Boolean
  def onMessage(message:InjectMessage):Unit
  final def close():Unit = {
    postClose()
  }
  def postClose():Unit
}
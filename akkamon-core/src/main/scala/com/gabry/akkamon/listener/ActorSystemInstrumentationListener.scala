package com.gabry.akkamon.listener

import java.util.concurrent.atomic.AtomicInteger

import com.gabry.akkamon.protocol.InjectMessage

/**
  * Created by gabry on 2018/8/27 14:55
  */
object ActorSystemInstrumentationListeners{
  private var listeners = List.empty[ActorSystemInstrumentationListener]
  // 在系统启动过程中，可能还没有listener启动，所以先缓存startupMessageLength长度的消息，
  // 并发送给第一个listener
  private val startupMessageLength = 1000
  private val startupMessage = new Array[InjectMessage](startupMessageLength)
  private val bufferPos = new AtomicInteger(0)

  def register(newListener:ActorSystemInstrumentationListener):Unit = {
    if(!newListener.isInstanceOf[UnListenedListener]){
      listeners = newListener :: listeners
    }
    if( startupMessage.nonEmpty && bufferPos.get < startupMessageLength )
      0 until bufferPos.get foreach { idx =>
        listeners.foreach(_.onMessage(startupMessage(idx)))
      }

  }
  def unRegister(oldListener:ActorSystemInstrumentationListener):Unit = {
    listeners = listeners.filter( _ != oldListener )
  }
  def notifyMessage(message:InjectMessage):InjectMessage = {
    assert(message!=null)
    if( listeners.isEmpty ){
      startupMessage(bufferPos.getAndIncrement()%startupMessageLength) = message
    }else{
      listeners.foreach(_.onMessage(message))
    }
    message
  }
}
abstract class ActorSystemInstrumentationListener{
  def onMessage(message:InjectMessage):Unit
  def close():Unit
}
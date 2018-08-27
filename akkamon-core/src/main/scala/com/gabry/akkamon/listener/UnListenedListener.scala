package com.gabry.akkamon.listener
import com.gabry.akkamon.protocol.InjectMessage
import org.slf4j.LoggerFactory

/**
  * Created by gabry on 2018/8/27 17:07
  * 系统启动期间，有些消息无法通知对应的listener，由该listener监听
  */
class UnListenedListener extends ActorSystemInstrumentationListener{
  private val log = LoggerFactory.getLogger(classOf[UnListenedListener])

  override def onMessage(message: InjectMessage): Unit = {
    log.warn(s"UnListened message $message")
  }

  override def close(): Unit = {

  }
}

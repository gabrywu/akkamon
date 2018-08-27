package com.gabry.akkamon

import akka.actor.Actor

/**
  * Created by gabry on 2018/8/24 12:20
  */
class HelloActor extends Actor{
  def printlnHelloActor():Unit = {
    println("HelloActor")
  }
  override def receive: Receive = {
    case any =>
      printlnHelloActor()
      println(s"HelloWorld: $any")
  }
}

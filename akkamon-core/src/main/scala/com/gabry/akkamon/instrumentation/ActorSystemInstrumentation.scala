package com.gabry.akkamon.instrumentation

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{ActorRef, ActorSystem, Props}
import com.gabry.akkamon.metrics.Metrics
import org.slf4j.LoggerFactory

/**
  * Created by gabry on 2018/8/24 18:30
  */
/**
  * ActorSystem注入的相关接口
  */
trait ActorSystemInstrumentation {
  def beforeActorCellCreation(system: ActorSystem, ref: ActorRef, props: Props, parent: ActorRef ):Unit
  def beforeActorSystemCreation(system: ActorSystem): Unit
}

class ActorSystemInstrumentationImpl extends ActorSystemInstrumentation with Metrics{
  private val log = LoggerFactory.getLogger(classOf[ActorSystemInstrumentation])
  private var system:Option[ActorSystem] = None
  private val actorCounter = new AtomicInteger(0)

  override def beforeActorCellCreation(system: ActorSystem, ref: ActorRef, props: Props, parent: ActorRef): Unit = {
    val currentActorNumber = actorCounter.getAndIncrement()
    log.info(s"Actor[${props.clazz.getName}(${props.args.mkString(",")})] has created,name: ${ref.path.name},current actor number: $currentActorNumber")
  }

  override def beforeActorSystemCreation(actorSystem: ActorSystem): Unit = {
    if(system.isEmpty){
      log.info(s"ActorSystem has created:${actorSystem.name}")
      system = Some(actorSystem)
    }else{
      log.error("ActorSystem set duplicated")
    }
  }

  override def numberOfActor(): Int = actorCounter.get()
}

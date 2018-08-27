package akka.akkamon.extend

import akka.actor.{ActorCell, ActorRef, ActorSystem, Props}
import akka.dispatch.Envelope
import com.gabry.akkamon.listener.{ActorSystemInstrumentationListener, UnListenedListener}
import com.gabry.akkamon.protocol.InjectMessage
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

/**
  * Created by gabry on 2018/8/24 18:30
  */
/**
  * ActorSystem注入的相关接口。由InstrumentationJavaShell负责调用相关接口
  */
trait ListenerInstrumentation{
  def afterListenerCreation(newListener:ActorSystemInstrumentationListener):Unit
  def afterListenerClose(oldListener:ActorSystemInstrumentationListener):Unit
}
trait ActorCellInstrumentation {
  def beforeActorCellCreation(system: ActorSystem, ref: ActorRef, props: Props, parent: ActorRef ):Unit
  def beforeActorTerminate(cell:ActorCell):Unit
}
trait ActorSystemInstrumentation {
  def beforeActorSystemCreation(system: ActorSystem,config:Config): Unit
}
trait MessageInstrumentation{
  def beforeInvoke(cell: ActorCell, envelope: Envelope):Unit
  def beforeTell(receiver:ActorRef,message:Any,sender:ActorRef):Unit
  def beforeSend(cell: ActorCell,envelope: Envelope):Unit
}
trait ActorSystemInjectPoint extends ListenerInstrumentation
  with ActorCellInstrumentation
  with ActorSystemInstrumentation
  with MessageInstrumentation

class ActorSystemInjectPointImpl extends ActorSystemInjectPoint {
  import InjectMessage._
  import com.gabry.akkamon.listener.ActorSystemInstrumentationListeners._
  private val log = LoggerFactory.getLogger(classOf[ActorSystemInjectPoint])
  private var unListenedListener:Option[UnListenedListener] = None
  override def beforeActorCellCreation(system: ActorSystem, ref: ActorRef, props: Props, parent: ActorRef): Unit = {
    val message = notifyMessage(ActorCellCreation(system.name,ref,props,parent))
    log.debug(s"beforeActorCellCreation $message")
  }

  override def beforeActorSystemCreation(actorSystem: ActorSystem,config:Config): Unit = {
    unListenedListener = Some(new UnListenedListener)
    val message = notifyMessage(ActorSystemCreation(actorSystem.name,config))
    log.debug(s"beforeActorSystemCreation $message")
  }

  override def afterListenerCreation(newListener: ActorSystemInstrumentationListener): Unit = {
    register(newListener)
    log.debug(s"afterListenerCreation $newListener")
  }

  override def afterListenerClose(oldListener: ActorSystemInstrumentationListener): Unit = {
    unRegister(oldListener)
    log.debug(s"afterListenerClose $oldListener")
  }

  override def beforeTell(receiver:ActorRef,message: Any, sender: ActorRef): Unit = {
    val msg = notifyMessage(MessageTelled(receiver,message,sender))
    log.debug(s"beforeTell $msg")
  }

  override def beforeInvoke(cell: ActorCell, envelope: Envelope): Unit = {
    val message = notifyMessage(MessageReceived(cell.system.name,cell.self,envelope))
    log.debug(s"beforeInvoke $message")
  }

  override def beforeActorTerminate(cell:ActorCell): Unit = {
    val message = notifyMessage(ActorTerminated(cell.system.name,cell.self))
    log.debug(s"beforeActorTerminate $message")
  }

  override def beforeSend(cell: ActorCell, envelope: Envelope): Unit = {
    val message = notifyMessage(MessageSent(cell.system.name,cell.self,envelope))
    log.debug(s"beforeSend $message")
  }
}

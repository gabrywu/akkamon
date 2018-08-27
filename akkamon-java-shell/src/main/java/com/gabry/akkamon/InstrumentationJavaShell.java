package com.gabry.akkamon;

import akka.actor.*;
import akka.akkamon.extend.ActorSystemInjectPoint;
import akka.akkamon.extend.ActorSystemInjectPointImpl;
import akka.dispatch.Envelope;
import akka.dispatch.sysmsg.SystemMessage;
import com.gabry.akkamon.listener.ActorSystemInstrumentationListener;
import com.typesafe.config.Config;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by gabry on 2018/8/24 15:22
 */
@Aspect
public class InstrumentationJavaShell implements ActorSystemInjectPoint {

    private static ActorSystemInjectPointImpl actorSystemInstrumentation = new ActorSystemInjectPointImpl();

    @Pointcut("execution(akka.actor.ActorSystemImpl.new(..)) && this(system) && args(*,config,*,*,*,*)")
    public void actorSystemCreation(ActorSystem system, Config config){}

    @Override
    @After("actorSystemCreation(system,config)")
    public void beforeActorSystemCreation(ActorSystem system, Config config) {
        actorSystemInstrumentation.beforeActorSystemCreation(system,config);
    }
    @Pointcut("execution(akka.actor.ActorCell.new(..)) && args(system, ref, props, *, parent)")
    public void actorCellCreation(ActorSystem system , ActorRef ref,Props props , ActorRef parent){}

    @Override
    @Before("actorCellCreation(system,ref,props,parent)")
    public void beforeActorCellCreation(ActorSystem system , ActorRef ref, Props props , ActorRef parent) {
        actorSystemInstrumentation.beforeActorCellCreation(system,ref,props,parent);
    }
    @Pointcut("execution(* akka.actor.ActorCell.terminate()) && this(cell)")
    public void  actorTerminate( ActorCell cell) {}
    @Override
    @Before("actorTerminate(cell)")
    public void beforeActorTerminate(ActorCell cell) {
        actorSystemInstrumentation.beforeActorTerminate(cell);
    }

    @Pointcut("execution(* akka.actor.ActorCell.invoke(*)) && this(cell) && args(envelope)")
    public void actorCellInvoke(ActorCell cell , Envelope envelope ) {}
    @Override
    @Before("actorCellInvoke(cell,envelope)")
    public void beforeInvoke(ActorCell cell, Envelope envelope) {
        if( InjectFilter.inject(cell.self()) ){
            actorSystemInstrumentation.beforeInvoke(cell,envelope);
        }
    }

    @Pointcut("execution(com.gabry.akkamon.listener.ActorSystemInstrumentationListener.new(..)) && this(listener)")
    public void listenerCreation(ActorSystemInstrumentationListener listener) {

    }

    @Override
    @After("listenerCreation(listener)")
    public void afterListenerCreation(ActorSystemInstrumentationListener listener) {
        actorSystemInstrumentation.afterListenerCreation(listener);
    }
    @Pointcut("execution(* com.gabry.akkamon.listener.ActorSystemInstrumentationListener.close(..)) && this(listener)")
    public void listenerClose(ActorSystemInstrumentationListener listener) {

    }
    @Override
    @After("listenerClose(listener)")
    public void afterListenerClose(ActorSystemInstrumentationListener listener) {
        actorSystemInstrumentation.afterListenerClose(listener);
    }
    @Pointcut("execution(* akka.actor.ScalaActorRef.$bang*(..)) && this(receiver) && args(message,sender)")
    public void actorRefTell(ActorRef receiver,Object message, ActorRef sender){}
    @Override
    @Before("actorRefTell(receiver,message,sender)")
    public void beforeTell(ActorRef receiver,Object message, ActorRef sender) {
        if( InjectFilter.inject(receiver) ){
            actorSystemInstrumentation.beforeTell(receiver,message,sender);
        }
    }
    @Pointcut("execution(* akka.actor.ActorCell.sendMessage(*)) && this(cell) && args(envelope)")
    public void sendMessage(ActorCell cell, Envelope envelope) {}
    @Override
    @Before("sendMessage(cell,envelope)")
    public void beforeSend(ActorCell cell, Envelope envelope) {

    }
}

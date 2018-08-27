package com.gabry.akkamon;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.gabry.akkamon.instrumentation.ActorSystemInstrumentation;
import com.gabry.akkamon.instrumentation.ActorSystemInstrumentationImpl;
import com.gabry.akkamon.metrics.collector.ActorSystemCollection;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by gabry on 2018/8/24 15:22
 */
@Aspect
public class InstrumentationJavaShell implements ActorSystemInstrumentation  {
    private static ActorSystemInstrumentationImpl actorSystemInstrumentation = new ActorSystemInstrumentationImpl();

    /**
     * ActorSystemCollection启动时把InstrumentationJavaShell对象传给它
     */
    @Pointcut("execution(com.gabry.akkamon.metrics.collector.ActorSystemCollection.new(..)) && this(collection) ")
    public void actorSystemCollectionCreate(ActorSystemCollection collection){ }

    @Before("actorSystemCollectionCreate(collection)")
    public void beforeActorSystemCollection(ActorSystemCollection collection){
        collection.setWeaveMetrics( actorSystemInstrumentation );
    }

    @Pointcut("execution(akka.actor.ActorSystemImpl.new(..)) && this(system)")
    public void actorSystemCreation(ActorSystem system){}

    @Override
    @After("actorSystemCreation(system)")
    public void beforeActorSystemCreation(ActorSystem system) {
        actorSystemInstrumentation.beforeActorSystemCreation(system);
    }
    @Pointcut("execution(akka.actor.ActorCell.new(..)) && args(system, ref, props, *, parent)")
    public void actorCellCreation(ActorSystem system , ActorRef ref,Props props , ActorRef parent){}

    @Override
    @Before("actorCellCreation(system,ref,props,parent)")
    public void beforeActorCellCreation(ActorSystem system , ActorRef ref, Props props , ActorRef parent) {
        actorSystemInstrumentation.beforeActorCellCreation(system,ref,props,parent);
    }

}

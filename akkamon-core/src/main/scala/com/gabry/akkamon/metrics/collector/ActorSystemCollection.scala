package com.gabry.akkamon.metrics.collector

import akka.actor.Address
import akka.cluster.metrics.{MetricsCollector, NodeMetrics}
import com.gabry.akkamon.instrumentation.WeaveTargetAccess
import com.gabry.akkamon.metrics.Metrics
import org.slf4j.LoggerFactory

/**
  * Created by gabry on 2018/8/24 19:29
  * 该收集器需要与InstrumentationShell进行通信，所以InstrumentationShell监控到ActorSystemCollection创建时，
  * 调用WeaveTargetAccess.setWeaveShell对shell设置值。
  * 这样ActorSystemCollection就可以获得InstrumentationShell相关的采样值了
  */
class ActorSystemCollection(address: Address, decayFactor: Double) extends MetricsCollector with WeaveTargetAccess{
  private val log = LoggerFactory.getLogger(classOf[ActorSystemCollection])
  private var metric:Option[Metrics] = None
  override def sample(): NodeMetrics = {
    NodeMetrics(address,0)
  }

  override def close(): Unit = {
  }

  override def setWeaveMetrics(metrics: Metrics): Unit = {
    log.info(s"Set current WeaveMetricCollection: $metrics")
    if( metric.isEmpty ) {
      metric = Some(metrics)
    }else{
      log.error(s"Set current WeaveMetricCollection duplicated: $metrics")
    }
  }
}

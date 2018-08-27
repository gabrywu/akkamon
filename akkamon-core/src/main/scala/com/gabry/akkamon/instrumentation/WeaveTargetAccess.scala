package com.gabry.akkamon.instrumentation

import com.gabry.akkamon.metrics.Metrics

/**
  * Created by gabry on 2018/8/24 19:31
  */
trait WeaveTargetAccess {
  def setWeaveMetrics(metrics: Metrics)
}

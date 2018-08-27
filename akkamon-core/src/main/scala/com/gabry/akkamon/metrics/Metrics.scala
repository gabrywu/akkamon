package com.gabry.akkamon.metrics

/**
  * Created by gabry on 2018/8/24 20:02
  * 这个类定义collection能够搜集的指标
  * 通过函数定义
  */
trait Metrics {
  def numberOfActor():Int
}

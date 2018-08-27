# 方案说明

core

    --- collection:指标搜集相关
    --- instrumentation:注入相关
        ---WeaveTargetAccess 给MetricsCollector设置通过javaagent注入的InstrumentationJavaShell实例

ActorSystemCollection调用InstrumentationJavaShell相关的函数搜集指标
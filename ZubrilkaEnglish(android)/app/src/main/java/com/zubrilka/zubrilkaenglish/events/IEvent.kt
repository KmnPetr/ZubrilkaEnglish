package com.zubrilka.zubrilkaenglish.events

/**
 * от этого интерфейса наследуются все евенты для Event Bus
 */
interface iEvent<T : Enum<T>>{
    val typeEvent: T
    var properties: MutableMap<String, Any>
}
package com.example.zubrilkaenglish.events

/**
 * от этого интерфейса наследуются все евенты для Event Bus
 */
interface iEvent<T : Enum<T>>{
    val typeEvent: T
}
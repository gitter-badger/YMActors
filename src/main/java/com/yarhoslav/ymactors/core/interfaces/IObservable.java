package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author yarhoslavme
 */
public interface IObservable {
    public void addObserver(Object pEvent, Object pMsg, IActorRef pActor);
    public void removeObserver(Object pEvent, IActorRef pActor);
    public void notifyObservers(Object pEvent);
}

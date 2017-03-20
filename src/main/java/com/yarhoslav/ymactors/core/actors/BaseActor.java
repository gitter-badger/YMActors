/* 
 * This file is part of YMActors project.
 * Copyright (C) 2017 Yarhoslav ME <yarhoslavme@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.BaseContext;
import com.yarhoslav.ymactors.core.BaseWorker;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IObservable;
import com.yarhoslav.ymactors.core.interfaces.IObserver;
import com.yarhoslav.ymactors.core.interfaces.ISystem;
import com.yarhoslav.ymactors.core.interfaces.IWorker;
import com.yarhoslav.ymactors.core.messages.BaseEnvelope;
import com.yarhoslav.ymactors.core.states.RunningState;
import com.yarhoslav.ymactors.core.states.StoppingState;
import java.util.Iterator;

/**
 *
 * @author yarhoslavme
 */
public abstract class BaseActor implements IActorRef, IObservable {

    private final Logger logger = LoggerFactory.getLogger(BaseActor.class);
    private IActorContext context;
    private IWorker worker;
    private ISystem system;
    private String name;
    private final AtomicBoolean isAlive = new AtomicBoolean(false);

    //TODO: Store the actor class in order to recreate it when restarted.
    public abstract void process(Object pMsg, IActorRef pSender) throws Exception;

    public void postStart() throws Exception {
    }

    public void beforeStop() throws Exception {
    }

    public void handleException(Object pData, IActorRef pSender) {
    }

    public void setSystem(ISystem pSystem) {
        system = pSystem;
    }

    public void setName(String pName) {
        name = pName;
    }

    //TODO: Generate own exception classes
    public void start() throws IllegalStateException {
        context = new BaseContext(system);
        worker = new BaseWorker(context);
        //TODO: ID should be an Static Constant in StoppingState?
        publishEvent(new StoppingState().id());
        try {
            postStart();
        }
        catch (Exception ex) {
            logger.warn("Actor {} throws an exception in postStart method while starting: {}", name, ex.getMessage());
            throw new IllegalStateException(String.format("Error starting Actor %s: %s", name, ex));
        }
        context.setState(new RunningState(this));
        isAlive.set(true);
    }

    public void stop() throws Exception {
        isAlive.set(false);
        worker.discardMessages();
        try {
            beforeStop();
        }
        catch (Exception ex) {
            throw new IllegalStateException(String.format("Error stoping Actor %s", name));
        }
        finally {
            system.removeActor(this);
            context.setState(new StoppingState());
            //TODO: Cancel and remove ticket from YMExecutor
        }
    }

    @Override
    public void tell(Object pData, IActorRef pSender) throws IllegalStateException {
        if (!isAlive.get()) {
            return;
        }
        worker.newMessage(new BaseEnvelope(pData, pSender));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addObserver(String pEvent, Object pMsg, IObserver pObserver) throws IllegalArgumentException {
        context.getObservableService().addObserver(pEvent, pMsg, pObserver);
    }

    @Override
    public void removeObserver(String pEvent, IObserver pObserver) throws IllegalArgumentException {
        context.getObservableService().removeObserver(pEvent, pObserver);
    }

    @Override
    public void notifyObservers(String pEvent) {
        context.getObservableService().notifyObservers(pEvent);
    }

    @Override
    public void publishEvent(String pEvent) {
        context.getObservableService().publishEvent(pEvent);
    }

    @Override
    public Iterator availableEvents() {
        return context.getObservableService().availableEvents();
    }

    @Override
    public void clearEvents() {
        context.getObservableService().clearEvents();
    }
    
    public IActorContext getContext() {
        return context;
    }
}

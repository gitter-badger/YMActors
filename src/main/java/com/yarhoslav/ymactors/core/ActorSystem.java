/**
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
package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.BaseActor;
import com.yarhoslav.ymactors.core.actors.UniverseActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.services.YMExecutorService;
import static java.lang.System.currentTimeMillis;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.ISystem;
import com.yarhoslav.ymactors.core.interfaces.IWorker;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author yarhoslav
 */
public class ActorSystem implements ISystem {

    private final Logger logger = LoggerFactory.getLogger(ActorSystem.class);
    private final YMExecutorService living = new YMExecutorService(8); //TODO: Create APPConfig with external config file
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1); //TODO: Create APPConfig with external config file
    private final AtomicBoolean isAlive = new AtomicBoolean(false);
    private final String name;
    private UniverseActor universeActor;
    private IActorContext universeContext;
    private final ConcurrentMap<String, IActorRef> actors = new ConcurrentHashMap<>();
    private final long startTime = currentTimeMillis();

    //TODO: Create own exception classes
    public ActorSystem(String pName) throws IllegalArgumentException {
        if (pName == null) {
            throw new IllegalArgumentException("ActorContainer's name can not be null.");
        }
        name = pName;
    }

    public void start() {
        universeActor = new UniverseActor();
        universeActor.setName(UniverseActor.SYSTEMACTOR);
        universeActor.setSystem(this);
        universeActor.start();
        isAlive.set(true);
    }

    @Override
    public IActorRef findActor(String pName) throws IllegalArgumentException {
        IActorRef actor = actors.get(pName);
        if (actor == null) {
            throw new IllegalArgumentException(String.format("Actor with name:%s not found in System %s.", pName, name));
        }
        return actor;
    }

    @Override
    public BaseActor addActor(BaseActor pActorType, String pName) throws IllegalArgumentException {
        if (actors.containsKey(pName)) {
            throw new IllegalArgumentException(String.format("Name:%s already used in System %s", pName, name));
        } else {
            BaseActor newChild = pActorType;
            newChild.setName(pName);
            newChild.setSystem(this);
            newChild.start();
            actors.put(pName, newChild);
            return newChild;
        }
    }

    @Override
    public void queueUp(IWorker pWorker) {
        if (isAlive.get()) {
            living.offer(pWorker);
        } else {
            logger.warn("System {} is inactive. New worker cannot be enqueued.", name);
        }
    }

    @Override
    public int getDispatcher() {
        return living.getDispacher();
    }

    public void ShutDownNow() {
        //TODO: Create Shutdown controlled by UniverseActor -> will send PoisonPill to all Actors in the System and then shuts down the Container.
        isAlive.set(false);
        logger.info("Actors Container {} is shutting down.", name);
        scheduler.shutdownNow();
        /*
        living.shutdown();
        try {
            living.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            logger.warn("An error occurs trying to shutdown the workpool.", ex);
        } finally {
            living.shutdownNow();
        }*/
    }

    public synchronized String getEstadistica() {
        String tmp = "Start: " + startTime;

        tmp = tmp + " Up:" + getUpTime();
        tmp = tmp + " Mensajes:" + living.mensajes.get();
        tmp = tmp + " Actores:" + actors.size();
        tmp = tmp + " Executor service:" + living.toString();

        return tmp;
    }

    public long getUpTime() {
        return currentTimeMillis() - startTime;
    }

    public void tell(Object pData, IActorRef pSender) {
        logger.info("Universe actor recibe un mensaje de: {}", pSender.getName());
        universeActor.tell(pData, pSender);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void removeActor(IActorRef pActor) throws IllegalArgumentException {
        IActorRef actor = actors.remove(pActor.getName());
        if (actor == null) {
            throw new IllegalArgumentException(String.format("Name:%s does not exists in System %s", pActor.getName(), name));
        }
    }

}

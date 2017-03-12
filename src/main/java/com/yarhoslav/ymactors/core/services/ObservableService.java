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
package com.yarhoslav.ymactors.core.services;

import com.yarhoslav.ymactors.core.interfaces.IObservable;
import com.yarhoslav.ymactors.core.interfaces.IObserver;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Yarhoslav ME <yarhoslavme@gmail.com>
 */
public final class ObservableService implements IObservable {

    private final Map<String, Map<IObserver, Object>> events = new ConcurrentHashMap<>();

    @Override
    public void addObserver(String pEvent, Object pMsg, IObserver pObserver) throws IllegalArgumentException {
        Map observers = events.get(pEvent);
        if (observers == null) {
            throw new IllegalArgumentException(String.format("Event %s not published by the Observable object.", pEvent));
        } else {
            observers.put(pObserver, pMsg);
        }
    }

    @Override
    public void removeObserver(String pEvent, IObserver pObserver) throws IllegalArgumentException {
        Map observers = events.get(pEvent);
        if (observers == null) {
            throw new IllegalArgumentException(String.format("Event %s not published by the Observable object.", pEvent));
        } else {
            observers.remove(pObserver);
        }
    }

    @Override
    public void notifyObservers(String pEvent) {
        Map<IObserver, Object> observers = events.get(pEvent);
        if (observers != null) {
            observers.forEach((observer, msg) -> observer.eventUpdate(msg, this));
        }
    }

    @Override
    public void publishEvent(String pEvent) {
        events.put(pEvent, new ConcurrentHashMap<>());
    }

    @Override
    public Iterator availableEvents() {
        return events.keySet().iterator();
    }

    @Override
    public void clearEvents() {
        events.clear();
    }

}

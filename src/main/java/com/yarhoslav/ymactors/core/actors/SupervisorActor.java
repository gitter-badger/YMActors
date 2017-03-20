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

import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IObservable;
import com.yarhoslav.ymactors.core.interfaces.IObserver;
import com.yarhoslav.ymactors.core.states.StoppingState;

/**
 *
 * @author Yarhoslav ME <yarhoslavme@gmail.com>
 */
public abstract class SupervisorActor extends BaseActor implements IObserver {
    
    //TODO: Define messages for observables minions.
    //TODO: Define list of minions
    
    @Override
    public void eventUpdate(Object pMsg, IObservable pObservable) {
        tell(pMsg, (IActorRef) pObservable);
    }
    
    //TODO: Overrides START adding behaviors for messages from minions.
    
    public BaseActor newMinion(BaseActor pActorType, String pName) throws IllegalArgumentException {
        BaseActor minion = getContext().getSystem().addActor(pActorType, pName);
        minion.addObserver(new StoppingState().id(), pName, this);
        //TODO: Add observer for event ERROR_STATE.
        return minion;
    }    
}

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
package com.yarhoslav.ymactors.core.states;

import com.yarhoslav.ymactors.core.interfaces.IAction;
import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorState;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author yarhoslavme
 */
public abstract class BaseState implements IActorState {

    private final Map<String, IAction> behaviors = new HashMap();
    private final String id;
    
    public BaseState(String pId) {
        id = pId;
    }

    @Override
    public void addBehavior(IActorMsg pMsg, IAction pAction) {
        behaviors.put(pMsg.id(), pAction);
    }

    @Override
    public void execute(Object pMsg, IActorRef pSender) throws Exception {
        if (pMsg instanceof IActorMsg) {
            IActorMsg msg = (IActorMsg) pMsg;
            IAction action = behaviors.get(msg.id());
            if (action != null) {
                action.doIt(msg, pSender);
            }
        } else {
            unknownMsg(pMsg, pSender);
        }
    }
    
    @Override
    public String id() {
        return id;
    }
    
    public abstract void unknownMsg(Object pMsg, IActorRef pSender) throws Exception;
}

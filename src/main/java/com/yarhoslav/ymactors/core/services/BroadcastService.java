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

import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public final class BroadcastService {

    private final Logger logger = LoggerFactory.getLogger(BroadcastService.class);
    private final Iterator children;
    
    public BroadcastService(Iterator pChildren) {
        children = pChildren;
    }
    
    public BroadcastService send(IActorMsg pMsg, IActorRef pSender) {
        while (children.hasNext()) {
            IActorRef child = (IActorRef) children.next();
            child.tell(pMsg, pSender);
        }
        return this;
    }

}

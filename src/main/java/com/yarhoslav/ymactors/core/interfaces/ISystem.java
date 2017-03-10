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
package com.yarhoslav.ymactors.core.interfaces;

import com.yarhoslav.ymactors.core.actors.BaseActor;

/**
 *
 * @author yarhoslavme
 */
public interface ISystem {

    public void queueUp(IWorker pWorker);

    public int getDispatcher();
    
    public String getName();
       
    public IActorRef findActor(String pName) throws IllegalArgumentException;
    
    public <E extends BaseActor> IActorRef addActor(E pActorType, String pName) throws IllegalArgumentException;
    
    public void removeActor(IActorRef pActor) throws IllegalArgumentException;

}

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
package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorMsg;

/**
 *
 * @author YarhoslavME
 */
public final class PoisonPill implements IActorMsg {
    
    private final String ID = "POISONPILL";
    private static final PoisonPill SINGLETON = new PoisonPill();
    
    public static PoisonPill getInstance() {
        return SINGLETON;
    }  

    @Override
    public Object takeData() {
        return SINGLETON;
    }

    @Override
    public String id() {
        return ID;
    }
}

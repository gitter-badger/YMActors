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
package ymactors;

import com.yarhoslav.ymactors.core.actors.BaseActor;
import com.yarhoslav.ymactors.core.messages.PoisonPill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;

/**
 *
 * @author YarhoslavME
 */
public class ContadorActor extends BaseActor {

    Logger logger = LoggerFactory.getLogger(ContadorActor.class);
    private int contador;
    
    public ContadorActor(int pContador) {
        contador = pContador;
    }

    @Override
    public void process(Object msj, IActorRef pSender) {
        if (msj.equals("contar")) {
            contador--;
            if (contador <= 0) {
                tell(PoisonPill.getInstance(), this);
            } else {
                tell("contar", this);
            }
        }
    }

}

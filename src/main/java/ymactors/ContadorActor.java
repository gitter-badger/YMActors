package ymactors;

import com.yarhoslav.ymactors.core.DefaultActorHandler;
import static com.yarhoslav.ymactors.core.messages.PoisonPill.getInstance;

/**
 *
 * @author YarhoslavME
 */
public class ContadorActor extends DefaultActorHandler {
    private int contador;
    
    public ContadorActor(int pInicial) {
        contador = pInicial;
    }

    @Override
    public void process(Object msj) {
        if (msj.equals("contar")) {
            contador--;
            if (contador <= 0) {
                  this.getMyself().tell(getInstance(), null);
            } else {
                this.getMyself().tell("contar", null);
            }
        }
    }
    
}
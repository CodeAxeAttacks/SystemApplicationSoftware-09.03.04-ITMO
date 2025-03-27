package Moves;
import ru.ifmo.se.pokemon.*;
public class Swagger extends StatusMove {
    public Swagger() {
        super(Type.NORMAL, 0, 85);
    }
    @Override
    public void applyOppEffects(Pokemon p) {
        p.confuse();
        p.setMod(Stat.ATTACK, 2);
    }
    @Override
    protected String describe() {
        return "использует Swagger: вызывает растерянность и увеличивает атаку соперника в 2 раза";
    }
}

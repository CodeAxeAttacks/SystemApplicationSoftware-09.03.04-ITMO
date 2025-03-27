package Moves;
import ru.ifmo.se.pokemon.*;
public class Thunder_Wave extends StatusMove {
    public Thunder_Wave() {
        super(Type.ELECTRIC, 0, 90);
    }
    @Override
    public void applyOppEffects(Pokemon p) {
        Effect.paralyze(p);
    }
    @Override
    protected String describe() {
        return "использует Thunder Wave: парализует";
    }
}

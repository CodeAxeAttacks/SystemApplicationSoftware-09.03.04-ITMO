package Moves;
import ru.ifmo.se.pokemon.*;
public class Powder_Snow extends SpecialMove {
    public Powder_Snow() {
        super(Type.ICE, 40, 100);
    }
    private boolean flag;
    @Override
    public void applyOppEffects(Pokemon p) {
        int chance = (int)(Math.random() * 101);
        if (chance <= 10) flag = true;
        if (flag) Effect.freeze(p);
    }
    @Override
    protected String describe() {
        if (flag) return "повезло, покемон использует Powder Snow: замораживает противника и наносит ему урон";
        else return "наносит урон";
    }
}
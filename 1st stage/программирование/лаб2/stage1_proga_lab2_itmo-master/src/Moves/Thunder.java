package Moves;
import ru.ifmo.se.pokemon.*;
public class Thunder extends SpecialMove {
    public Thunder() {
        super(Type.ELECTRIC, 110, 70);
    }
    private boolean flag;
    @Override
    public void applyOppDamage(Pokemon def, double damage) {
        int chance = (int)(Math.random() * 101);
        if (chance <= 30 && !def.hasType(Type.ELECTRIC)) flag = true;
        if (flag) Effect.paralyze(def);
    }
    @Override
    protected String describe() {
        if (flag) return "повезло, покемон использует Thunder: парализует и наносит урон";
        else return "наносит урон";
    }
}

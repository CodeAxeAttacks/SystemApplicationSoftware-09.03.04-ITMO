package Moves;
import ru.ifmo.se.pokemon.*;
public class Facade extends PhysicalMove {
    public Facade() {
        super(Type.NORMAL, 70, 100);
    }
    private boolean flag;
    @Override
    public void applyOppDamage(Pokemon def, double damage) {
        Status condition = def.getCondition();
        if (condition.equals(Status.POISON) || condition.equals(Status.PARALYZE)) {
            flag = true;
            def.setMod(Stat.HP, +2 * (int)Math.round(damage));
        }
    }
    @Override
    protected  String describe() {
        if (flag) return "наносит двойной урон";
        else return "наносит урон";
    }
}
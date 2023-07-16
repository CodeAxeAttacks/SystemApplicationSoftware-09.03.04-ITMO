package Moves;
import ru.ifmo.se.pokemon.*;
public class Dream_Eater extends SpecialMove {
    public Dream_Eater() {
        super(Type.PSYCHIC, 100, 100);
    }
    private boolean flag;
    @Override
    public void applyOppDamage(Pokemon def, double damage) {
        Status condition = def.getCondition();
        flag = true;
        if (condition.equals(Status.SLEEP)) def.setMod(Stat.HP, (int)Math.round(damage));
    }
    @Override
    public void applySelfDamage(Pokemon att, double damage) {
        if (flag) att.setMod(Stat.HP, -((int)Math.round(damage) / 2));
    }
    @Override
    protected String describe() {
        if (flag) return "использует Dream Eater: наносит урон и востанавливает себе половину нанесенного урона";
        else return "ничего не делает";
    }
}

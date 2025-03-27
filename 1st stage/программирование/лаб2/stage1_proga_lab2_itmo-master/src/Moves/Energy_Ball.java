package Moves;
import ru.ifmo.se.pokemon.*;
public class Energy_Ball extends SpecialMove {
    public Energy_Ball() {
        super(Type.GRASS, 90, 100);
    }
    private boolean flag;
    @Override
    public void applyOppDamage(Pokemon def, double damage) {
        int chance = (int)(Math.random() * 101);
        if (chance <= 10) flag = true;
        if (flag) def.setMod(Stat.SPECIAL_DEFENSE, -1);
    }
    @Override
    protected String describe() {
        if (flag) return "повезло, покемон использует Energy Ball: уменьшает Специальную Защиту противника на 1 и наносит урон";
        else return "наносит урон";
    }
}
package Moves;
import ru.ifmo.se.pokemon.*;
public class Ancient_Power extends SpecialMove {
    public Ancient_Power() {
        super(Type.ROCK, 60, 100);
    }
    private boolean flag;
    @Override
    public void applySelfEffects(Pokemon p) {
        int chance = (int)(Math.random() * 101);
        if (chance <= 10) flag = true;
        if (flag) {
            p.setMod(Stat.ATTACK, +1);
            p.setMod(Stat.DEFENSE, +1);
            p.setMod(Stat.SPECIAL_ATTACK, +1);
            p.setMod(Stat.SPECIAL_DEFENSE, +1);
            p.setMod(Stat.SPEED, +1);
        }
    }
    @Override
    protected String describe() {
        if (flag) return "повезло, покемон использует Ancient Power: увеличивает свои характеристики на 1 и атакует";
        else return "наносит урон";
    }
}

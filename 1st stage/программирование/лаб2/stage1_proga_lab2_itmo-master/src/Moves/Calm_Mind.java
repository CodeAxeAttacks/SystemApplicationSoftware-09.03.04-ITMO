package Moves;
import ru.ifmo.se.pokemon.*;
public class Calm_Mind extends StatusMove {
    public Calm_Mind() {
        super(Type.PSYCHIC, 0, 0);
    }
    @Override
    public void applySelfEffects(Pokemon p) {
        p.setMod(Stat.SPECIAL_ATTACK, +1);
        p.setMod(Stat.SPECIAL_DEFENSE, +1);
    }
    @Override
    protected String describe() {
        return "использует Calm Mind: увеличивает Специальную Атаку и Специальную Защиту на 1";
    }
}
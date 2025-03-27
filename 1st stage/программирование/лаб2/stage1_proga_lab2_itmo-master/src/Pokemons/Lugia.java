package Pokemons;
import Moves.*;
import ru.ifmo.se.pokemon.*;
public class Lugia extends Pokemon {
    public Lugia(String name, int level) {
        super(name, level);
        setType(Type.PSYCHIC, Type.FLYING);
        setStats(106, 90, 130, 90, 154, 110);
        setMove(new Ancient_Power(), new Thunder(), new Thunder_Wave(), new Calm_Mind());
    }
}

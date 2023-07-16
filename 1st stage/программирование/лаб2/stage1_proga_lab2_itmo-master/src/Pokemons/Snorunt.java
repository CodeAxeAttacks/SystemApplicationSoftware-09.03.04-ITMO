package Pokemons;
import Moves.*;
import ru.ifmo.se.pokemon.*;
public class Snorunt extends Pokemon {
    public Snorunt(String name, int level) {
        super(name, level);
        setType(Type.ICE);
        setStats(50, 50, 50, 50, 50, 50);
        setMove(new Powder_Snow(), new Confide(), new Facade());
    }
}

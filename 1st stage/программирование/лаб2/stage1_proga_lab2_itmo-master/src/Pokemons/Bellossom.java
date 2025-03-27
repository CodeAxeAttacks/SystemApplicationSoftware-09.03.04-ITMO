package Pokemons;
import Moves.*;
import ru.ifmo.se.pokemon.*;
public class Bellossom extends Gloom {
    public Bellossom(String name, int level) {
        super(name, level);
        setType(Type.GRASS);
        setStats(75, 80, 95, 90, 100, 50);
        setMove(new Energy_Ball(), new Swagger(), new Acid(), new Swagger());
    }
}

package Pokemons;
import Moves.*;
import ru.ifmo.se.pokemon.*;
public class Gloom extends Oddish {
    public Gloom(String name, int level) {
        super(name, level);
        setType(Type.GRASS, Type.POISON);
        setStats(60, 65, 70, 85, 75, 40);
        setMove(new Energy_Ball(), new Swagger(), new Acid());
    }
}

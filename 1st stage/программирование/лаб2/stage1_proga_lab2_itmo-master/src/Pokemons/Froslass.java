package Pokemons;
import Moves.*;
import ru.ifmo.se.pokemon.*;
public class Froslass extends Snorunt {
    public Froslass(String name, int level) {
        super(name, level);
        setType(Type.ICE, Type.GHOST);
        setStats(70, 80, 70, 80, 70, 110);
        setMove(new Powder_Snow(), new Confide(), new Facade(), new Dream_Eater());
    }
}

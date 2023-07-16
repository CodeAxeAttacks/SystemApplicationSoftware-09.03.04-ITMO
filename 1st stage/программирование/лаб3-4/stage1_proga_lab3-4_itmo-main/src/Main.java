//lab3,4, var: 8500.141
public class Main {
    public static void main(String[] args) {
        Queen queen = new Queen();
        Narrator narrator = new Narrator();
        Alice alice = new Alice();
        Cards cards = new Cards();
        Sister sister = new Sister();
        Tree tree = new Tree();

        narrator.speak();
        narrator.smthHapenned();
        narrator.versus(queen, alice);
        narrator.wonder(Math.random());

        alice.start();
        alice.grow(Height.TINY_LITTLE);
        alice.grow(Height.TINY);
        alice.grow(Height.PETTIT);
        alice.grow(Height.SMALL);
        alice.grow(Height.REAL);

        cards.speak();
        try { alice.booze("Карты"); }
        catch (Alice.frightOutOfBounds exp) {
            System.out.println(exp.getMessage());
        }
        cards.speak();
        cards.fly();
        try { cards.battle("Алиса"); }
        catch (Cards.tooMuchCards ogg) {
            System.out.println(ogg.getMessage());
        }
        alice.Battle("Карты", cards.howMuch);

        narrator.stepBackToReality();

        alice.lie();
        tree.shareLeaves("Алиса", cards.howMuch);
        alice.Sleepable(alice.fright);
        sister.speak();
        sister.help();
        sister.Sleepable(alice.fright);
        alice.awakening();
        narrator.retelling();
        sister.react(alice.fright);
        narrator.postAction();

        narrator.summarize(Math.random());
    }
}
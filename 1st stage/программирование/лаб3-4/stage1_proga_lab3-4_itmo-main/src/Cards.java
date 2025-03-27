import jdk.jshell.spi.ExecutionControlProvider;

public class Cards extends Alive implements Speakable {
    public Cards() {
        super("Карты");
    }

    int howMuch = (int) (Math.random() * 99) + 1;

    @Override
    public void speak() {
        System.out.println("*Шелест карт*");
    }

    public void fly() {
        System.out.println(getName() + " в количестве " + howMuch + " единиц" + " взвивают в воздух на " + (howMuch % 4 + 1) + " метров");
    }

    public static class tooMuchCards extends Exception {
        public tooMuchCards(String message) {
            super(message);
        }
    }

    public void battle(String wh) throws tooMuchCards {
        howMuch += 2;
        if (howMuch > 0 & howMuch < 25) System.out.println(getName() + " атакуют в лицо " + wh + " опасаясь поражения");
        else if (howMuch > 25 & howMuch < 50) System.out.println(getName() + " атакуют в лицо " + wh + " понимая шансы проиграть");
        else if (howMuch > 50 & howMuch < 75) System.out.println(getName() + " атакуют в лицо " + wh + " с предвкушением победы");
        else if (howMuch > 75 & howMuch < 100) System.out.println(getName() + " атакуют в лицо " + wh + " уверенно, веря на все 100 в свою победу");
        else {
            throw new tooMuchCards(getName() + " атакуют в лицо " + wh + " не теряя надежду");
        }
        howMuch -= 2;
    }

}

public class Sister extends Alive implements Speakable, Sleepable {
    public Sister() {
        super("Сестра Алисы");
    }

    @Override
    public void speak() {
        System.out.println(getName() + " молчит");
    }

    @Override
    public void Sleepable(int lot) {
        String[] method = new String[5];
        method[0] = " нехотя, ведь она очень крепко спит";
        method[1] = " нехотя, ведь она крепко спит";
        method[2] = " охотно, ведь она не очень крепко спит";
        method[3] = " охотно, ведь она некрепко спит";
        method[4] = " желанно, ведь она тревожно спит";
        System.out.println(getName() + " будит Алису"  + method[lot]);
    }

    public void help() {
        System.out.println(getName() + " смахивает листья, летящие на сестрёнку");
    }

    public void react(int stat) {
        String[] how = new String[5];
        how[0] = " хорошим";
        how[1] = " интересным";
        how[2] = " занятным";
        how[3] = " страшным";
        how[4] = "-очень страшным";
        System.out.println(getName() + ": \"Сон и правда оказался очень" + how[stat] + "! Но уже пора торипиться, собирайся скорее");
    }
}

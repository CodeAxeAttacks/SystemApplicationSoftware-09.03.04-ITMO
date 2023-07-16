public class Queen extends Alive implements Speakable {
    public Queen() {
        super("Королева");
    }

    @Override
    public void speak() {
        String[] phrases = new String[10];
        phrases[0] = "Королева: \"Я тебе волосы вырву!\"";
        phrases[1] = "Королева: \"Не затыкай мне рот!\"";
        phrases[2] = "Королева: \"Я в ярости\"";
        phrases[3] = "Королева: \"Не испытывай мои нервы\"";
        phrases[4] = "Королева: \"Не говори глупостей\"";
        phrases[5] = "Королева: \"Постыдись своей глупости\"";
        phrases[6] = "Королева: \"Я тебе голову отрублю!\"";
        phrases[7] = "Королева: *Крик во всю глотку*";
        phrases[8] = "Королева: \"Стой где стоишь!\"";
        phrases[9] = "Королева: \"Не шевелись!\"";
        System.out.println(phrases[(int) (Math.random() * 9)]);
    }
}

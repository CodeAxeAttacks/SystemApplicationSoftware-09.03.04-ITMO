public class Alice extends Alive implements Growable, Speakable, Battle, Sleepable {
    public Alice() {
        super("Алиса");
    }

    int fright = (int) (Math.random() * 4);

    @Override
    public void speak() {
        String[] words = new String[10];
        words[0] = "Алиса: \"Я тебе волосы вырву!\"";
        words[1] = "Алиса: \"Не затыкай мне рот!\"";
        words[2] = "Алиса: \"Я в ярости\"";
        words[3] = "Алиса: \"Не испытывай мои нервы\"";
        words[4] = "Алиса: \"Не говори глупостей\"";
        words[5] = "Алиса: \"Постыдись своей глупости\"";
        words[6] = "Алиса: \"Я тебе голову отрублю!\"";
        words[7] = "Алиса: *Крик во всю глотку*";
        words[8] = "Алиса: \"Стой где стоишь!\"";
        words[9] = "Алиса: \"Не шевелись!\"";
        System.out.println(words[(int) (Math.random() * 9)]);
    }

    @Override
    public void grow(Height height) {
        System.out.print(getName() + " достигла");
        switch(height) {
            case TINY_LITTLE:
                System.out.println(" совсем крохотного роста!");
                break;
            case TINY:
                System.out.println(" крохотного роста!");
                break;
            case PETTIT:
                System.out.println(" малюсенького роста!");
                break;
            case SMALL:
                System.out.println(" маленького роста!");
                break;
            case REAL:
                System.out.println(" реального роста!");
                break;
            default:
                break;
        }
    }

    public class frightOutOfBounds extends IndexOutOfBoundsException {
        public frightOutOfBounds(String message) {
            super(message);
        }
    }

    public void booze(String target) throws frightOutOfBounds {
        fright++;
        String[] egg = new String[5];
        egg[0] = "Жалкие " + target + "! Я вас совсем не боюсь!";
        egg[1] = "Милые " + target + "! Я бы вас затискала";
        egg[2] = "Не такие уж вы и жалкие " + target +"Я готова сразиться!";
        egg[3] = "Я боюсь вас, " + target + "! Кто-нибудь, помогите мне!";
        egg[4] = "Я очень сильно боюсь вас, " + target + "! Мне очень страшно!";
        if (fright >= 0 & fright <= 4) System.out.println(egg[fright]);
        else {
            fright--;
            throw new frightOutOfBounds("А вот программа и сломалась!");
        }
    }

    public void start() {
        System.out.println(getName() + " начинает расти");
    }

    @Override
    public void Battle(String object, int num) {
        if (num > 0 & num < 25) {
            System.out.println(getName() + " стала отбиваться от " + object + " смеясь, ведь их совсем мало");
            fright = 0;
        }
        else if (num > 25 & num < 50) {
            System.out.println(getName() + " стала отбиваться от " + object + " с небольшой опаской");
            fright = 1;
        }
        else if (num > 50 & num < 75) {
            System.out.println(getName() + " стала отбиваться от " + object + " крича, их так много!");
            fright = 2;
        }
        else if (num > 75 & num < 100) {
            System.out.println(getName() + " стала отбиваться от " + object + " рыдая, их слишком много!");
            fright = 4;
        }
        else {
            System.out.println(getName() + " стала отбиваться от " + object + " не понимая, что происходит");
            fright = 3;
        }
    }

    public void lie() {
        String[] place = new String[4];
        place[0] = " берегу реки";
        place[1] = " листве в парке";
        place[2] = " опушке в лесу";
        place[3] = " на траве";
        System.out.println(getName() + " лежит на" + place[(int) (Math.random() * 2) + 1]);
    }

    @Override
    public void Sleepable(int lot) {
        String[] way = new String[5];
        way[0] = " очень крепко";
        way[1] = " крепко";
        way[2] = " не очень крепко";
        way[3] = " некрепко";
        way[4] = " тревожно";
        System.out.println(getName() + way[fright] + " спит");
    }

    public void awakening() {
        String[] type = new String[5];
        type[0] = " прекрасный";
        type[1] = " нормальный";
        type[2] = " странный";
        type[3] = " ужасный";
        type[4] = " кошмарный";
        System.out.println(getName() + ": \"Мне приснился" + type[fright] + " сон\"");
    }
}

public class Narrator extends Alive implements Speakable {
    public Narrator() {
        super("Рассказчик");
    }

    public void versus(Speakable q, Speakable a) {
        int x = 0;
        int y = 0;
        for (int i = 0; i <= 2 * (int) (Math.random() * 5) + 5; i++) {
            int r = (int) (Math.random() + 0.5);
            if (r == 1) {
                a.speak();
                x++;
            }
            else {
                q.speak();
                y++;
            }
        }
        if (x > y) System.out.println("В этой словесной перепалке победила Алиса");
        else System.out.println("В этой словесной перепалке победила Королева");
    }

    @Override
    public void speak() {
        String[] mas = new String[5];
        mas[0] = " очень давно";
        mas[1] = " давным-давно";
        mas[2] = " совсем недавно";
        mas[3] = " пару дней назад";
        mas[4] = " больше 1000 лет назад";
        System.out.println("Случилось это" + mas[(int) (Math.random() * 3) + 1]);
    }

    public void wonder(double shock) {
        String[] arr = new String[6];
        arr[0] = "Вау! Посмотрите!!!";
        arr[1] = "Ничего необычного, так происходит при каждом запуске программы" + "\n" + "Сами посмотрите:";
        arr[2] = "Это просто невероятно! Я никогда ещё не видел ничего подобного! Смотрите:";
        arr[3] = "Бесподобно! Вы только посмотрите!";
        arr[4] = "Все сюда! Что-то неладное творится! *Раздаётся звон колокола*";
        arr[5] = "Ой, что это?";
        System.out.println(arr[(int) (shock * 4) + 1]);
    }

    public void summarize(double mood) {
        String[] end = new String[6];
        end[0] = "Вот и сказочке конец, а кто слушал – молодец";
        end[1] = "Сказка ложь, да в ней намёк: добрым молодцам урок";
        end[2] = "А вот что произошло дальше – это уже совсем другая история";
        end[3] = "Выводы делайте сами, потому что я ничего не понял";
        end[4] = "Сказка кончилась, это печально";
        end[5] = "Конец!";
        System.out.println(end[(int) (mood * 4) + 1]);
    }

    public void stepBackToReality() {
        System.out.println("Яркая вспышка, громкий звук: все резко вернулись в реальность");
    }

    public void smthHapenned() {
        System.out.println("<...>");
    }

    public void retelling() {
        System.out.println("Алиса принялась рассказывать про сон, который ей приснился, про все её приключения");
    }

    public void postAction() {
        System.out.println("Алиса собралась, попрощалась с сестрой и убежала по своим делам" + '\n' + "Такой сон она никогда не забудет!");
    }
}

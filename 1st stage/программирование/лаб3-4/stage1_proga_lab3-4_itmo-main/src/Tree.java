public class Tree extends Alive {
    public Tree() {
        super("Дерево");
    }


    public void shareLeaves(String forWhom, int sum) {
        System.out.println(getName() + " осыпает лицо " + forWhom + " " + sum + " единицами листьев");
    }
}

package Support;

public class Discipline {
    private String name;
    private int selfStudyHours;

    public String getName() {
        return name;
    }

    public Discipline(String name, int selfStudyHours) {
        this.name = name;
        this.selfStudyHours = selfStudyHours;
    }

    public int getSelfStudyHours() {
        return selfStudyHours;
    }

    public void setName(String name1) {
        this.name = name1;
    }

    public void setSelfStudyHours(int selfStudyHours) {
        this.selfStudyHours = selfStudyHours;
    }
}
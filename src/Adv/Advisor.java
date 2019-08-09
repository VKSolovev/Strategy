package Adv;

public class Advisor {
    protected String name;
    protected int level = ((int) (Math.random() * 15)) % 10;
    protected int ability = 0;
    protected int age = (int) (Math.random() * 40) + 100;
    protected int haveJob;
    protected int numberOfPossibleChar;
    // выводить возраст надо деля на 4

    //список возможных бафов
    protected int modBuildingCost;
    protected int modTactic;

    // клерик
    protected int rebeltLevel = 0;

    // генерал
    protected int modFire = 0;
    protected int modShock = 0;


    public boolean Death(){
        return (Math.random() * 1000000 - (age*age*age)/64 > 0);
    }
    public void AgeUp(){
        age++;
    }

    public int getAge() {
        return age/4;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getAbility() {
        return ability;
    }

    public void setHaveJob(int haveJob) {
        this.haveJob = haveJob;
    }

    public int getHaveJob() {
        return haveJob;
    }

    public int getModBuildingCost() {
        return modBuildingCost;
    }

    public int getModTactic() {
        return modTactic;
    }

    public int getModFire() {
        return modFire;
    }

    public int getModShock() {
        return modShock;
    }
}

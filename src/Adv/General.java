package Adv;

public class General extends Adv.Advisor {
    private int fire;
    private int shock;
    private int tactic;
    private int mobility;
    private int[] bonus = new int[4];
    private int baseNumberOfAdvisorChar = 3;
    private int inArmy = -1;

    // тут идут характеристики генерала

    public General() {
        this.tactic = level;
        this.fire = level + ((int) (Math.random()) * 6 - 3);
        if (fire < 0){
            fire = 0;
        }
        this.shock = level + ((int) (Math.random()) * 6 - 3);
        if (shock < 0){
            shock = 0;
        }
        this.mobility = level + ((int) (Math.random()) * 6 - 3);
        if (mobility < 0){
            mobility = 0;
    }
        bonus[(int) (Math.random() * 4)] = 1;
        name = "Ildar Zagretdinov";
        ability = (int) (Math.random() * baseNumberOfAdvisorChar);
        if (ability == 0){
            tactic = 2;
        }
        if (ability == 1){
            modFire = 10;
        }
        if (ability == 2){
            modShock = 10;
        }
    }

    public int getFire() {
        return fire;
    }

    public int getShock() {
        return shock;
    }

    public int getTactic() {
        return tactic;
    }

    public int getMobility() {
        return mobility;
    }

    public int[] getBonus() {
        return bonus;
    }

    public int getInArmy() {
        return inArmy;
    }

    public void setInArmy(int inArmy) {
        this.inArmy = inArmy;
    }
}

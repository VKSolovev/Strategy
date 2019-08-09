package Adv;

public class Cleric extends Adv.Advisor {
    private int baseNumberOfAdvisorChar = 1;

    public Cleric() {
        name = "Ildar Zagretdinov";
        ability = (int) (Math.random() * baseNumberOfAdvisorChar);
        if (ability == 0){
            rebeltLevel = -4;
        }
    }

    public String GetAbility(){
        if (ability == 0) {
            return "Уровень восстаний -4";
        } else {
            return null;
        }
    }
}

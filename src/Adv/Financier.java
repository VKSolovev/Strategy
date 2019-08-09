package Adv;

public class Financier extends Adv.Advisor  {
    private int baseNumberOfAdvisorChar = 1;

    public Financier() {
        name = "Ildar Zagretdinov";
        ability = (int) (Math.random() * baseNumberOfAdvisorChar);
        if (ability == 0){
            modBuildingCost = 90;
        }

    }
}

package Adv;

public class Judge extends Adv.Advisor  {
    private int baseNumberOfAdvisorChar = 1;

    public Judge() {
        name = "Ildar Zagretdinov";
        ability = (int) (Math.random() * baseNumberOfAdvisorChar);

    }
}

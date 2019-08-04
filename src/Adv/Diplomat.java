package Adv;

public class Diplomat extends Adv.Advisor  {
    private int baseNumberOfAdvisorChar = 1;

    public Diplomat() {
        name = "Ildar Zagretdinov";
        ability = (int) (Math.random() * baseNumberOfAdvisorChar);

    }
}

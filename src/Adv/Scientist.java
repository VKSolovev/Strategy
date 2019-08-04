package Adv;

public class Scientist extends Adv.Advisor  {
    private int baseNumberOfAdvisorChar = 1;

    public Scientist() {
        name = "Ildar Zagretdinov";
        ability = (int) (Math.random() * baseNumberOfAdvisorChar);

    }
}

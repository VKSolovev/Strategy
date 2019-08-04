package Adv;

public class Spy extends Adv.Advisor  {
    private int baseNumberOfAdvisorChar = 1;

    public Spy() {
        name = "Ildar Zagretdinov";
        ability = (int) (Math.random() * baseNumberOfAdvisorChar);

    }
}

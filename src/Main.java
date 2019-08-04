import Adv.General;
import Estate.Estate;
import Estate.Manufactor;
import Estate.Generals;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        Estate[] estates = new Estate[2];
        estates[0] = new Manufactor();
        estates[1] = new Generals();
        System.out.println(estates[1].getAbilityName());
    }

}

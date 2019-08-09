package Estate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Generals extends Estate{
    private int modRebelLevel = 0;
    private int modTactic = 0;
    private int numAbility = 3;

    private static List<String> abilityName = new ArrayList<>();
    static {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                new FileInputStream("src\\Estate\\Texts\\AbilityNames\\Generals")))) {
            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                abilityName.add(nextLine);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Generals() {
        ability = new Ability[numAbility];
        for (int i = 0; i < numAbility; i++){
            ability[i] = new Ability();
        }
    }
    @Override
    public String getBonus(){
        return "Уровень восстания" + rebelLevel + "Тактика" + tactic;
    }
    @Override
    public void ActivateAbility(int num){
        if (num == 0 && ability[0].getTime() == 0) {
            ability[num].Activate(10);
            modTactic = 3;
            power += 100;
        }
        if (num == 1 && ability[1].getTime() == 0){
            ability[num].Activate(10);
            power += 200;
            loyality += 100;
            modRebelLevel = -5;
        }
        if (num == 2 && ability[2].getTime() == 0){
            general = true;
            ability[num].Activate(15);
            power += 100;
            loyality -= 50;
        }
    }
// тут одновляем бонусы от сословия и убираем бонусы от абилок
    @Override
    public void UpdateBonus(){
        if (ability[0].getTime() == 5){
            modTactic = 0;
            power -= 50;
        }
        if (ability[1].getTime() == 5){
            modRebelLevel = 0;
        }

        if (partOfPover >= 500 && loyality >= 300){
            tactic = 3 + modTactic;
            rebelLevel = 3 + modRebelLevel;
            profit = 8;
        } else if (partOfPover >= 400 && loyality >= 300) {
            tactic = 3 + modTactic;
            rebelLevel = -1 + modRebelLevel;
            profit = 9;
        } else if (partOfPover >= 300 && loyality >= 300){
                tactic = 3 + modTactic;
                rebelLevel = - 3 + modRebelLevel;
                profit = 10;
        }  else if (partOfPover >= 100 && loyality >= 300){
            tactic = 2 + modTactic;
            rebelLevel = - 2 + modRebelLevel;
            profit = 10;
        } else if (partOfPover < 100 && loyality >= 300){
            tactic = 1 + modTactic;
            rebelLevel = -1 + modRebelLevel;
            profit = 10;
        } else if (loyality < 100){
            tactic = -5;
            rebelLevel = 10;
            profit = 8;
        } else if (loyality < 200){
            tactic = -2;
            rebelLevel = 5;
            profit = 9;
        } else {
            tactic = 0;
            rebelLevel = 0;
            profit =10;
        }

    }

    public List<String> getAbilityName() {
        return abilityName;
    }
}

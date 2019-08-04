import Functional.Maps.CityAttack;
import Functional.Maps.CityCoordinate;
import Functional.Maps.MapOfArmies;
import Functional.Maps.Position;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//* Название ресурсов mineral RR CR
public class World {
    public World() throws IOException {

        //создаем карту для аттак городов
        ArrayList<Position> pos = new ArrayList<>();
        ArrayList<CityCoordinate> coord = new ArrayList<>();
        for (int i = 0; i < country.size(); i++){
            for (int j = 0; j < country.get(i).getRegionControl().size(); j++){
                for (int k = 0; k <country.get(i).getRegionControl().get(j).getCity().length; k++){
                    pos.add(country.get(i).getRegionControl().get(j).getCity()[k].getPosition());
                    coord.add(new CityCoordinate(i, j, k));
                }
            }
        }
        Position[] positions = new Position[pos.size()];
        CityCoordinate[] cityCoordinates = new CityCoordinate[coord.size()];
        for (int i = 0; i < pos.size(); i++){
            positions[i] = pos.get(i);
            cityCoordinates[i] = coord.get(i);
        }
        CityAttack cityAttack = new CityAttack(positions, cityCoordinates);
    }

    // базовые настройки
    public  static int ammountMod = 22;

    public static int baseProfitFromCity = 1;
    public static int baseProfitFromRegion = 1;
    public static int baseProfitFromMineral = 1;
    public static int baseProfitFromProduction = 1;
    public static int baseCostArmy = 1;
    public static int baseCostAdm = 1;
    public static int baseAdvisorCost = 1;
    public static int baseInterest = 3;
    public static int baseAutonomy = 1;

    public static int numberOfEvent;

    public static int baseNumberOfEstates;
    public static int baseLoyalityIncrease = 1;

    public static int baseAdm = 5;
    public static int basePrestige = 5;
    public static int baseLegicimacy = 50;


    public static int numberOfCR = 1;
    public static int numberOfRR = 1;
    public static int numberOfMineral = 1;
    public static int numberOfBuildings = 1;
    public static int[] baseCostBuild = new int[numberOfBuildings];
    public static int baseCostPlant = 1;
    public static int baseCostInfrasructure = 1;

    public static int baseMaxMovement = 5;
    public static int[] equipmentOfSquade = {1000, 1200, 1200, 1500, 1700, 2000, 2000, 1000};
    public static int baseDamage = 5;
    public static int[] baseCostCreationSquad;
    public static int baseMobilisation;
    public static int baseCostMobilisation;

    public static int possibleAdvisors = 15;

    public static int baseChanceOfChangingReligion = 2;
    public static int baseChanceOfChangingCulture = 5;

    // компонены мира
    private boolean endGame = false;
    private ArrayList<Gov> country = new ArrayList<>();
    private int totalPopulation;
    private CityAttack cityAttack;

    public static int heigthOfMap = 5;
    public static int wideOfMap = 5;

    public static MapOfArmies mof = new MapOfArmies(wideOfMap, heigthOfMap);

    public static List<String> lines;

    static {
        try {
            lines = Files.readAllLines(Paths.get("src\\Texts\\GovModificator"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // цены на ресурсы

    public static int[] valueRR = new int[numberOfRR];
    public static int[] valueMineral = new int[numberOfMineral];
    public static int[] valueCR = new int[numberOfCR];

    private int[] baseValueCR =  new int[numberOfCR];
    private int[] baseValueRR = new int[numberOfRR];
    private int[] baseValueMineral = new int[numberOfMineral];

    // производство ресурсов
    public static int[] totalCityProduction = new int[numberOfCR];
    public static int[] totalRegionProduction = new int[numberOfRR];
    public static int[] totalMineralProduction = new int[numberOfMineral];

    // спрос на ресурсы
    public static int[] totalPlantCRDemand = new int[numberOfCR];
    public static int[] totalPlantRRDemand = new int[numberOfRR];
    public static int[] totalPlantMineralDemand = new int[numberOfMineral];

    private int[] basePopulationRRDemand = new int[numberOfRR];
    private int[] basePopulationMineralDemand = new int[numberOfMineral];
    private int[] basePopulationCRDemand = new int[numberOfCR];

    // Служебное
    public static boolean otladka = true;
    // штука, которая дает массив заводов



    //не знаю где это оставить, поэтому пусть будут тут
    // Находит 2 армии по координатам и сталкивает их. Можно сократить конечно количество опреций, тк мы знаем первую страну, но есть варик делать
    // и все перемещения через карту армий
    public void MoveArmy(Army army, Position second){
        if ((!cityAttack.CheckPosition(second)) && army.CheckMove(second)){
            if (mof.CheckPosition(second) == -1){
                army.Move(second);
                //проверяем алекватная ли клетка
                if (cityAttack.CheckPositionCityAttack(second) == null |
                        cityAttack.CheckPositionCityAttack(second).getCountry() == army.getCountry()){
                } else { // если нет,то идет захват города
                    int co = cityAttack.CheckPositionCityAttack(second).getCountry();
                    int re = cityAttack.CheckPositionCityAttack(second).getRegion();
                    int ci = cityAttack.CheckPositionCityAttack(second).getCity();
                    Position cit = country.get(co).getRegionControl().get(re).getCity()[ci].getPosition();
                    boolean defend = false;
                    for (int i = -1; i <2; i++){
                        for (int j = -1; j<2; j++){
                            Position posi = new Position(cit.GetX() + i, cit.GetY() +j);
                            if (mof.CheckPosition(posi) == co){
                                defend = true;
                            }
                        }
                    }
                    boolean regionAttack = true;
                    if (!defend){
                        country.get(co).getRegionControl().get(re).getCity()[ci].setOwner(army.getCountry());
                        for (int k = 0; k < country.get(co).getRegionControl().get(re).getCity().length; k++){
                            if (country.get(co).getRegionControl().get(re).getCity()[k].getOwner() != army.getCountry()){
                                regionAttack = false;
                            }
                        }
                        if (regionAttack){
                            if (country.get(army.getCountry()).getRegion().contains(country.get(co).getRegionControl().get(re))){
                                country.get(co).getRegionControl().get(re).setOccupation(false);
                            } else {
                                country.get(co).getRegionControl().get(re).setOccupation(true);
                            }
                            country.get(army.getCountry()).getRegionControl().add(country.get(co).getRegionControl().get(re));
                            country.get(co).getRegionControl().remove(country.get(co).getRegionControl().get(re));
                        }
                    }
                }
            //тут надо прописать дипломатическую атаку. Пока что дипломатии нет и просто атака всех армий,
            } else if (mof.CheckPosition(second) != army.getCountry()) {
                Battle(army.getPosition(), second);
            }
        }
    }


    private void Battle(Position position, Position battle){
        for (int j = 0; j < country.get(mof.CheckPosition(position)).army.size() ; j++){
            if (country.get(mof.CheckPosition(position)).army.get(j).getPosition() == position){
                for (int k = 0; k < country.get(mof.CheckPosition(battle)).army.size() ; k++){
                    if (country.get(mof.CheckPosition(battle)).army.get(j).getPosition() == battle){
                        boolean win = Fight(country.get(mof.CheckPosition(position)).army.get(j), country.get(mof.CheckPosition(battle)).army.get(j));
                        if (win){
                            int regi = (int) (Math.random() * country.get(mof.CheckPosition(battle)).getRegionControl().size());
                            if (country.get(mof.CheckPosition(battle)).getRegionControl().get(regi).getCity()[0].CheckPosition()){
                                country.get(mof.CheckPosition(battle)).army.get(j).
                                        Move(country.get(mof.CheckPosition(battle)).getRegionControl().get(regi).getCity()[0].getPosArmy());
                            } else {
                                country.get(mof.CheckPosition(battle)).army.remove(j);
                            }

                        } else {
                            int regi = (int) (Math.random() * country.get(mof.CheckPosition(position)).getRegionControl().size());
                            if (country.get(mof.CheckPosition(position)).getRegionControl().get(regi).getCity()[0].CheckPosition()){
                                country.get(mof.CheckPosition(position)).army.get(j).
                                        Move(country.get(mof.CheckPosition(position)).getRegionControl().get(regi).getCity()[0].getPosArmy());
                            } else {
                                country.get(mof.CheckPosition(position)).army.remove(j);
                            }
                        }
                    }
                }
            }
        }

    }
    //функция, которая позволяет сражаться двум рамиям
    private boolean Fight(Army army1, Army army2) {
        int country1 = army1.getCountry();
        int country2 = army2.getCountry();
        int baseAmmount1 = army1.getAmount();
        int baseAmmount2 = army2.getAmount();
        int baseOrganisation1 = army1.getOrganization();
        int baseOrganisation2 = army2.getOrganization();
        army1.UpdateSF();
        army2.UpdateSF();
        while ((army1.getMorale() > 100) && (army2.getMorale() > 100)){
            army1.Lose(100 - (army2.getFire() *(100 + country.get(country2).getModFire()) * army2.getEquipment()/ army1.getEquipment()/ army1.getAmount()) / 2 - baseDamage);
            army2.Lose(100 - (army1.getFire() *(100 + country.get(country1).getModFire()) * army1.getEquipment()/ army2.getEquipment()/ army2.getAmount()) / 2 - baseDamage);
            army1.UpdateSF();
            army2.UpdateSF();
            if ((army1.getMorale() > 100) && (army2.getMorale() > 100)) {
                army1.Lose(100 - (army2.getShock() * (100 + country.get(country2).getModShock()) / army1.getAmount()) / 2 - baseDamage);
                army2.Lose(100 - (army1.getShock() * (100 + country.get(country1).getModShock()) / army2.getAmount()) / 2 - baseDamage);
                army1.UpdateSF();
                army2.UpdateSF();
            }
        }
        // теперь проводим обновление обмундирования
        if (army1.getMorale()>army2.getMorale()){
            int j = (400 * army1.getAmount() / baseAmmount1 / 3 + army2.getAmount()/baseAmmount2 / 3);
            army1.setOrganization(baseOrganisation1);
            if (j > 100){
                j = 100;
            }
            army1.LoseEquipment(j);
            army1.WinBattle();
            int lose = army2.LoseBattle();
            if (lose == 1){
                army2.LoseEquipment(300 * army2.getAmount() / baseAmmount2 / 3);
            } else if (lose == 2){
                army2.LoseEquipment(100 * army2.getAmount() / baseAmmount2 / 3);
            } else if (lose == 3){
                army2.LoseEquipment(50 * army2.getAmount() / baseAmmount2 / 3);
            } else if (lose == 4){
                country.get(country2).army.remove(army2);
            }
            return true;
        } else{
            int j = (400 * army2.getAmount() / baseAmmount2 / 3 + army1.getAmount()/baseAmmount1 / 3);
            army2.setOrganization(baseOrganisation2);
            if (j > 100){
                j = 100;
            }
            army2.LoseEquipment(j);
            army2.WinBattle();
            int lose = army1.LoseBattle();
            if (lose == 1){
                army1.LoseEquipment(300 * army1.getAmount() / baseAmmount1 / 3);
            } else if (lose == 2){
                army2.LoseEquipment(100 * army1.getAmount() / baseAmmount1 / 3);
            } else if (lose == 3){
                army2.LoseEquipment(50 * army1.getAmount() / baseAmmount1 / 3);
            } else if (lose == 4){
                country.get(country1).army.remove(army1);
            }
            return false;
        }

    }

    // обнуление массива
    private void NullArray(int[] array){
        for (int j = 0; j < array.length; j++){
            array[j] = 0;
        }
    }
    // считаем цены на ресурсы
    public void Market(){
        //считаем цены на региональные ресурсы
        for (int j = 0; j < numberOfRR; j++) {
            if (totalRegionProduction[j] != 0) {
                valueRR[j] = (totalPlantRRDemand[j] + totalPopulation * basePopulationRRDemand[j]) * baseValueRR[j] / totalRegionProduction[j];
            } else {
                valueRR[j] = 0;
            }
        }
        //считаем цены на ископаемые ресурсы
        for (int j = 0; j < numberOfCR; j++) {
            if (totalMineralProduction[j] != 0) {
                valueMineral[j] = (totalPlantMineralDemand[j] + totalPopulation * basePopulationMineralDemand[j]) * baseValueMineral[j] / totalMineralProduction[j];
            } else {
                valueMineral[j] = 0;
            }
        }
        // считаем цены на городские товары
        for (int j = 0; j < numberOfCR; j++) {
            if (totalCityProduction[j] != 0) {
                valueCR[j] = (totalPlantCRDemand[j] + totalPopulation * basePopulationCRDemand[j]) * baseValueCR[j] / totalCityProduction[j];
            } else {
                valueCR[j] = 0;
            }
        }
        //Обнуляем разные важные массивы
        NullArray(totalCityProduction);
        NullArray(totalMineralProduction);
        NullArray(totalRegionProduction);
        NullArray(totalPlantCRDemand);
        NullArray(totalPlantRRDemand);
        NullArray(totalPlantMineralDemand);
    }

    private void preTurn(int i){
        country.get(i).MakeMoney();
    }
    private void afterTurn(int i){
        country.get(i).UpdatePD();
    }

    public void Main() {
        int i = -1;
        while (!endGame){
            i++;
            if (i == country.size()){
                Market();
            }
            preTurn(i);
            // тут как раз начинается ход игрока
            //тут он кончается
            afterTurn(i);
        }
    }
}
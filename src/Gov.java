import Adv.*;
import Adv.Advisor;
import Estate.Estate;
import Estate.Generals;
import Estate.Manufactor;
import Functional.Debt;
import Functional.Maps.Position;
import Functional.Modificator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Gov {
    public Gov(){
        estate[0] = new Generals();
        estate[1] = new Manufactor();

        // создаем модификторы из файла
        int i =1;
        int k;
        int j = 0;
        List<String> string = World.lines;
        while (!string.get(i).equals("end")){
            k = i;
            i += 4;
            int m = 0;
            int[][] mods = new int[2][Integer.parseInt(string.get(k+3))];
            while(!string.get(i).equals("stop")){
                mods[0][m] = Integer.parseInt(string.get(i));
                i++;
                mods[1][m] = Integer.parseInt(string.get(i));
                i++;
                m++;
            }
            modificator[j] = new Modificator(Integer.parseInt(string.get(k)), string.get(k+1), Integer.parseInt(string.get(k+2)),
                    World.ammountMod, mods[0], mods[1]);
            j++;
            i++;
        }
    }

    // мдификаторы
    private int modAdvisorCost = 0;
    private int modBuildingCost = 0;
    private int modArmyCreation = 0;
    private int modRebel = 0;
    private int modInterest = 0;
    private int modPrestige = 0; // из 10000
    private int modLegecimacy = 0; // из 10000
    private int modAdm = 0;
    private int modAutonomy = 0;
    private boolean isPlayer = true;
    private int profitFromEstates = 10;
    private int[] powerIncrease = new int[World.baseNumberOfEstates];
    private int[] loyalityIncrease = new int[World.baseNumberOfEstates];
    private int modShock; //в процентах
    private int modFire;
    private int modTactic;
    private int modMorale;
    private int modOrganisation;
    private int modIncreaseMorale;
    private int modIncreaseOrganisation;

    private int modExchangeReligion;
    private int modExchangeCulture;
    private int modProfitFromProduction;
    private int modProfitFromRegion;
    private int modProfitFromMineral;
    private int modProfitFromCity;
    private int modCityInfrastructure = 0;

    private int modCostAdm = 1;
    private int modCostArmy = 1;

    private Modificator[] modificator = new Modificator[1];
// обновляем все моды; сначала обнуляем затем добавляем во всех структурах, которые влияют на них
    public void UpdateMod(){
        modAdvisorCost =0;
        modBuildingCost =0;
        modArmyCreation =0;
        modRebel =0;
        modInterest =0;
        modPrestige =0;
        modLegecimacy =0;
        modAdm =0;
        modAutonomy =0;
        modIncreaseMorale = 0;
        modIncreaseOrganisation = 0;

        modShock =0;
        modFire =0;
        modTactic =0;
        modMorale =0;
        modOrganisation =0;
        modExchangeReligion =0;
        modExchangeCulture =0;
        modProfitFromProduction =0;
        modProfitFromRegion =0;
        modProfitFromMineral =0;
        modProfitFromCity =0;

        for (int i = 0; i < modificator.length; i++){
            if (modificator[i].getIs()) {
                modAdvisorCost += modificator[i].getModificator()[0];
                modBuildingCost += modificator[i].getModificator()[1];
                modArmyCreation += modificator[i].getModificator()[2];
                modRebel += modificator[i].getModificator()[3];
                modInterest += modificator[i].getModificator()[4];
                modPrestige += modificator[i].getModificator()[5];
                modLegecimacy += modificator[i].getModificator()[6];
                modAdm += modificator[i].getModificator()[7];
                modAutonomy += modificator[i].getModificator()[8];
                modIncreaseMorale += modificator[i].getModificator()[9];
                modIncreaseOrganisation += modificator[i].getModificator()[11];

                modShock += modificator[i].getModificator()[12];
                modFire += modificator[i].getModificator()[13];
                modTactic += modificator[i].getModificator()[14];
                modMorale += modificator[i].getModificator()[15];
                modOrganisation += modificator[i].getModificator()[16];
                modExchangeReligion += modificator[i].getModificator()[17];
                modExchangeCulture += modificator[i].getModificator()[18];
                modProfitFromProduction += modificator[i].getModificator()[19];
                modProfitFromRegion += modificator[i].getModificator()[20];
                modProfitFromMineral += modificator[i].getModificator()[21];
                modProfitFromCity += modificator[i].getModificator()[22];

            }

        }
        for (int i = 0; i < advList.size(); i++){
            if (advList.get(i).getHaveJob() > 0) {
                modBuildingCost += advList.get(i).getModBuildingCost();
                modTactic += advList.get(i).getModTactic();
                modShock += advList.get(i).getModShock();
                modFire += advList.get(i).getModFire();
            }
        }

        for (int i = 0; i < estate.length; i++){
            if (estate[i].getIsInLobby() == 1) {
                modBuildingCost += estate[i].getModBuildingCost() * estate[i].getIsInLobby();
                modTactic += estate[i].getTactic();
                modProfitFromProduction += estate[i].getProfitFromProduction();
                modProfitFromRegion += estate[i].getProfitFromRegion();
                modProfitFromMineral += estate[i].getProfitFromMineral();
                modProfitFromCity += estate[i].getProfitFromCity();
                PlusMoney(estate[i].getPlusMoney() * profit / 10);
                estate[i].setPlusMoney(0);
                if (estate[i].isManufatory()){
                    int reg = (int) (Math.random() * regionControl.size());
                    int cit = (int) (Math.random() * regionControl.get(reg).getCity().length);
                    if (regionControl.get(reg).getCity()[cit].getPlant().size() != 0){
                        regionControl.get(reg).getCity()[cit].getPlant().get((int) (Math.random() * regionControl.get(reg).getCity()[cit].getPlant().size())).Upgrade();
                    } else {
                        regionControl.get(reg).getCity()[cit].newPlant((int) (Math.random() * World.numberOfCR));
                    }
                    estate[i].setManufatory(false);
                }
                if (estate[i].isFinancier()){
                    CreateAdvisor("Financier");
                    estate[i].setFinancier(false);
                }
                if (estate[i].isGeneral()){
                    CreateAdvisor("General");
                    estate[i].setFinancier(false);
                }
            }
        }

    }

    //доходы
    private int money;
    private int profitFromProduction;
    private int profitFromRegion;
    private int profitFromMineral;
    private int profitFromCity;
    private int profit;

    // расходы
    private int cost;
    private int costArmy;
    private int costAdm;


    // займы
    private int interest = World.baseInterest;
    private ArrayList<Debt> debt = new ArrayList<>();
    private int maxDebt;
    private int costDebt;

    // другие ресурсы
    private int adm;
    private int prestige; //от -10000 до 10000. Показываются только первые 3 цифры
    private int legicimacy; //по-разному. Пока что делаю для королевства также, как престиж

    // государственное устройство
    private int counryNum;
    private ArrayList<Region> regionControl;
    private ArrayList<Region> region;
    private Position capital;
    private int religion;
    private int culture;

    // армия
    public ArrayList<Army> army = new ArrayList<>();
    public ArrayList<Army> mobilisateArmy = new ArrayList<>();
    //чисто для призывной армии. Для всех остальных считается по другому в самой армии. Надо только будет сделать
    //кнопку для пополнения всех.
    private int maxEquipment;
    private int equipment;
    private int maxMobilisationArmy;


    // советники
    private ArrayList<Advisor> advList = new ArrayList<>();
    private ArrayList<General> general = new ArrayList<>();
    // призываем советника
    public void CreateAdvisor(String adv){
        if (adm > World.baseAdvisorCost * ( 100 + modAdvisorCost) /100){
            adm -= World.baseAdvisorCost * ( 100 + modAdvisorCost) /100;
            if (adv.equals("Diplomat")){
                advList.add(new Diplomat());
            }
            if (adv.equals("Cleric")){
                advList.add(new Cleric());
            }
            if (adv.equals("Financier")){
                advList.add(new Financier());
            }
            if (adv.equals("General")){
                General gen = new General();
                general.add(gen);
                advList.add(gen);
            }
            if (adv.equals("Judge")){
                advList.add(new Judge());
            }
            if (adv.equals("Scientist")){
                advList.add(new Scientist());
            }
            if (adv.equals("Spy")){
                advList.add(new Spy());
            }
        }
    }
    // назначаем советника  в ячейку number
    public void AssignAdvisor(int adv, int number){
        if (advList.get(AdvisorNumber(number)) == null){
            advList.get(adv).setHaveJob(number);
        } else {
            DismissAdvisor(AdvisorNumber(number));
            advList.get(adv).setHaveJob(number);
        }
    }
    // убираем советника
    public void DismissAdvisor(int adv){
        advList.get(adv).setHaveJob(0);
    }
    // убиваем советника
    public void KillAdvisor(int adv){
        advList.remove(adv);
    }
    // выдаем список незанятых советников
    public Integer[] GetUnasignAdvisors(){
        ArrayList<Integer> ar = new ArrayList<Integer>();
        for (int i = 0; i<advList.size(); i++){
            if (advList.get(i).getHaveJob() == 0){
                ar.add(i);
            }
        }
        return ar.toArray(new Integer[0]);
    }
    // выдаем советника стоящего на месте number
    public int AdvisorNumber(int number){
        for (int i = 0; i <advList.size(); i++){
            if (advList.get(i).getHaveJob() == number){
                return i;
            }
        }
        return -1;
    }
    public Advisor getAdv(int i){
        return advList.get(AdvisorNumber(i));
    }
    // Увеличение возраста всех советников
    public void UpAge(){
        int i = 0;
        while (i < advList.size()){
            advList.get(i).AgeUp();
            if (advList.get(i).Death()){
                KillAdvisor(i);
            } else {
                i++;
            }
        }
    }


    // Сословия
    private Estate[] estate = new Estate[2];
    public void UpdateEstate(){
        int commonPower = 0;
        for (int i =0; i < estate.length; i++){
            loyalityIncrease[i] = World.baseLoyalityIncrease;
            estate[i].setLoyalityIncrease(loyalityIncrease[i]);
            powerIncrease[i] = estate[i].getIsInLobby();
            estate[i].setPowerIncrease(powerIncrease[i]);
            estate[i].UpdateLP();
            commonPower += estate[i].getPower();
        }
        for (int i = 0; i<estate.length; i++){
            estate[i].setPartOfPover(estate[i].getPower() / (commonPower / 10));
            estate[i].UpdateBonus();
        }
    }
    public void ExchangeEstate(int newOne, int oldOne){
        estate[newOne].setPower(estate[oldOne].getPower() / 2 + estate[newOne].getPower());
        estate[oldOne].setPower(estate[oldOne].getPower() / 2);
    }

    private void NullArray(int[] array){
        for (int j = 0; j < array.length; j++){
            array[j] = 0;
        }
    }

    //обновляем производство и спрос на все ресурсы. Заодно обновляем религию, культуру и восстания
    public void UpdatePD() {
        for (Region value : regionControl) {
            World.totalRegionProduction[value.getResource()] += Math.min(value.getPopulation(), value.getSquareOfGround() * value.getEffectivity()) * World.baseProfitFromRegion;
            World.totalMineralProduction[value.getMineral()] += (5 + value.getInfrastructure()) * value.getBaseMineralProduction() * (50 + value.getProsperity()) * World.baseProfitFromMineral;
            int mod = 0; // модификатор восстания из-за культуры и религии
            if (culture != value.getCulture()) {
                mod += 2;
            }
            if (value.getReligion() != religion) {
                mod += 4;
            }
            value.UpdateRebelLevel(modRebel + mod);
            if (value.ExchangeReligion(World.baseChanceOfChangingReligion + modExchangeReligion) && value.getReligion() != religion) {
                value.setReligion(religion);
            }
            if (value.ExchangeCulture(World.baseChanceOfChangingCulture + modExchangeCulture) && value.getCulture() != culture) {
                value.setCulture(culture);
            }
            for (int j = 0; j < value.getCity().length; j++) {
                mod = 0;
                if (culture != value.getCulture()) {
                    mod += 2;
                }
                if (value.getCity()[j].getReligion() != religion) {
                    mod += 4;
                }
                value.getCity()[j].UpdateRebelLevel(modRebel + mod);
                value.getCity()[j].BuildingTurn();
                if (value.getCity()[j].ExchangeReligion(World.baseChanceOfChangingReligion + modExchangeReligion) && value.getCity()[j].getReligion() != religion) {
                    value.setReligion(religion);
                }
                for (int k = 0; k < value.getCity()[j].getPlant().size(); k++) {
                    World.totalCityProduction[value.getCity()[j].getPlant().get(k).getResourceOfPlant()] += value.getCity()[j].getPlant().get(k).getLevelOfPlant() * World.baseProfitFromProduction;
                    for (int l = 0; l < World.numberOfRR; l++) {
                        World.totalPlantRRDemand[l] += value.getCity()[j].getPlant().get(k).getDemand("RR", l);
                    }
                    for (int l = 0; l < World.numberOfMineral; l++) {
                        World.totalPlantMineralDemand[l] += value.getCity()[j].getPlant().get(k).getDemand("Mineral", l);
                    }
                    for (int l = 0; l < World.numberOfCR; l++) {
                        World.totalPlantCRDemand[l] += value.getCity()[j].getPlant().get(k).getDemand("CR", l);

                    }
                }
            }
        }
    }

    // обновляем базовый доход всего государства и максимальный заем. Обновляем автономию. Обновляем суммарную экипированность
    private void UpdateProfit () {
        profitFromRegion = 0;
        profitFromCity = 0;
        profitFromProduction = 0;
        maxEquipment = 0;
        profitFromCity = 0;
        for (int i = 0; i < regionControl.size(); i++){
            // обновляем доход от регионов
            int mod = 0; // модификатор автономии из-за культуры и религии
            if (culture != regionControl.get(i).getCulture()){
                mod +=10;
            }
            if (regionControl.get(i).getReligion() != religion){
                mod +=20;
            }
            int aut;
            aut = (int) (Math.tan((Math.pow(capital.GetX()- regionControl.get(i).getPosition().GetX(), 2) +
                    Math.pow(regionControl.get(i).getPosition().GetY() - capital.GetY(), 2)))
                    / Math.sqrt(Math.pow(World.heigthOfMap, 2) + Math.pow(World.wideOfMap, 2)) * World.baseAutonomy)+mod + modAutonomy;
            if (aut > 100){
                aut =100;
            }
            if (aut < 0 ){
                aut = 0;
            }
            regionControl.get(i).setAutonomy(aut);
            regionControl.get(i).UpdateProfitRR();
            regionControl.get(i).UpdateProfitMineral();
            if (regionControl.get(i).isOccupation()) {
                profitFromRegion += regionControl.get(i).getProfitRR() / 2;
                profitFromMineral += regionControl.get(i).getProfitMineral()/2;
            } else {
                profitFromRegion += regionControl.get(i).getProfitRR();
                profitFromMineral += regionControl.get(i).getProfitMineral();
            }
            for (int j = 0; j < regionControl.get(i).getCity().length; j++){
                maxEquipment += regionControl.get(i).getCity()[j].GetEquipment();
                mod = 0;
                if (culture != regionControl.get(i).getCulture()){
                    mod +=10;
                }
                if (regionControl.get(i).getCity()[j].getReligion() != religion){
                    mod +=20;
                }
                aut = (int) (Math.tan((Math.pow(capital.GetX()- regionControl.get(i).getCity()[j].getPosition().GetX(), 2) +
                        Math.pow(regionControl.get(i).getCity()[j].getPosition().GetY() - capital.GetY(), 2)))
                        / (Math.pow(World.heigthOfMap, 2) + Math.pow(World.wideOfMap, 2)) * World.baseAutonomy) + mod + modAutonomy;
                if (aut > 100){
                    aut =100;
                }
                if (aut < 0 ){
                    aut = 0;
                }
                regionControl.get(i).getCity()[j].setAutonomy(aut);
                //обновляем доход от городов
                regionControl.get(i).getCity()[j].UpdateProfitFromProduction();
                regionControl.get(i).getCity()[j].UpdateTax(modCityInfrastructure);
                if (regionControl.get(i).isOccupation()) {
                    profitFromProduction +=  regionControl.get(i).getCity()[j].getProfit() / 2;
                    profitFromCity += regionControl.get(i).getCity()[j].getTax() / 2;
                } else {
                    profitFromProduction +=  regionControl.get(i).getCity()[j].getProfit();
                    profitFromCity += regionControl.get(i).getCity()[j].getTax();
                }
            }
        }

        profitFromRegion *= 100 + modProfitFromRegion;
        profitFromCity *= 100 + modProfitFromCity;
        profitFromMineral *= 100 + modProfitFromMineral;
        profitFromProduction *= 100 + modProfitFromProduction;
        profitFromRegion /= 100;
        profitFromCity /= 100;
        profitFromMineral /= 100;
        profitFromProduction /= 100;

        profit = profitFromProduction + profitFromRegion + profitFromMineral + profitFromCity;
        maxDebt = 4 * profit;
    }
    //расходы - армия, бюрократия,

    //обновляем расходы на армию
    private void UpdateCostArmy () {
        costArmy = 0;
        for (Army value : army) {
            costArmy += value.GetCost();
        }
        costArmy *= World.baseCostArmy * modCostArmy;
    }
    // обновляем расход на бюрократию
    private void UpdateCostAdm () {
        int n = regionControl.size();
        for (Region value : regionControl) {
            n += value.getCity().length;
        }
        costAdm = n * World.baseCostAdm * modCostAdm;
    }
    // обновляем расход на долги. Можно использовать только раз в ход
    private void UpdateCostDebt () {
        costDebt = 0;
        for (int i = 0; i <debt.size(); i++){
            while (debt.get(i).getTime() == 0) {
                money -= debt.get(i).getSum();
                debt.remove(i);
            }
            if (i < debt.size()) {
                costDebt += debt.get(i).getSum() * debt.get(i).getInterest();
                debt.get(i).PayDay();
            }
        }
    }
    //обновляем все расходы
    private void UpdateCost() {
        UpdateCostArmy();
        UpdateCostAdm();
        UpdateCostDebt();

    }
    // этот класс сделан специально, чтобы пересчитывать при найме армии и других изменниях костов
    private void ReCountCost(){
        cost = costArmy + costAdm + costDebt;
    }
    // обновляем профит от сословий
    private void UpdateProfitFromEstates(){
        UpdateEstate();
        profitFromEstates = 1;
        for (int i = 0; i < estate.length; i++){
            if (estate[i].getIsInLobby() == 1) {
                profitFromEstates *= estate[i].getProfit();
            }
        }
    }
    //изменение казны
    public void MakeMoney() {
        UpAge();
        UpdateMod();

        UpdateArmy();

        UpdateAPL();
        UpdateProfitFromEstates();
        UpdateProfit();
        UpdateCost();
        ReCountCost();
        money += profit * profitFromEstates / 100000 - cost;
        while (!CheckMoney(0)){
            TakeDebt();
        }
        //надо сделать проверку на количество долгов и сделать банкротство вообще надо придуать, что надоделать
    }
    // проверка на наличие суммы денег
    public boolean CheckMoney( int number){
        if (number < money) {
            return false;
        } else{
            return true;
        }
    }
    // получаем деньги
    public void PlusMoney(int m){
        money += m;
    }
    //берем в долг
    public void TakeDebt(){
        debt.add(new Debt(maxDebt, interest, 10));
        money += maxDebt;
    }
    //обновляем другие ресурсы
    public void UpdateAPL() {
        adm += World.baseAdm + modAdm;
        prestige -= prestige * (100 - World.basePrestige) / 100 - modPrestige;
        legicimacy += World.baseLegicimacy + modLegecimacy;
    }

    // строительство
    // проверяем воможно ли построть
    public boolean posBuild(int numberOfRegion, int numberOfCity, int numberOfBuilding){
        if (CheckMoney(regionControl.get(numberOfRegion).getCity()[numberOfCity].CostOfBuilding(numberOfBuilding) * modBuildingCost / 100)){
            return true;
        } else{
            return false;
        }
    }
    // строим здание
    public void Build(int numberOfRegion, int numberOfCity, int numberOfBuilding){
        if (posBuild(numberOfRegion, numberOfCity, numberOfBuilding)){
            regionControl.get(numberOfRegion).getCity()[numberOfCity].Build(numberOfBuilding);
            PlusMoney(- regionControl.get(numberOfRegion).getCity()[numberOfCity].CostOfBuilding(numberOfBuilding) * modBuildingCost / 100);
        }
    }
    // улучшаем инфраструктуру города
    public int costInfr(int numberOfRegion, int numberOfCity){
        return regionControl.get(numberOfRegion).getCity()[numberOfCity].CostOfInfrastructure();
    }
    public boolean posBuildInfr(int numberOfRegion, int numberOfCity){
        return CheckMoney(costInfr(numberOfRegion, numberOfCity));
    }
    public void UpgradeInfr(int numberOfRegion, int numberOfCity){
        if (posBuildInfr(numberOfRegion, numberOfCity)){
            regionControl.get(numberOfRegion).getCity()[numberOfCity].UpgradeInfrastructure();
            PlusMoney(-costInfr(numberOfRegion, numberOfCity));
        }
    }
    // региона
    public int costInf(int numberOfRegion){
        return regionControl.get(numberOfRegion).CostOfInfrastructure();
    }
    public boolean posBuildInf(int numberOfRegion){
        return CheckMoney(costInf(numberOfRegion));
    }
    public void UpgradeInf(int numberOfRegion){
        if (posBuildInf(numberOfRegion)){
            regionControl.get(numberOfRegion).UpgradeInfrastructure();
            PlusMoney(-costInf(numberOfRegion));
        }
    }
    // проверяем можем ли построить/улучшить завод
    public int costUpgradePlant(int numberOfRegion, int numberOfCity, int numberPlant){
        return regionControl.get(numberOfRegion).getCity()[numberOfCity].CostOfPlant(numberPlant) * modBuildingCost / 100;
    }
    public boolean posBuildPlant(int numberOfRegion, int numberOfCity, int numberPlant /*номер клетки*/){
        return CheckMoney(costUpgradePlant(numberOfRegion, numberOfCity, numberPlant));
    }
    // строим завод
    public void BuildPlant(int numberOfRegion, int numberOfCity, int numberPlant){
        if (posBuildPlant(numberOfRegion, numberOfCity, numberPlant)){
            regionControl.get(numberOfRegion).getCity()[numberOfCity].getPlant().get(numberPlant).Upgrade();
            PlusMoney(-costUpgradePlant(numberOfRegion, numberOfCity, numberPlant));
        }
    }
    public void newPlant(int numberOfRegion, int numberOfCity, int numberPlant, int resource){
        if (posBuildPlant(numberOfRegion, numberOfCity, numberPlant)) {
            regionControl.get(numberOfRegion).getCity()[numberOfCity].newPlant(resource);
            PlusMoney(-costUpgradePlant(numberOfRegion, numberOfCity, numberPlant));
        }
    }

    //  ДА ЗДРАСТВУЕТ ВЕЛИКАЯ ФРАНЦУЗКАЯ АРМИЯ

    // Эта штука принимает позицию, но в целом можно переделать и под саму армию, убрав первую часть. В целом потребуется
    // для удаления армий после поражения. Хотя не особо. В общем есть и есть
    public void UpgradeArmy(Position position, int armyMen){
        Army arm = null;
        for (int i = 0; i < army    .size(); i++){
            if (army.get(i).getPosition().equals(position)){
                arm  = army.get(i);
            }
        }
        if (arm != null) {
            arm.Employ(armyMen);
            money -= World.baseCostCreationSquad[armyMen];
        }
    }
    public void CreateArmy(City cit){
        if (cit.CheckPosition() & CheckMoney(World.baseCostCreationSquad[0] * (100 + modArmyCreation))){
            Army newArmy = new Army(counryNum, modMorale, modOrganisation, cit.getPosArmy(), 3);
            newArmy.Employ(0);
            army.add(newArmy);
            World.mof.AddArmy(counryNum, cit.getPosArmy());
            money -= World.baseCostCreationSquad[0] * (100 + modArmyCreation);
        }
    }
    // cетаем генерала
    public void SetGeneral(int arm, int genera){
        if (army.get(arm).getGeneral() != null){
            RemoveGeneral(arm);
        }
        army.get(arm).setGeneral(general.get(genera));
        general.get(genera).setInArmy(arm);
    }
    public void RemoveGeneral(int arm){
        army.get(arm).getGeneral().setInArmy(-1);
        army.get(arm).setGeneral(null);
    }
    public void DeliteArmy(Army arm){
        army.remove(arm);
    }
    private void UpdateMobilisationArmy(){
        maxMobilisationArmy = 0;
        for (Region value : regionControl){
            for (City cit: value.getCity()){
                for (int ammount : cit.Mobilisation()){
                    maxMobilisationArmy += ammount;
                }
            }
        }
    }

    private void MobilisationCity(City cit){
        double partEqp = 1.0 * equipment/ maxEquipment;
        if (partEqp > 1){
            partEqp = 1;
        }
        Army arm = new Army(cit.Mobilisation(), counryNum, modMorale, modOrganisation,  cit.getPosArmy(), 2, partEqp);
        if (cit.CheckPosition() & !cit.isMobilisation() && CheckMoney(arm.getMaxEquipment() * World.baseCostMobilisation)) {
            mobilisateArmy.add(arm);
            cit.setMobilisation(true);
            PlusMoney(arm.getMaxEquipment() * World.baseCostMobilisation);
            maxEquipment -= arm.getMaxEquipment();
            equipment -= arm.getTotalEquipment();
            World.mof.AddArmy(counryNum, cit.getPosArmy());
        }
    }
    public void Mobilisation(){
        for (Region value : regionControl) {
            for (int j = 0; j < value.getCity().length; j++) {
                MobilisationCity(value.getCity()[j]);
            }
        }
    }
    public void Demobilisation(){
        while (mobilisateArmy.size()>0){
            maxEquipment += mobilisateArmy.get(0).getMaxEquipment();
            equipment += mobilisateArmy.get(0).getTotalEquipment();
            mobilisateArmy.remove(0);
        }
        for (Region value : regionControl) {
            for (int j = 0; j < value.getCity().length; j++) {
                value.getCity()[j].setMobilisation(false);
            }
        }
    }
    //обновляем мораль и организованность должно использоваться каждый ход
    private void UpdateArmy(){
        for (Army value : army) {
            value.UpdateMaxArmy(modMorale, modOrganisation);
            value.UpdateMorale(modIncreaseMorale);
            value.UpdateOrganisation(modIncreaseOrganisation);
            value.UpdateTactic(modTactic);
        }
        for (Army value : mobilisateArmy) {
            value.UpdateMaxArmy(modMorale, modOrganisation);
            value.UpdateMorale(modIncreaseMorale);
            value.UpdateOrganisation(modIncreaseOrganisation);
            value.UpdateTactic(modTactic);
        }
    }
    public int getModTactic() {
        return modTactic;
    }

    public void ActivateModificator(int i){
        modificator[i].Activate();
    }

    public int getModShock() {
        return modShock;
    }

    public int getModFire() {
        return modFire;
    }

    public ArrayList<Region> getRegionControl() {
        return regionControl;
    }

    public ArrayList<Region> getRegion() {
        return region;
    }

    public int getModMorale() {
        return modMorale;
    }

    public int getModOrganisation() {
        return modOrganisation;
    }

    public int getMaxEquipment() {
        return maxEquipment;
    }

    public int getEquipment() {
        return equipment;
    }

    public int getMaxMobilisationArmy() {
        return maxMobilisationArmy;
    }
}
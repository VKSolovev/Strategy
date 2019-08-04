package Functional.Maps;

// видимо тут показывается армия какой страны находится в этой позиции
public class MapOfArmies {
    private int[][] map;
    public MapOfArmies(int n, int m){
        map = new int[n][m];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < m; j++){
                map[i][j] = -1;
            }
        }
    }
    public void AddArmy(int countryTag, Position position){
        map[position.GetX()][position.GetY()] = countryTag;
    }
    public int CheckPosition(Position position){
        return map[position.GetX()][position.GetY()];
    }
    public void moveArmy(Position first, Position last){
        map[last.GetX()][first.GetY()] = map[first.GetX()][first.GetY()];
        map[first.GetX()][first.GetY()] = -1;
    }

}

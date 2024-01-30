package a01a.e2;

import java.util.ArrayList;
import java.util.List;

public class LogicImpl implements Logic {

    List<Pair<Integer,Integer>> cells;
    public LogicImpl(){
        cells = new ArrayList<>();
    }
    
    @Override
    public String getValue(int x, int y) {
        if(cellsContains(x, y)){
            return "*";
        }
        return " ";
    }

    @Override
    public void pressButton(int x, int y) {
        if(cellsContains(x, y)){
            cellsRemove(x, y);
        }else{
            cellsAdd(x,y);
        }
        if(checkDiag()){
            System.exit(1);
        }
        
    }

    public void pressButton(Pair<Integer,Integer> cord){
        pressButton(cord.getX(),cord.getY());
    }

    private boolean cellsContains(int x, int y){
        return cells.contains(new Pair<Integer,Integer>(x, y));
    }
    private boolean cellsRemove(int x, int y){
        return cells.remove(new Pair<Integer,Integer>(x,y));
    }
    private boolean cellsAdd(int x, int y){
        return cells.add(new Pair<Integer,Integer>(x, y));
    }
    private boolean checkDiag(){
        if(cells.size() >= 3){
            var e1 = cells.get(cells.size() -1);
            var e2 = cells.get(cells.size() -2);
            var e3 = cells.get(cells.size() -3);

            var t1 = subPair(e1, e2);
            var t2 = subPair(e2, e3);
            if(Pair.isDiag(t1)){
                if(t1.equals(t2)){
                    return true;
                }
            }
        }
        return false;
    }
    private Pair<Integer,Integer> subPair(Pair<Integer,Integer> e1, Pair<Integer,Integer> e2){
        return new Pair<Integer,Integer>(e1.getX() - e2.getX(), e1.getY() - e2.getY());
    }
    
    
}

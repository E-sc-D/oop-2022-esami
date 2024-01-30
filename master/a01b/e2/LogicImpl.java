package a01b.e2;

import java.util.List;
import java.util.ArrayList;

public class LogicImpl implements Logic {

    private final List<Pair<Integer,Integer>> cells;
    private final int size;

    public LogicImpl(int size){
        this.cells = new ArrayList<>();
        this.size = size;
    }

    @Override
    public boolean pressButton(Pair<Integer, Integer> coord) {
        var prev = cells.size();
        generateB(coord).stream().filter(this::isInBound).forEach(this::toggle);
        if(prev - cells.size() == 2){
            return false;
        }

        return true;

    }

    @Override
    public String getValue(Pair<Integer, Integer> coord) {
        if(cells.indexOf(coord) == -1){return " ";}
        return "*";
    }

    private Boolean isInBound(Pair<Integer, Integer> coord){
        if((coord.getX() < this.size && coord.getX() > -1) &&
            (coord.getY() < this.size && coord.getY() > -1)){return true;}
        
        return false;
    }
    private List<Pair<Integer, Integer>> generateB(Pair<Integer, Integer> coord){
        return new ArrayList<Pair<Integer, Integer>>(List.of(
            duplCoord(coord, 1, 1),
            duplCoord(coord, -1, 1),
            duplCoord(coord, 1, -1),
            duplCoord(coord, -1, -1)
        ));
    }

    private Pair<Integer, Integer> duplCoord(Pair<Integer, Integer> coord,int x, int y){
        return new Pair<Integer,Integer>(coord.getX() + x, coord.getY() + y);
    }

    private void toggle(Pair<Integer, Integer> coord){
        if(cells.contains(coord)){
            cells.remove(coord);
        }else{
            cells.add(coord);
        }
    }
     
}

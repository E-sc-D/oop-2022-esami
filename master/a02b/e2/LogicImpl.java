package a02b.e2;

import java.util.List;
import java.util.ArrayList;

public class LogicImpl implements Logic{

    private List<Pair<Integer,Integer>> cells = new ArrayList<>();

    @Override
    public void press(Pair<Integer, Integer> coord) {
       if(cells.contains(coord)){cells.remove(coord);} else {cells.add(coord);}
    }

    @Override
    public String getValue(Pair<Integer, Integer> coord) {
        return cells.contains(coord)? "*" : " ";
    }

    @Override
    public boolean check() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'check'");
    }
    
}

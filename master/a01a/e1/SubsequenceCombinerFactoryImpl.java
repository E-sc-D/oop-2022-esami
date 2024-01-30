package a01a.e1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;


public class SubsequenceCombinerFactoryImpl implements SubsequenceCombinerFactory {

    private <I,O> SubsequenceCombiner<I,O> generCombiner(Function<List<I>,List<O>> mapper, Predicate<List<I>> trigger){
        return new SubsequenceCombiner<I,O>() {

            @Override
            public List<O> combine(List<I> list) {
                final List<O> outlist = new ArrayList<>();
                List<I> buffer = new ArrayList<>();

                for (I li : list) {
                    buffer.add(li);
                    if(trigger.test(buffer)){
                        outlist.addAll(mapper.apply(buffer));
                        buffer = new ArrayList<>();
                    }
                }
                if(!buffer.isEmpty()){
                    outlist.addAll(mapper.apply(buffer));
                }

                return outlist;
            }     
        };
    }
    @Override
    public SubsequenceCombiner<Integer, Integer> tripletsToSum() {
        return generCombiner(list -> List.of(list.stream().reduce(0,Integer::sum)), list -> list.size() == 3);
    }

    @Override
    public <X> SubsequenceCombiner<X, List<X>> tripletsToList() {
        return generCombiner(list -> List.of(list), list -> list.size() == 3);
    }

    @Override
    public SubsequenceCombiner<Integer, Integer> countUntilZero() {
       return generCombiner(list -> List.of(
            Long.valueOf(list.stream().filter(x -> x != 0)
            .count()).intValue()),
        list -> list.contains(0));
    }

    @Override
    public <X, Y> SubsequenceCombiner<X, Y> singleReplacer(Function<X, Y> function) {
        return list -> list.stream().map(function::apply).toList();
    }

    @Override
    public SubsequenceCombiner<Integer, List<Integer>> cumulateToList(int threshold) {
       return generCombiner(list -> List.of(list), list -> list.stream().reduce(0,Integer::sum) >= threshold);
    }
    
}

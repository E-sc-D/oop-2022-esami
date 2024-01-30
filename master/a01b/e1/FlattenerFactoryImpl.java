package a01b.e1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Collectors;
public class FlattenerFactoryImpl implements FlattenerFactory {

    private <I,O> Flattener<I,O> genFlatter(
        BiFunction<List<I>,List<O>,List<O>> mapper,
        Predicate<List<O>> trigger){

        return new Flattener<I,O>() {

            @Override
            public List<O> flatten(List<List<I>> list) {
                final List<O>  outList = new ArrayList<>();
                List<O> buffer = new ArrayList<>();
                for (List<I> i : list) {
                    if(trigger.test(buffer)){
                        outList.addAll(mapper.apply(i,buffer));
                        buffer = new ArrayList<>();
                    }
                    else{
                        buffer.addAll(mapper.apply(i,buffer));
                    }
                }
                
                outList.addAll(buffer);
                return outList;
            }
            
        };
    }

    @Override
    public Flattener<Integer, Integer> sumEach() {
        //return list -> list.stream().map(i -> i.stream().reduce(0, Integer::sum)).toList();
        //return genFlatter((a,b) -> a.stream().reduce(0, Integer::sum), (x) -> true);
        return each((a) -> a.stream().reduce(0, Integer::sum));
    }

    @Override
    public <X> Flattener<X, X> flattenAll() {
        //return list -> list.stream().flatMap(List::stream).toList();
        return genFlatter((a,b) -> a, s -> true);
    }

    @Override
    public Flattener<String, String> concatPairs() {
       /*  return new Flattener<String,String>() {
            
            @Override
            public List<String> flatten(List<List<String>> list) {
                boolean process = false;
                List<String> buffer = new ArrayList<>();
                List<String> output = new ArrayList<>();
                for (List<String> list2 : list) {
                    buffer.addAll(list2);
                    if(process){
                        output.add(buffer.stream().collect(Collectors.joining()));
                        buffer.clear();
                    } 
                    process = !process;
                }
                if(!buffer.isEmpty()){
                    output.add(buffer.stream().collect(Collectors.joining()));
                }
                
                return output;
            }
        }; */

        /*  return lista -> IntStream.range(0,(lista.size()/2) + lista.size() % 2) //generate a stream containing the exact number of output elements ( 0 - n-1 )
         .mapToObj((x) -> lista.stream().skip(x*2).limit(2)// each number is transformed into a stream of 2 elements
         .map((y) -> y.stream().collect(Collectors.joining()))//the two lists of Strings are packed into two strings
         .collect(Collectors.joining()))// and merged together into a single string
         .toList(); */

         return genFlatter((a,b) -> List.of(
            Stream.concat(b.stream(), a.stream())
            .collect(Collectors.joining())), 
        (x) -> x.size() == 1);
         
        
    }

    @Override
    public <I, O> Flattener<I, O> each(Function<List<I>, O> mapper) {
        //return list -> list.stream().map(mapper).toList();
        return genFlatter((a,b) -> List.of(mapper.apply(a)), (s) -> true);
    }

    @Override
    public Flattener<Integer, Integer> sumVectors() {
        /* return list -> IntStream.range(0, list.get(0).size())
            .mapToObj(i -> list.stream().map(h -> h.get(i)).reduce(0,Integer::sum))
            .toList();
        */
        return genFlatter(this::sumTwoVectors, (x) -> true);      
    }

    private List<Integer> sumTwoVectors(List<Integer> list1, List<Integer> list2){
        return list1.isEmpty() ? list2 : IntStream.range(
            0, list1.size())
            .mapToObj(i -> list1.get(i) + list2.get(i))
            .collect(Collectors.toList());

    }
 
}

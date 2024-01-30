package a02a.e1;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RecursiveIteratorHelpersImpl implements RecursiveIteratorHelpers {


    private <X> RecursiveIterator<X> constr(Supplier<RecursiveIterator<X>> sup,X elm){
        return new RecursiveIterator<X>() {

            @Override
            public X getElement() {
                return elm;
            }

            @Override
            public RecursiveIterator<X> next() {
                return sup.get();
            }
            
        };
    }
    private <X> RecursiveIterator<X> fromIterator(Iterator<X> iterator){
        if(iterator.hasNext() == false){
            return null;
        }
        return constr(() -> fromIterator(iterator), iterator.next());
    }
    
    @Override
    public <X> RecursiveIterator<X> fromList(List<X> list) {
        return fromIterator(list.iterator());
        /* using a stream in this case is triky since is necessary a version of 
        recursive iterator that is manageble, then, by saving the previus generated instance
        is possible to link them all togheter, but, that is not much of a stream anymore,
        it would be easier to just use a foreach
        */
        
    }

    @Override
    public <X> List<X> toList(RecursiveIterator<X> input, int max) {
       return Stream.iterate(input, Objects::nonNull, RecursiveIterator::next).limit(max).map(RecursiveIterator::getElement).toList();
       //note: streams are handy to navigate inside recursions without messy code
    }

    @Override
    public <X, Y> RecursiveIterator<Pair<X, Y>> zip(RecursiveIterator<X> first, RecursiveIterator<Y> second) {
       /* return fromIterator(Stream.iterate(
        new Pair<RecursiveIterator<X>,RecursiveIterator<Y>>(first,second),
        x -> x.getX() != null && x.getY() != null, (x) -> new Pair<>(x.getX().next(), x.getY().next()))
        .map((x) -> new Pair<>(x.getX().getElement(), x.getY().getElement())).iterator()); */

        if(Objects.isNull(first) || Objects.isNull(second)){return null;}
        return constr(() -> zip(first.next(),second.next()),new Pair<X,Y>(first.getElement(),second.getElement()));
    }

    @Override
    public <X> RecursiveIterator<Pair<X, Integer>> zipWithIndex(RecursiveIterator<X> iterator) {
        return zip(iterator,fromIterator(Stream.iterate(0, i -> i + 1).iterator()));
    }

    @Override
    public <X> RecursiveIterator<X> alternate(RecursiveIterator<X> first, RecursiveIterator<X> second) {
        /* if(Objects.isNull(first) && Objects.isNull(second)){return null;}
        if(Objects.isNull(first)){return constr(() -> alternate(first, second.next()), second.getElement());}
        if(Objects.isNull(second)){return constr(() -> alternate(first.next(), second), first.getElement());}
        */

        if(Objects.nonNull(first)){
            return constr(() -> alternate(second, first.next()) , first.getElement());
        }
        return second;

    }
    
}

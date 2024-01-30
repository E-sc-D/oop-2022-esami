package a03a.e1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ParserFactoryImpl implements ParserFactory {

    @Override
    public <X> Parser<X> fromFinitePossibilities(Set<List<X>> acceptedSequences) {
        return recursive( x -> 
                    Optional.of(fromFinitePossibilities(
                        acceptedSequences
                        .stream()
                        .filter(l -> !l.isEmpty())
                        .filter(l -> l.get(0).equals(x))
                        .map(l -> new ArrayList<>(l.subList(1, l.size())))
                        .collect(Collectors.toSet()))), 
            acceptedSequences.contains(Collections.emptyList()));
    }

    @Override
    public <X> Parser<X> fromGraph(X x0, Set<Pair<X, X>> transitions, Set<X> acceptanceInputs) {
        return new Parser<X>() {

            @Override
            public boolean accept(Iterator<X> iterator) {
                var x1 = x0;
                var x2 = x0;
                while(iterator.hasNext()){
                    x2 = x1;
                    x1 = iterator.next();
                    if(!transitions.contains(new Pair<X,X>(x2,x1))){
                        return false;
                    }
                }
                if(acceptanceInputs.contains(x1)){
                    return true;
                }
                return false;
            }
            
        };
    }

    @Override
    public <X> Parser<X> fromIteration(X x0, Function<X, Optional<X>> next) {
       return fromParserWithInitial(x0,fromIteration(next.apply(x0).get(), next));
    }

    @Override
    public <X> Parser<X> recursive(Function<X, Optional<Parser<X>>> nextParser, boolean isFinal) {
        return new Parser<X>() {

            @Override
            public boolean accept(Iterator<X> iterator) {
                if(!iterator.hasNext()){
                    return isFinal;
                }
                return nextParser.apply(iterator.next()).map(x -> x.accept(iterator)).orElse(isFinal);
            }
            
        };
    }

    @Override
    public <X> Parser<X> fromParserWithInitial(X x, Parser<X> parser) {
        return new Parser<X>() {

            @Override
            public boolean accept(Iterator<X> iterator) {
                if(iterator == null && x == null){return true;}
                if(!iterator.next().equals(x)){return false;}
                return parser.accept(iterator);
            }
            
        };
    }
    
}

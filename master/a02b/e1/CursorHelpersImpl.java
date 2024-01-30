package a02b.e1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CursorHelpersImpl implements CursorHelpers{

    @Override
    public <X> Cursor<X> fromNonEmptyList(List<X> list) {
        return fromIterator(list.iterator());
    }

    private <X> Cursor<X> fromIterator(Iterator<X> ite) {
        return new Cursor<X>() {
            Iterator<X> it = ite;
            X elem = it.next();

            @Override
            public X getElement() {
               return elem;
            }

            @Override
            public boolean advance() {
                if(it.hasNext()){
                    elem = it.next();
                    return true;
                } 
                return false;
            }
            
        };
    }

    @Override
    public Cursor<Integer> naturals() {
       return fromIterator(Stream.iterate(0,n -> n + 1).iterator());
    }

    @Override
    public <X> Cursor<X> take(Cursor<X> input, int max) {
       return new Cursor<X>() {
        int limit = max;
        int i = 0;
        @Override
        public X getElement() {
            return input.getElement();
        }

        @Override
        public boolean advance() {
            i++;
            if(i < limit){
                return input.advance();
            }
            return false;
        }
        
       };
    }

    @Override
    public <X> void forEach(Cursor<X> input, Consumer<X> consumer) {   
        do {
            consumer.accept(input.getElement());
        } while (input.advance());
    }

    @Override
    public <X> List<X> toList(Cursor<X> input, int max) {
        final List<X> outlist = new ArrayList<>();
        outlist.add(input.getElement());
        for(int i = 1; i < max && input.advance(); i++){
            outlist.add(input.getElement());
        }

        return outlist;
    }
    
}

package a03b.e1;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class LazyTreeFactoryImpl implements LazyTreeFactory{

    @Override
    public <X> LazyTree<X> constantInfinite(X value) {
        return cons(Optional.of(value), ()-> constantInfinite(value), () -> constantInfinite(value));
    }

    @Override
    public <X> LazyTree<X> fromMap(X root, Map<X, Pair<X, X>> map) {
        var mappa = map.get(root);
        if(mappa == null){
            return cons(Optional.of(root), 
            () -> empty(), 
            () -> empty());
        }
        return cons(Optional.of(root), 
        () -> fromMap(mappa.getX(), map), 
        () -> fromMap(mappa.getY(), map));
    }

    @Override
    public <X> LazyTree<X> cons(Optional<X> root, Supplier<LazyTree<X>> leftSupp, Supplier<LazyTree<X>> rightSupp) {
        return new LazyTree<X>() {

            @Override
            public boolean hasRoot() {
                return root.isPresent();
            }

            @Override
            public X root() {
                return root.get();
            }

            @Override
            public LazyTree<X> left() {
                if(hasRoot()){
                    return leftSupp.get();
                } else{
                    return empty();
                }
            }

            @Override
            public LazyTree<X> right() {
                if(hasRoot()){
                    return rightSupp.get();
                } else{
                    return empty();
                }
            }
            
        };
    }
    private <X> LazyTree<X> empty(){
        return new LazyTree<X>() {

            @Override
            public boolean hasRoot() {
                return false;
            }

            @Override
            public X root() {
                throw new NoSuchElementException();
            }

            @Override
            public LazyTree<X> left() {
                return empty();
            }

            @Override
            public LazyTree<X> right() {
                return empty();
            }
            
        };
    }

    @Override
    public <X> LazyTree<X> fromTwoIterations(X root, UnaryOperator<X> leftOp, UnaryOperator<X> rightOp) {
        return cons(Optional.of(root),
         () -> fromTwoIterations(leftOp.apply(root), leftOp, rightOp), 
         () -> fromTwoIterations(rightOp.apply(root), leftOp, rightOp));
    }

    @Override
    public <X> LazyTree<X> fromTreeWithBound(LazyTree<X> tree, int bound) {
       if(bound == 0){return empty();}

       return cons(Optional.of(tree.root()), 
       () -> fromTreeWithBound(tree.left(), bound - 1),
       () -> fromTreeWithBound(tree.right(), bound - 1));
    }
    
}

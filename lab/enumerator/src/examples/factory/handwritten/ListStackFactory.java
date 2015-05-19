package examples.factory.handwritten;

import java.math.BigInteger;
import java.util.Random;

import examples.factory.ListStack;
import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import tom.library.enumerator.P1;
import tom.library.factory.Enumerate;

public class ListStackFactory {

    private static Enumeration<ListStack> enumListStack = null;

    public static Enumeration<ListStack> getEnumeration() {

        // final result
//        Enumeration<ListStack> enumRes = null;
        int maxSize = 8;
        boolean canBeNull = false; 
        int singletonSize = -1;
        boolean singleton = false;

        // constructor with no arguments
        Enumeration<ListStack> enumCons1 = Enumeration.singleton(new ListStack());

        // constructor with arguments 
        // F< arg1, F< arg2, ... F <argn, Student>...>
        F<Integer, ListStack> _listStack_cons2 = new F<Integer, ListStack>() {
            public ListStack apply(final Integer t2) {
                return  new ListStack(t2);
            }
        };
        // //@Enumerate(maxSize=5)
        // int nbElem
        final Enumeration<Integer> nbElemEnum = new Enumeration<Integer>(
                Combinators.makeInteger().parts().take(BigInteger.valueOf(5)));
        Enumeration<ListStack> enumCons2 = Enumeration.apply(Enumeration.singleton(_listStack_cons2), nbElemEnum);

        // enumeration for all constructors (as many PLUS as constructors-1)
        Enumeration<ListStack>  enumCons = enumCons1.plus(enumCons2);


        
        // the "this" used in the call to enumerating methods with numberOfSamples>1 (push, extend, etc.)
        Enumeration<ListStack> tmpListStackEnum = new Enumeration<ListStack>((LazyList<Finite<ListStack>>) null);
        
/**********************************************************************************************************************************
********************************************    push()    *************************************************************************
***********************************************************************************************************************************/
        // begin PUSH
        // enumerating method 
        // @Enumerate
        // F< arg1, F< arg2, ... F <argn, Student>...>
//        F<Enumeration<ListStack>, F<Enumeration<Integer>, Enumeration<ListStack>>> _listStack_push = new F<Enumeration<ListStack>, F<Enumeration<Integer>, Enumeration<ListStack>>>() {
//            public F<Enumeration<Integer>, Enumeration<ListStack>> apply(final Enumeration<ListStack> t1) {
//                return new F<Enumeration<Integer>, Enumeration<ListStack>>() {
//                    public Enumeration<ListStack> apply(final Enumeration<Integer> t2) {
//                        return 
//                        Enumeration.apply(
//                            Enumeration.apply(
//                                Enumeration.singleton(
//                                    (F<ListStack, F<Integer, ListStack>>) new F<ListStack, F<Integer, ListStack>>() {
//                                        public F<Integer, ListStack> apply(final ListStack t1) {
//                                            return new F<Integer, ListStack>() {
//                                                public ListStack apply(final Integer t2) {
//                                                    // should build a completely new object which does not interfere with the others
//                                                    return (ListStack) t1.push(t2);
//                                                }
//                                            };
//                                        }
//                                    }
//                                ),
//                                t1
//                            ),
//                            t2
//                        ).pay();
//                    }
//                };
//            }
//        };
        
        F<ListStack, F<Integer, ListStack>> _listStack_push = new F<ListStack, F<Integer, ListStack>>() {
            public F<Integer, ListStack> apply(final ListStack t1) {
                return new F<Integer, ListStack>() {
                    public ListStack apply(final Integer t2) {
                        return (ListStack) t1.push(t2);
                    }
                };
            }
        };
        
        // @Enumerate(...
        // Integer elem
        
        // @Enumerate(maxSize = 4, ...
        maxSize = 4; // try with 2 and singleton 8 to see how the try-catch works
        Enumeration<Integer> elemEnum = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(maxSize)));
        
        // @Enumerate(... singleton = true, singletonSize = 3 ...
        singleton = true;
        singletonSize = 3; 
        if(singleton){
            Integer _elemSingleton = elemEnum.get(BigInteger.valueOf(singletonSize));
            while(_elemSingleton==null){ // no element at randomly generated index 
                _elemSingleton = elemEnum.get(BigInteger.valueOf(singletonSize++));
            }
            // should we handle the case of an empty enumeration?
            elemEnum = Enumeration.singleton(_elemSingleton);
        }
        // @Enumerate(...  canBeNull = true
        canBeNull = false; //true;
        if(canBeNull){
            Enumeration<Integer> emptyIntegerEnum = Enumeration.singleton(null);
            elemEnum = elemEnum.plus(emptyIntegerEnum);
        }
        
        // METHOD level
        // @Enumerate(...
        maxSize = -1;   // can't be used for enumerating this (or we can use it on enumCons but doesn't make too much sense)
                        //new Enumeration<ListStack>(enumCons.parts().take(BigInteger.valueOf(maxSize)));
        
        // @Enumerate(... singletonSize = 0, ...
        Enumeration<ListStack> pushThisEnum = tmpListStackEnum;
        singleton = false;
        singletonSize = 0;
        if(singleton){
            ListStack _pushThisSingleton = null;
            try {
                _pushThisSingleton = enumCons.get(BigInteger.valueOf(singletonSize));
            } catch (RuntimeException e1) {}
            while(_pushThisSingleton==null){ // no element at specified size 
                try {
                    _pushThisSingleton = enumCons.get(BigInteger.valueOf(singletonSize++));
                } catch (RuntimeException e1) {}
            }
            pushThisEnum = Enumeration.singleton(_pushThisSingleton);
        }
        
        // @Enumerate(... 
        canBeNull = false; // only possible choice for @Enumerate on METHOD
        
        // as many apply as parameters
//        Enumeration<ListStack>  enumPushListStack = _listStack_push.apply(tmpListStackEnum).apply(elemEnum);
        Enumeration<ListStack>  enumPushListStack = 
            Enumeration.apply(
                Enumeration.apply(
                    Enumeration.singleton(
                        _listStack_push
                    ),
                    pushThisEnum
                ),
                elemEnum
            ).pay();
        
        // end PUSH
/**********************************************************************************************************************************
********************************************    Empty()    ************************************************************************
***********************************************************************************************************************************/
        // begin EMPTY
        // enumerating method 
        // @Enumerate(...) 
//        F<Enumeration<ListStack>, Enumeration<ListStack>> _listStack_empty = new F<Enumeration<ListStack>, Enumeration<ListStack>>() {
//            public Enumeration<ListStack> apply(final Enumeration<ListStack> e) {
//                return Enumeration.apply(
//                    Enumeration.singleton(
//                        (F<ListStack, ListStack>) new F<ListStack, ListStack>() {
//                            public ListStack apply(final ListStack e) {
//                                return (ListStack) e.empty();
//                            }
//                        }
//                    ),
//                    e
//                ).pay();
//            }
//        };
        
        F<ListStack, ListStack> _listStack_empty = new F<ListStack, ListStack>() {
            public ListStack apply(final ListStack e) {
                return (ListStack) e.empty();
            }
        };
        
        // METHOD level
        // @Enumerate(...
        maxSize = -1;   // can't be used for enumerating this (or we can use it on enumCons but doesn't make too much sense)
                        //new Enumeration<ListStack>(enumCons.parts().take(BigInteger.valueOf(maxSize)));
        
        // @Enumerate(... singleton = true, ...
        Enumeration<ListStack> emptyThisEnum = tmpListStackEnum;
        singleton = true;
        singletonSize = 0;  // by default
        if(singleton){
            ListStack _emptyThisSingleton = null;
            try{
                _emptyThisSingleton = enumCons.get(BigInteger.valueOf(singletonSize));
            }catch(RuntimeException exc){}  
            while(_emptyThisSingleton==null){ // no element at randomly generated index 
                try{ // try exhaustively from singleton downwards
                    _emptyThisSingleton = enumCons.get(BigInteger.valueOf(singletonSize++));
                }catch(RuntimeException exc){}  
            }
            emptyThisEnum = Enumeration.singleton(_emptyThisSingleton);
        }
        // @Enumerate(... 
        canBeNull = false; // only possible choice fro @Enumerate on METHOD
        
//        Enumeration<ListStack> enumEmptyListStack = _listStack_empty.apply(emptyThisEnum);
        Enumeration<ListStack> enumEmptyListStack = Enumeration.apply(Enumeration.singleton(_listStack_empty), emptyThisEnum).pay();
        // end EMPTY
/**********************************************************************************************************************************
********************************************    Extend()    ************************************************************************
***********************************************************************************************************************************/

        // begin EXTEND
        // enumerating method 
        // other method without parameters but not a base case (normally no difference to the others)
//        F<Enumeration<ListStack>, Enumeration<ListStack>> _listStack_extend = new F<Enumeration<ListStack>, Enumeration<ListStack>>() {
//            public Enumeration<ListStack> apply(final Enumeration<ListStack> e) {
//                return Enumeration
//                        .apply(Enumeration.singleton((F<ListStack, ListStack>) new F<ListStack, ListStack>() {
//                            public ListStack apply(final ListStack e) {
//                                return (ListStack) e.clone().extend();
//                            }
//                        }), e).pay();
//            }
//        };

        F<ListStack, ListStack> _listStack_extend = new F<ListStack, ListStack>() {
            public ListStack apply(final ListStack e) {
                return (ListStack) e.extend();
            }
        };
        
        // METHOD level
        // @Enumerate(...
        maxSize = -1;   // can't be used for enumerating this (or we can use it on enumCons but doesn't make too much sense)
                        //new Enumeration<ListStack>(enumCons.parts().take(BigInteger.valueOf(maxSize)));
        
        // @Enumerate(... singleton = 0, ...
        Enumeration<ListStack> extendThisEnum = tmpListStackEnum;
        singleton = false;  // by default
        singletonSize = 0; // by default 
        if(singleton){
            ListStack _extendThisSingleton=null;
            try{
                _extendThisSingleton = enumCons.get(BigInteger.valueOf(singletonSize));
            }catch(RuntimeException exc){}  
            while(_extendThisSingleton==null){ // no element at randomly generated index 
                try{ // try exhaustively from singleton downwards
                    _extendThisSingleton = enumCons.get(BigInteger.valueOf(singletonSize++));
                }catch(RuntimeException exc){}  
            }
            extendThisEnum = Enumeration.singleton(_extendThisSingleton);
        }
        // @Enumerate(... 
        canBeNull = false; // only possible choice for @Enumerate on METHOD
        
        // as many apply as parameters
//        Enumeration<ListStack>  enumExtendListStack = _listStack_extend.apply(extendThisEnum);
        Enumeration<ListStack> enumExtendListStack = Enumeration.apply(Enumeration.singleton(_listStack_extend), extendThisEnum).pay();
        // end EXTEND
        
        
//        enumListStack = enumPushListStack;
//        enumListStack = enumListStack.plus(enumEmptyListStack);
//        enumListStack = enumListStack.plus(enumExtendListStack);
        
        enumListStack = enumCons.plus(enumPushListStack).plus(enumEmptyListStack).plus(enumExtendListStack);

        tmpListStackEnum.p1 = new P1<LazyList<Finite<ListStack>>>() {
            public LazyList<Finite<ListStack>> _1() {
                return enumListStack.parts();
            }
        };

//        enumRes = enumListStack;
        
        return enumListStack;
    }
    
/*
        private static BigInteger nextRandomBigInteger(BigInteger n) {
        Random rand = new Random();
        BigInteger result = new BigInteger(n.bitLength(), rand);
        while (result.compareTo(n) > 0) {
            result = new BigInteger(n.bitLength(), rand);
        }
        return result;
    }
*/
}

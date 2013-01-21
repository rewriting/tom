import lib.*;
import lib.sl.*;
import tom.library.sl.*;

import miniml.miniml.types.*;

public class MiniML {
    %include { string.tom }

    public static void showresult(Object x) { System.out.println("<result>" + x.toString() + "</result>"); }

    %gom {
        module MiniML

        imports String int boolean

        abstract syntax

        EnvAssoc = EnvAssoc( name:String , val:Val )
        MemAssoc = MemAssoc( addr:int    , val:Val )

        Env      = Env( EnvAssoc* )
        Mem      = Mem( MemAssoc* )

        Val      = Int( i:int )
                 | Bool( b:boolean )

                 | Closure( code:Code , env:Env )

                 | Ref( addr:int )

                 | Obj( members:Env )

        BinOp  = Add()
               | Sub()
               | Mult()
               | Div()

               | And()
               | Or()

               | Eq()

               | LT()
               | GT()
               | LE()
               | GE()

        UniOp  = Minus()
               | Neg()


        Code  = Unit()

              | Val( val:Val )
              | UniOp( uniop:UniOp , code:Code )
              | BinOp( fst:Code , binop:BinOp, snd:Code)

              | Var( name:String )
              | Assign( var:Code, body:Code )

              | Member(obj: Code , member:String )

              | If( cond:Code , then:Code, else:Code )
              | While( cond:Code , body:Code )
              | Seq ( fst:Code, snd:Code )

              | Let( name:String, def:Code, body:Code )
              | Fun( name:String, body:Code )
              | App( fun:Code   , arg:Code )

              | Read()
              | Write( body:Code )


        module MiniML:rules() {
            Env(x*, EnvAssoc(n,_), y*, EnvAssoc(n,v), z*) -> Env(x*, y*, EnvAssoc(n,v), z*)
            Mem(x*, MemAssoc(n,_), y*, MemAssoc(n,v), z*) -> Mem(x*, y*, MemAssoc(n,v), z*)

            Seq(Unit,x)                                   -> x
        }
    }

    %include{sl.tom}


    public static Visitor<Visitable,Visitable> binOp = Visitor.map( new Fun<Visitable,Visitable>() {
        public Visitable apply(Visitable u) throws VisitFailure {

            if (!(u instanceof Code)) throw new VisitFailure();

            /*
             Now that we are on Code, we can match.
             */
            %match(u) {
                BinOp(Val(Int(i))  , Add()  , Val(Int(j)))  -> { return `Val(Int(i + j));                               }
                BinOp(Val(Int(i))  , Sub()  , Val(Int(j)))  -> { return `Val(Int(i - j));                               }
                BinOp(Val(Int(i))  , Mult() , Val(Int(j)))  -> { return `Val(Int(i * j));                               }
                BinOp(Val(Int(i))  , Div()  , Val(Int(j)))  -> { return `Val(Int(i / j));                               }

                BinOp(Val(Int(i))  , LT()   , Val(Int(j)))  -> { return `Val(Bool(i <  j));                             }
                BinOp(Val(Int(i))  , GT()   , Val(Int(j)))  -> { return `Val(Bool(i >  j));                             }
                BinOp(Val(Int(i))  , LE()   , Val(Int(j)))  -> { return `Val(Bool(i <= j));                             }
                BinOp(Val(Int(i))  , GE()   , Val(Int(j)))  -> { return `Val(Bool(i >= j));                             }

                BinOp(Val(Bool(i)) , And()  , Val(Bool(j))) -> { return `Val(Bool(i & j));                              }
                BinOp(Val(Bool(i)) , Or()   , Val(Bool(j))) -> { return `Val(Bool(i | j));                              }

                BinOp(x            , Eq()   , y           ) -> { return `Val(Bool(true));                               }
                BinOp(_            , Eq()   , _           ) -> { return `Val(Bool(false));                              }
            };
            throw new VisitFailure();
        }});

    public static Visitor<Visitable,Visitable> uniOp = Visitor.map( new Fun<Visitable,Visitable>() {
        public Visitable apply(Visitable u) throws VisitFailure {

            if (!(u instanceof Code)) throw new VisitFailure();

            /*
             Now that we are on Code, we can match.
             */
            %match(u) {
                UniOp(Minus() , Val(Int( i)))  -> { return `Val(Int( -i));                               }
                UniOp(Neg()   , Val(Bool(b)))  -> { return `Val(Bool(!b));                               }
            };
            throw new VisitFailure();
        }});


    public static void run() throws VisitFailure {
        System.out.println("<MiniML>\n\n");
        System.out.println("</MiniML>\n\n");
    }

}

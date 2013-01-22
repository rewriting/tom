// START OF M4 MACROS
changequote(`[[', `]]')

define([[VISITORMAP]],
[[public static Visitor<$2,$3> $1 = Visitor.map( new Fun<$2,$3>() {
  public $3 apply($2 $4) throws VisitFailure {
    $5
 }});]])

// END OF M4 MACROS


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

        Val      = Unit()

                 | Int( i:int )
                 | Bool( b:boolean )

                 | Closure( code:Code , env:Env )

                 | Ptr( addr:int )

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


        Code  = Val( val:Val )
              | UniOp( uniop:UniOp , code:Code )
              | BinOp( fst:Code , binop:BinOp, snd:Code)

              | Var( name:String )

              | If( cond:Code , then:Code, else:Code )
              | While( cond:Code , body:Code )
              | Seq ( fst:Code, snd:Code )

              | Ref( code:Code )
              | Bang( code:Code )
              | Assign( var:Code, body:Code )

              | Member(obj: Code , member:String )

              | Let( name:String, def:Code, body:Code )
              | Fun( name:String, body:Code )
              | App( fun:Code   , arg:Code )

              | Read()
              | Write( body:Code )

        Inputs   = Inputs(Val*)
        Outputs  = Outputs(Val*)

        module MiniML:rules() {
            Env(x*, EnvAssoc(n,_), y*, EnvAssoc(n,v), z*) -> Env(x*, y*, EnvAssoc(n,v), z*)
            Mem(x*, MemAssoc(n,_), y*, MemAssoc(n,v), z*) -> Mem(x*, y*, MemAssoc(n,v), z*)
        }
    }

    %include{sl.tom}

    VISITORMAP(code_reduction, Visitable , Visitable , u , [[

            if (!(u instanceof Code)) throw new VisitFailure();

            /*
             Now that we are on Code, we can match.
             */
            %match(u) {
                UniOp(Minus() , Val(Int( i)))                    -> { return `Val(Int( -i));                                 }
                UniOp(Neg()   , Val(Bool(b)))                    -> { return `Val(Bool(!b));                                 }


                BinOp(Val(Int(i))  , Add()  , Val(Int(j)))       -> { return `Val(Int(i + j));                               }
                BinOp(Val(Int(i))  , Sub()  , Val(Int(j)))       -> { return `Val(Int(i - j));                               }
                BinOp(Val(Int(i))  , Mult() , Val(Int(j)))       -> { return `Val(Int(i * j));                               }
                BinOp(Val(Int(i))  , Div()  , Val(Int(j)))       -> { return `Val(Int(i / j));                               }

                BinOp(Val(Int(i))  , LT()   , Val(Int(j)))       -> { return `Val(Bool(i <  j));                             }
                BinOp(Val(Int(i))  , GT()   , Val(Int(j)))       -> { return `Val(Bool(i >  j));                             }
                BinOp(Val(Int(i))  , LE()   , Val(Int(j)))       -> { return `Val(Bool(i <= j));                             }
                BinOp(Val(Int(i))  , GE()   , Val(Int(j)))       -> { return `Val(Bool(i >= j));                             }

                BinOp(Val(Bool(i)) , And()  , Val(Bool(j)))      -> { return `Val(Bool(i & j));                              }
                BinOp(Val(Bool(i)) , Or()   , Val(Bool(j)))      -> { return `Val(Bool(i | j));                              }

                BinOp(x            , Eq()   , y           )      -> { return `Val(Bool(true));                               }
                BinOp(_            , Eq()   , _           )      -> { return `Val(Bool(false));                              }


                If(Val(true)  , t , _ )                          -> { return `t;                                              }
                If(Val(false) , _ , e )                          -> { return `e;                                              }

                Seq(Val(_), c)                                   -> { return `c;                                              }

                Member(Val(Obj(Env(_*, EnvAssoc(n, v), _*))), n) -> { return `v;                                              }
            };
            throw new VisitFailure();
    ]])

    VISITORMAP(var, [[ P<Visitable,Visitable> ]] , [[ P<Visitable,Visitable> ]] , p , [[

            Visitable code  = p.left;
            Visitable assoc = p.right;

            if (!(code  instanceof Code    )) throw new VisitFailure();
            if (!(assoc instanceof EnvAssoc)) throw new VisitFailure();

            %match {
                Var(x) << code && EnvAssoc(y, v) << assoc && x == y -> { return P.mkP((Visitable)`Val(v), assoc);                               }
            };
            throw new VisitFailure();
    ]])


    VISITORMAP(ref, [[ P<Visitable,Visitable> ]], [[ P<Visitable,Visitable> ]] , p , [[

            Visitable code  = p.left;
            Visitable assoc = p.right;

            if (!(code  instanceof Code    )) throw new VisitFailure();
            if (!(assoc instanceof MemAssoc)) throw new VisitFailure();

            /*
             Now that we are on Code * EnvAssoc, we can match.
             */
            %match {
                Bang(  Val(Ptr(i))        ) << code && MemAssoc(i, v) << assoc -> { return P.mkP((Visitable)`Val(v)     , assoc                     );  }
                Assign(Val(Ptr(i)), Val(v)) << code && MemAssoc(i, _) << assoc -> { return P.mkP((Visitable)`Val(Unit()), (Visitable)`MemAssoc(i, v));  }
            };
            throw new VisitFailure();
    ]])

    VISITORMAP(input, [[ P<Visitable,Visitable> ]] , [[ P<Visitable,Visitable> ]] , p , [[

            Visitable code  = p.left;
            Visitable inputs = p.right;

            if (!(code   instanceof Code    )) throw new VisitFailure();
            if (!(inputs instanceof Inputs  )) throw new VisitFailure();

            /*
             Now that we are on Code * EnvAssoc, we can match.
             */
            %match {
                Read() << code && Inputs(v,x*) << inputs -> { return P.mkP((Visitable)`x , (Visitable)`Inputs(x*) );  }
            };
            throw new VisitFailure();
    ]])

    VISITORMAP(output , [[ P<Visitable,Visitable> ]] , [[ P<Visitable,Visitable> ]] , p , [[

            Visitable code    = p.left;
            Visitable outputs = p.right;

            if (!(code    instanceof Code     )) throw new VisitFailure();
            if (!(outputs instanceof Outputs  )) throw new VisitFailure();

            /*
             Now that we are on Code * EnvAssoc, we can match.
             */
            %match {
                Write(Val(v)) << code && Outputs(x*) << outputs -> { return P.mkP((Visitable)`Val(Unit()) , (Visitable)`Outputs(x*,v) );  }
            };
            throw new VisitFailure();
    ]])

    public static void run() throws VisitFailure {
        System.out.println("<MiniML>\n\n");
        System.out.println("</MiniML>\n\n");
    }

}

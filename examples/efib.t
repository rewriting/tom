class EFIB 

creation main

feature
   -- symbol_type: STRING;
   symbol_type: ANY;

   zero_symbol: STRING is "zero";
   suc_symbol:  STRING is "suc";
   plus_symbol: STRING is "plus";
   fib_symbol:  STRING is "fib";

   zero: TERM is
      once
	 !!Result.make_0(zero_symbol);
      end;

   make_suc(t1: TERM): TERM is
      do
	 !!Result.make_1(suc_symbol,t1);
      end;

   make_plus(t1,t2: TERM): TERM is
      do
	 !!Result.make_2(plus_symbol,t1,t2);
      end;

   make_fib(t1: TERM): TERM is
      do
	 !!Result.make_1(fib_symbol,t1);
      end;

   main is
      do
	 run(1,10);
      end;

   run(loopit: INTEGER; n: INTEGER) is
      local
	 res, t: TERM; i: INTEGER;
	 view: STRING;
      do
	 from 
	    t := zero;
	 until
	    i >= n
	 loop
	    t := make_suc(t);
	    i := i + 1;
	 end;
	 res := fib(t);
	 view := res.to_string;
	 io.put_string(view);
	 io.put_string("%N");
      end;  

  %typeterm term {
    implement { TERM }
    get_fun_sym(t)      { t.symbol }
    cmp_fun_sym(s1,s2)  { s1.is_equal(s2) }
    get_subterm(t, n)   { t.item(n) }
    equals(t1, t2)      { t1.is_equal(t2) }
  }

  %op term zero {
    fsym { zero_symbol }
    make { zero }
  }
  
  %op term suc(n:term) {
    fsym { suc_symbol }
    make(t) { make_suc(t) }
    get_slot(n,t) { t.item(0) }
  }
  
  %op term plus(term,term) {
    fsym { plus_symbol }
    make(t1,t2) { make_plus(t1,t2) } 
  }
  
  %op term fib(term) {
    fsym { fib_symbol }
    make(t1) { make_fib(t1) }
  }
   
--  %rule {
--    plus(x,zero)     -> x
--    plus(x,suc(y))   -> suc(plus(x,y))
--  }

--  %rule {
--    fib(zero)        -> suc(zero)
--    fib(suc(zero))   -> suc(zero)
--    fib(suc(suc(x))) -> plus(fib(x),fib(suc(x)))
--  }


   plus(t1, t2: TERM): TERM is
      local
	 %variable
      do
         %match(term t1, term t2) {
	   x,zero   -> { Result := x; }
	   x,suc(y) -> { Result := make_suc(plus(x,y)); }
         }
      end;

   fib(t: TERM): TERM is
      local
   %variable
      do
	 %match(term t) {
	   zero          -> { Result := make_suc(zero); }
	   suc[n=zero]   -> { Result := make_suc(zero); }
	   suc(suc(x))   -> { Result := plus(fib(x),fib(make_suc(x))); }
         }
      end;

end

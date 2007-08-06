let fold:(Nat->Nat->Nat)->Nat->Natlist->Nat = 
  function f ->
    function x ->
      function l ->
        match l with
          Emptynlist() -> x
        | Consnlist(h,t) -> [[[fold,f],[[f,x],h]],t] 
;;


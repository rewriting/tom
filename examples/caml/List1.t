exception Res of int list 

let rec swapsort(l) = 
try (
  (

   %match l with
    conc(X1*,x,y,X2*) ->
      if (x>y)
      then raise (Res ( swapSort(`conc(X1*,y,x,X2*)) ))
  )
  ; l
)
with Res r -> r


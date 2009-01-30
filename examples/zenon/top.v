Require Import zenon8.

Parameter T S : Set.

Parameter g : T -> T -> T.
Parameter a b : T.
Parameter symb : T -> S.
Parameter _1 _2 : T -> T.
Parameter g_ a_ b_ : S.

Parameter symb_g :
  forall t : T, symb t = g_ <-> exists x : T, exists y : T, t = g x y.

Parameter symb_a :
  forall t : T, symb t = a_ <-> t = a.

Parameter symb_b :
  forall t : T, symb t = b_ <-> t = b.

Parameter st1_g :
  forall x y : T, _1 (g x y) = x.

Parameter st2_g :
  forall x y : T, _2 (g x y) = y.

Load proof.

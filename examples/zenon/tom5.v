(* ../zvtov/zvtov tom5.zv *)

Require Import zenon8.
Require Import zenon_coqbool8.
Require Import zenon8.

Parameter T S : Set.

Parameter plus : T -> T -> T.
Parameter fib suc : T -> T.
Parameter zero : T.
Parameter symb : T -> S.
Parameter _1 _2 : T -> T.
Parameter plus_ fib_ suc_ zero_ : S.

Parameter symb_suc :
  forall t : T, ((symb t) = suc_) <-> exists x : T, (t = (suc x)).

Parameter symb_fib :
  forall t : T, ((symb t) = fib_) <-> exists x : T, (t = (fib x)).

Parameter symb_zero :
  forall t : T, ((symb t) = zero_) <-> (t = zero).

Parameter st1_suc :
  forall x : T, ((_1 (suc x)) = x).

Parameter st1_fib :
  forall x : T, ((_1 (fib x)) = x).

Parameter symb_plus :
  forall t : T, ((symb t) = plus_) <-> exists x : T, exists y : T, (t = (plus x y)).

Parameter st1_plus :
  forall x : T, forall y : T, ((_1 (plus x y)) = x).

Parameter st2_plus :
  forall x : T, forall y : T, ((_2 (plus x y)) = y).

Parameter diff_suc_zero : forall x : T, ~((suc x) = zero).
Parameter diff_suc_fib : forall x : T, forall y : T, ~((suc x) = (fib y)).
Parameter diff_fib_zero : forall x : T, ~((fib x) = zero).

Parameter diff_plus_suc : forall x : T, forall y : T, forall z : T,
  ~((suc x) = plus y z).
Parameter diff_plus_fib : forall x : T, forall y : T, forall z : T,
  ~((fib x) = plus y z).
Parameter diff_plus_zero : forall x : T, forall y : T, ~((plus x y) = zero).

(*  *)
Lemma right_lem1 : forall _T_0 : T, (exists x : T,(exists y : T,(_T_0 = (plus x y)))) -> ((symb _T_0) = fib_) -> False.
do 1 intro; intros ZH23 ZH24.
elim ZH23; cintro _T_3; cintro ZH25.
elim ZH25; cintro _T_4; cintro ZH26.
generalize ( symb_fib _T_0); cintro ZH27.
apply (zenon_equiv_s _ _ ZH27); [ cintro ZH29; cintro ZH30 | cintro ZH24; cintro ZH28 ].
exact ( ZH29  ZH24).
elim ZH28; cintro _T_2; cintro ZH31.
generalize ( diff_plus_fib _T_2); cintro ZH32.
generalize ( ZH32 _T_3); cintro ZH33.
generalize ( ZH33 _T_4); cintro ZH34.
cut ((_T_0 = (plus _T_3 _T_4))); [ cintro  ZH26 | apply NNPP; cintro  ZH35 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH26 ZH34); [ cintro ZH36; cintro ZH37 | cintro ZH38; cintro ZH35 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH31 ZH36); [ cintro ZH39; cintro ZH40 | cintro ZH41; cintro ZH36 ].
auto.
auto.
auto.
exact ( ZH35  ZH26).
Qed.
Lemma right_lem2 : forall _T_0 : T, ~((_1 (fib (suc zero))) = (_1 _T_0)) -> ((fib (suc zero)) = _T_0) -> False.
do 1 intro; intros ZH42 ZH43.
apply (zenon_notequal_s _ _ _ ZH42); apply (zenon_equal_step _ _ (_1) (_1)); [ auto | cintro ZH44 ].
exact ( ZH44  ZH43).
Qed.
Lemma right_lem3 : forall _T_0 : T, forall _T_2 : T, ~((_1 (fib _T_2)) = (_1 _T_0)) -> (_T_0 = (fib _T_2)) -> False.
do 2 intro; intros ZH45 ZH31.
apply (zenon_notequal_s _ _ _ ZH45); apply (zenon_equal_step _ _ (_1) (_1)); [ auto | cintro ZH40 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH31 ZH40); [ cintro ZH36; cintro ZH41 | cintro ZH40; cintro ZH39 ].
auto.
auto.
Qed.
Lemma right_lem4 : forall _T_0 : T, forall _T_2 : T, (_T_0 = (fib _T_2)) -> ((_1 (fib _T_2)) = _T_2) -> ~(_T_2 = (_1 _T_0)) -> False.
do 2 intro; intros ZH31 ZH46 ZH47.
cut ((_T_2 = _T_2)); [ cintro  ZH48 | apply NNPP; cintro  ZH49 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH48 ZH47); [ cintro ZH49; cintro ZH49 | cintro ZH47; cintro ZH47 ].
auto.
apply (zenon_equalnotequal_s _ _ _ _ _ ZH46 ZH47); [ cintro ZH50; cintro ZH49 | cintro ZH47; cintro ZH45 ].
auto.
intros; eapply (right_lem3 _T_0 _T_2); eauto.
auto.
Qed.
Lemma right : (forall t : T,((((symb t) = fib_)/\(((symb (_1 t)) = suc_)/\~((symb (_1 (_1 t))) = zero_)))->((forall x : T,~((plus x zero) = t))/\((forall x : T,(forall y : T,~((plus x (suc y)) = t)))/\(~((fib zero) = t)/\(~((fib (suc zero)) = t)/\(exists x : T,((fib (suc x)) = t)))))))).
 apply NNPP; intros  ZH51.
apply ZH51; intro _T_0; apply NNPP; red; cintro ZH52.
apply (zenon_notimply_s _ _ ZH52); cintro ZH53; cintro ZH54.
elim ZH53; cintro ZH24; cintro ZH55.
elim ZH55; cintro ZH56; cintro ZH57.
apply (zenon_notand_s _ _ ZH54); [ cintro ZH58 | cintro  ZH59 ].
apply ZH58; intro _T_1; apply NNPP; red; cintro ZH60.
apply ZH60; clear ZH60; intro ZH61.
generalize ( symb_plus _T_0); cintro ZH62.
apply (zenon_equiv_s _ _ ZH62); [ cintro ZH64; cintro ZH65 | cintro ZH63; cintro ZH23 ].
apply ZH65.
exists _T_1; apply NNPP; red; intro ZH66.
apply ZH66.
exists zero; apply NNPP; red; intro ZH67.
apply (zenon_equalnotequal_s _ _ _ _ _ ZH61 ZH67); [ cintro ZH68; cintro ZH39 | cintro ZH67; cintro ZH69 ].
auto.
auto.
intros; eapply (right_lem1 _T_0); eauto.
apply (zenon_notand_s _ _ ZH59); [ cintro ZH70 | cintro  ZH71 ].
apply ZH70; intro _T_5; apply NNPP; red; cintro ZH72.
apply ZH72; intro _T_6; apply NNPP; red; cintro ZH73.
apply ZH73; clear ZH73; intro ZH74.
generalize ( symb_plus _T_0); cintro ZH62.
apply (zenon_equiv_s _ _ ZH62); [ cintro ZH64; cintro ZH65 | cintro ZH63; cintro ZH23 ].
apply ZH65.
exists _T_5; apply NNPP; red; intro ZH75.
apply ZH75.
exists (suc _T_6); apply NNPP; red; intro ZH76.
apply (zenon_equalnotequal_s _ _ _ _ _ ZH74 ZH76); [ cintro ZH77; cintro ZH39 | cintro ZH76; cintro ZH78 ].
auto.
auto.
intros; eapply (right_lem1 _T_0); eauto.
apply (zenon_notand_s _ _ ZH71); [ cintro ZH79 | cintro  ZH80 ].
apply ZH79; clear ZH79; intro ZH81.
generalize ( symb_suc zero); cintro ZH82.
apply (zenon_equiv_s _ _ ZH82); [ cintro ZH85; cintro ZH86 | cintro ZH83; cintro ZH84 ].
cut ((suc_ = suc_)); [ cintro  ZH87 | apply NNPP; cintro  ZH88 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH87 ZH85); [ cintro ZH89; cintro ZH89 | cintro ZH88; cintro ZH88 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH56 ZH89); [ cintro ZH90; cintro ZH88 | cintro ZH89; cintro ZH91 ].
auto.
apply (zenon_notequal_s _ _ _ ZH91); apply (zenon_equal_step _ _ (symb) (symb)); [ auto | cintro ZH92 ].
generalize ( st1_fib zero); cintro ZH93.
cut (((_1 _T_0) = (_1 (fib zero)))); [ cintro  ZH94 | apply NNPP; cintro  ZH95 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH94 ZH92); [ cintro ZH96; cintro ZH97 | cintro ZH98; cintro ZH92 ].
auto.
apply (zenon_equalnotequal_s _ _ _ _ _ ZH93 ZH98); [ cintro ZH99; cintro ZH100 | cintro ZH101; cintro ZH98 ].
auto.
auto.
apply (zenon_notequal_s _ _ _ ZH95); apply (zenon_equal_step _ _ (_1) (_1)); [ auto | cintro ZH102 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH81 ZH102); [ cintro ZH103; cintro ZH39 | cintro ZH102; cintro ZH104 ].
auto.
auto.
auto.
auto.
elim ZH84; cintro _T_7; cintro ZH105.
generalize ( diff_suc_zero _T_7); cintro ZH106.
apply (zenon_equalnotequal_s _ _ _ _ _ ZH105 ZH106); [ cintro ZH107; cintro ZH108 | cintro ZH106; cintro ZH101 ].
auto.
auto.
apply (zenon_notand_s _ _ ZH80); [ cintro ZH109 | cintro  ZH110 ].
apply ZH109; clear ZH109; intro ZH43.
generalize ( symb_zero zero); cintro ZH111.
apply (zenon_equiv_s _ _ ZH111); [ cintro ZH114; cintro ZH101 | cintro ZH112; cintro ZH113 ].
auto.
cut ((zero_ = zero_)); [ cintro  ZH115 | apply NNPP; cintro  ZH116 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH115 ZH57); [ cintro ZH117; cintro ZH117 | cintro ZH116; cintro ZH116 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH112 ZH117); [ cintro ZH114; cintro ZH116 | cintro ZH117; cintro ZH118 ].
auto.
apply (zenon_notequal_s _ _ _ ZH118); apply (zenon_equal_step _ _ (symb) (symb)); [ auto | cintro ZH119 ].
generalize ( st1_fib (suc zero)); cintro ZH120.
generalize ( st1_suc zero); cintro ZH121.
cut ((zero = zero)); [ cintro  ZH113 | apply NNPP; cintro  ZH101 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH113 ZH119); [ cintro ZH101; cintro ZH101 | cintro ZH119; cintro ZH119 ].
auto.
apply (zenon_equalnotequal_s _ _ _ _ _ ZH121 ZH119); [ cintro ZH122; cintro ZH101 | cintro ZH119; cintro ZH123 ].
auto.
apply (zenon_notequal_s _ _ _ ZH123); apply (zenon_equal_step _ _ (_1) (_1)); [ auto | cintro ZH124 ].
cut (((_1 (fib (suc zero))) = (_1 _T_0))); [ cintro  ZH125 | apply NNPP; cintro  ZH42 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH125 ZH124); [ cintro ZH126; cintro ZH127 | cintro ZH96; cintro ZH42 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH120 ZH126); [ cintro ZH128; cintro ZH129 | cintro ZH130; cintro ZH126 ].
auto.
auto.
auto.
intros; eapply (right_lem2 _T_0); eauto.
auto.
auto.
auto.
generalize ( symb_fib _T_0); cintro ZH27.
apply (zenon_equiv_s _ _ ZH27); [ cintro ZH29; cintro ZH30 | cintro ZH24; cintro ZH28 ].
exact ( ZH29  ZH24).
elim ZH28; cintro _T_2; cintro ZH31.
generalize ( st1_fib _T_2); cintro ZH46.
generalize ( symb_suc (_1 _T_0)); cintro ZH131.
apply (zenon_equiv_s _ _ ZH131); [ cintro ZH90; cintro ZH133 | cintro ZH56; cintro ZH132 ].
exact ( ZH90  ZH56).
elim ZH132; cintro _T_8; cintro ZH134.
apply ZH110.
exists _T_8; apply NNPP; red; intro ZH135.
cut ((_T_0 = _T_0)); [ cintro  ZH136 | apply NNPP; cintro  ZH39 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH136 ZH135); [ cintro ZH137; cintro ZH137 | cintro ZH39; cintro ZH39 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH31 ZH137); [ cintro ZH39; cintro ZH40 | cintro ZH138; cintro ZH137 ].
auto.
apply (zenon_notequal_s _ _ _ ZH138); apply (zenon_equal_step _ _ (fib) (fib)); [ auto | cintro ZH139 ].
cut ((_T_2 = (_1 _T_0))); [ cintro  ZH140 | apply NNPP; cintro  ZH47 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH140 ZH139); [ cintro ZH49; cintro ZH141 | cintro ZH142; cintro ZH139 ].
auto.
apply (zenon_equalnotequal_s _ _ _ _ _ ZH134 ZH142); [ cintro ZH96; cintro ZH143 | cintro ZH144; cintro ZH142 ].
auto.
auto.
intros; eapply (right_lem4 _T_0 _T_2); eauto.
auto.
auto.
Qed.


(*  *)
Lemma left_pos : (forall t : T,((exists x : T,((fib (suc x)) = t))->(((symb t) = fib_)/\((symb (_1 t)) = suc_)))).
 apply NNPP; intros  ZH17.
apply ZH17; intro _T_1; apply NNPP; red; cintro ZH18.
apply (zenon_notimply_s _ _ ZH18); cintro ZH19; cintro ZH20.
elim ZH19; cintro _T_0; cintro ZH21.
apply (zenon_notand_s _ _ ZH20); [ cintro ZH22 | cintro  ZH23 ].
generalize ( symb_fib (fib (suc _T_0))); cintro ZH24.
apply (zenon_equiv_s _ _ ZH24); [ cintro ZH27; cintro ZH28 | cintro ZH25; cintro ZH26 ].
apply ZH28.
exists (suc _T_0); apply NNPP; red; intro ZH29.
auto.
cut ((fib_ = fib_)); [ cintro  ZH30 | apply NNPP; cintro  ZH31 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH30 ZH22); [ cintro ZH32; cintro ZH32 | cintro ZH31; cintro ZH31 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH25 ZH32); [ cintro ZH27; cintro ZH31 | cintro ZH32; cintro ZH33 ].
auto.
apply (zenon_notequal_s _ _ _ ZH33); apply (zenon_equal_step _ _ (symb) (symb)); [ auto | cintro ZH34 ].
exact ( ZH34  ZH21).
auto.
auto.
generalize ( symb_suc (suc _T_0)); cintro ZH35.
apply (zenon_equiv_s _ _ ZH35); [ cintro ZH38; cintro ZH39 | cintro ZH36; cintro ZH37 ].
apply ZH39.
exists _T_0; apply NNPP; red; intro ZH40.
auto.
elim ZH37; cintro _T_2; cintro ZH41.
cut (((symb (_1 _T_1)) = (symb (suc _T_0)))); [ cintro  ZH42 | apply NNPP; cintro  ZH43 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH42 ZH23); [ cintro ZH44; cintro ZH45 | cintro ZH38; cintro ZH23 ].
auto.
apply (zenon_equalnotequal_s _ _ _ _ _ ZH36 ZH38); [ cintro ZH46; cintro ZH47 | cintro ZH48; cintro ZH38 ].
auto.
auto.
apply (zenon_notequal_s _ _ _ ZH43); apply (zenon_equal_step _ _ (symb) (symb)); [ auto | cintro ZH49 ].
cut (((_1 _T_1) = (suc _T_2))); [ cintro  ZH50 | apply NNPP; cintro  ZH51 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH50 ZH49); [ cintro ZH52; cintro ZH53 | cintro ZH54; cintro ZH49 ].
auto.
apply (zenon_equalnotequal_s _ _ _ _ _ ZH41 ZH54); [ cintro ZH55; cintro ZH56 | cintro ZH54; cintro ZH40 ].
auto.
auto.
generalize ( st1_fib (suc _T_0)); cintro ZH57.
cut (((suc _T_0) = (suc _T_2))); [ cintro  ZH41 | apply NNPP; cintro  ZH55 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH41 ZH51); [ cintro ZH58; cintro ZH53 | cintro ZH56; cintro ZH55 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH57 ZH58); [ cintro ZH59; cintro ZH40 | cintro ZH58; cintro ZH60 ].
auto.
apply (zenon_notequal_s _ _ _ ZH60); apply (zenon_equal_step _ _ (_1) (_1)); [ auto | cintro ZH34 ].
exact ( ZH34  ZH21).
auto.
exact ( ZH55  ZH41).
Qed.



(*  *)
Lemma left_neg_lem1 : forall _T_1 : T, forall _T_0 : T, ~((_1 (fib (suc _T_0))) = (_1 _T_1)) -> ((fib (suc _T_0)) = _T_1) -> False.
do 2 intro; intros ZH16 ZH17.
apply (zenon_notequal_s _ _ _ ZH16); apply (zenon_equal_step _ _ (_1) (_1)); [ auto | cintro ZH18 ].
exact ( ZH18  ZH17).
Qed.
Lemma left_neg_lem2 : forall _T_1 : T, forall _T_0 : T, ((_1 (fib (suc _T_0))) = (suc _T_0)) -> ~((suc _T_0) = (_1 _T_1)) -> ((fib (suc _T_0)) = _T_1) -> False.
do 2 intro; intros ZH19 ZH20 ZH17.
cut (((_1 (fib (suc _T_0))) = (_1 _T_1))); [ cintro  ZH21 | apply NNPP; cintro  ZH16 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH21 ZH20); [ cintro ZH22; cintro ZH23 | cintro ZH24; cintro ZH16 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH19 ZH22); [ cintro ZH25; cintro ZH26 | cintro ZH27; cintro ZH22 ].
auto.
auto.
auto.
intros; eapply (left_neg_lem1 _T_1 _T_0); eauto.
Qed.
Lemma left_neg_lem3 : forall _T_1 : T, forall _T_0 : T, ~((_1 (suc _T_0)) = (_1 (_1 _T_1))) -> ((fib (suc _T_0)) = _T_1) -> ((_1 (fib (suc _T_0))) = (suc _T_0)) -> False.
do 2 intro; intros ZH28 ZH17 ZH19.
apply (zenon_notequal_s _ _ _ ZH28); apply (zenon_equal_step _ _ (_1) (_1)); [ auto | cintro ZH20 ].
intros; eapply (left_neg_lem2 _T_1 _T_0); eauto.
Qed.
Lemma left_neg : (forall t : T,((~((fib (suc zero)) = t)/\(exists x : T,((fib (suc x)) = t)))->~((symb (_1 (_1 t))) = zero_))).
 apply NNPP; intros  ZH29.
apply ZH29; intro _T_1; apply NNPP; red; cintro ZH30.
apply (zenon_notimply_s _ _ ZH30); cintro ZH31; cintro ZH32.
apply ZH32; clear ZH32; intro ZH33.
elim ZH31; cintro ZH34; cintro ZH35.
elim ZH35; cintro _T_0; cintro ZH17.
cut (((fib (suc zero)) = (fib (suc _T_0)))); [ cintro  ZH36 | apply NNPP; cintro  ZH37 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH36 ZH34); [ cintro ZH38; cintro ZH39 | cintro ZH18; cintro ZH34 ].
auto.
apply (zenon_equalnotequal_s _ _ _ _ _ ZH17 ZH18); [ cintro ZH40; cintro ZH41 | cintro ZH42; cintro ZH18 ].
auto.
auto.
apply (zenon_notequal_s _ _ _ ZH37); apply (zenon_equal_step _ _ (fib) (fib)); [ auto | cintro ZH43 ].
apply (zenon_notequal_s _ _ _ ZH43); apply (zenon_equal_step _ _ (suc) (suc)); [ auto | cintro ZH44 ].
generalize ( st1_fib (suc _T_0)); cintro ZH19.
generalize ( st1_suc _T_0); cintro ZH45.
generalize ( st1_fib _T_0); cintro ZH46.
cut ((zero = (_1 (fib _T_0)))); [ cintro  ZH47 | apply NNPP; cintro  ZH48 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH47 ZH44); [ cintro ZH49; cintro ZH50 | cintro ZH51; cintro ZH44 ].
auto.
apply (zenon_equalnotequal_s _ _ _ _ _ ZH46 ZH51); [ cintro ZH52; cintro ZH53 | cintro ZH54; cintro ZH51 ].
auto.
auto.
generalize ( symb_zero (_1 (_1 _T_1))); cintro ZH55.
apply (zenon_equiv_s _ _ ZH55); [ cintro ZH57; cintro ZH58 | cintro ZH33; cintro ZH56 ].
exact ( ZH57  ZH33).
cut (((_1 (_1 _T_1)) = (_1 (fib _T_0)))); [ cintro  ZH59 | apply NNPP; cintro  ZH60 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH59 ZH48); [ cintro ZH58; cintro ZH50 | cintro ZH52; cintro ZH60 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH56 ZH58); [ cintro ZH61; cintro ZH62 | cintro ZH49; cintro ZH58 ].
auto.
auto.
auto.
cut ((_T_0 = (_1 (fib _T_0)))); [ cintro  ZH63 | apply NNPP; cintro  ZH53 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH63 ZH60); [ cintro ZH64; cintro ZH65 | cintro ZH52; cintro ZH53 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH45 ZH64); [ cintro ZH66; cintro ZH54 | cintro ZH64; cintro ZH28 ].
auto.
intros; eapply (left_neg_lem3 _T_1 _T_0); eauto.
auto.
apply (zenon_equalnotequal_s _ _ _ _ _ ZH46 ZH53); [ cintro ZH51; cintro ZH54 | cintro ZH53; cintro ZH52 ].
auto.
auto.
Qed.



(*  *)
Lemma tout_lem1 : forall _T_1 : T, ~(exists x : T,((fib (suc x)) = (fib (suc _T_1)))) -> False.
do 1 intro; intros ZH5.
apply ZH5.
exists _T_1; apply NNPP; red; intro ZH6.
auto.
Qed.
Lemma tout : (forall t : T,(((forall x : T,~((plus x zero) = t))/\((forall x : T,(forall y : T,~((plus x (suc y)) = t)))/\(~((fib zero) = t)/\(~((fib (suc zero)) = t)/\(exists x : T,((fib (suc x)) = t))))))<->(((symb t) = fib_)/\(((symb (_1 t)) = suc_)/\~((symb (_1 (_1 t))) = zero_))))).
 apply NNPP; intros  ZH7.
apply ZH7; intro _T_0; apply NNPP; red; cintro ZH8.
apply (zenon_notequiv_s _ _ ZH8); [ cintro ZH11; cintro ZH10 | cintro ZH9; cintro ZH12 ].
elim ZH10; cintro ZH13; cintro ZH14.
elim ZH14; cintro ZH15; cintro ZH16.
generalize ( right _T_0); cintro ZH17.
apply (zenon_imply_s _ _ ZH17); [ cintro ZH12 | cintro ZH9 ].
apply (zenon_notand_s _ _ ZH12); [ cintro ZH18 | cintro  ZH19 ].
exact ( ZH18  ZH13).
apply (zenon_notand_s _ _ ZH19); [ cintro ZH20 | cintro  ZH21 ].
exact ( ZH20  ZH15).
apply ZH21; clear ZH21; intro ZH22.
exact ( ZH16  ZH22).
exact ( ZH11  ZH9).
elim ZH9; cintro ZH23; cintro ZH24.
elim ZH24; cintro ZH25; cintro ZH26.
elim ZH26; cintro ZH27; cintro ZH28.
elim ZH28; cintro ZH29; cintro ZH30.
elim ZH30; cintro _T_1; cintro ZH31.
apply (zenon_notand_s _ _ ZH12); [ cintro ZH18 | cintro  ZH19 ].
generalize ( left_pos (fib (suc _T_1))); cintro ZH32.
apply (zenon_imply_s _ _ ZH32); [ cintro ZH5 | cintro ZH34 ].
intros; eapply (tout_lem1 _T_1); eauto.
elim ZH34; cintro ZH35; cintro ZH36.
cut ((fib_ = fib_)); [ cintro  ZH37 | apply NNPP; cintro  ZH38 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH37 ZH18); [ cintro ZH39; cintro ZH39 | cintro ZH38; cintro ZH38 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH35 ZH39); [ cintro ZH40; cintro ZH38 | cintro ZH39; cintro ZH41 ].
auto.
apply (zenon_notequal_s _ _ _ ZH41); apply (zenon_equal_step _ _ (symb) (symb)); [ auto | cintro ZH42 ].
exact ( ZH42  ZH31).
auto.
auto.
apply (zenon_notand_s _ _ ZH19); [ cintro ZH20 | cintro  ZH21 ].
generalize ( left_pos (fib (suc _T_1))); cintro ZH32.
apply (zenon_imply_s _ _ ZH32); [ cintro ZH5 | cintro ZH34 ].
intros; eapply (tout_lem1 _T_1); eauto.
elim ZH34; cintro ZH35; cintro ZH36.
cut ((suc_ = suc_)); [ cintro  ZH43 | apply NNPP; cintro  ZH44 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH43 ZH20); [ cintro ZH45; cintro ZH45 | cintro ZH44; cintro ZH44 ].
apply (zenon_equalnotequal_s _ _ _ _ _ ZH36 ZH45); [ cintro ZH46; cintro ZH44 | cintro ZH45; cintro ZH47 ].
auto.
apply (zenon_notequal_s _ _ _ ZH47); apply (zenon_equal_step _ _ (symb) (symb)); [ auto | cintro ZH48 ].
apply (zenon_notequal_s _ _ _ ZH48); apply (zenon_equal_step _ _ (_1) (_1)); [ auto | cintro ZH42 ].
exact ( ZH42  ZH31).
auto.
auto.
apply ZH21; clear ZH21; intro ZH22.
generalize ( left_neg _T_0); cintro ZH49.
apply (zenon_imply_s _ _ ZH49); [ cintro ZH50 | cintro ZH16 ].
apply (zenon_notand_s _ _ ZH50); [ cintro ZH51 | cintro  ZH52 ].
apply ZH51; clear ZH51; intro ZH53.
exact ( ZH29  ZH53).
exact ( ZH52  ZH30).
exact ( ZH16  ZH22).
Qed.


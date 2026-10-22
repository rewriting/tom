// Port of test/regress/BadOp.t, a non-regression test of tom's
// diagnostics (test/build.xml, target regress): tom must reject it, with
// the same errors as legacy/ on the original (port_test.go, Regress).
// port: the original has no host code (no class), hence no package
// clause here either.

%op None op() {

}

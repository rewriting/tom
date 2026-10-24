package backend

// Stubs of the generator methods not ported yet (plan J4.1b): each family
// task moves the methods it ports into its .t file and deletes their
// lines here; Task 7 deletes the file. A stub panics with the name of the
// method: the P2 counter (test/tools/j4) reports it.

import "tom/engine/platform"

// notPorted is the panic of a method not ported yet.
func notPorted(method string) {
	panic(&platform.RuntimeError{Msg: "backend: " + method + " is not ported yet (J4.1b)"})
}

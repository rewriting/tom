package platform

import (
	"fmt"
	"runtime"

	"tom/library/aterm"
)

// RuntimeError is the panic value of Java's TomRuntimeException and of the
// other runtime exceptions the compiler throws on purpose
// (IllegalArgumentException, InternalError): spec J4 §4.4.
type RuntimeError struct{ Msg string }

func (e *RuntimeError) Error() string { return e.Msg }

// IsRuntime reports whether a recovered panic value is one Java's
// catch(RuntimeException) would catch: a *RuntimeError, a *aterm.GomError
// (the getters and casts of the Gom terms: UnsupportedOperationException,
// IllegalArgumentException) or a runtime.Error (a nil dereference:
// NullPointerException; a failed type assertion: ClassCastException; an
// index out of range).
func IsRuntime(v any) bool {
	switch v.(type) {
	case *RuntimeError, *aterm.GomError, runtime.Error:
		return true
	}
	return false
}

// CatchRuntime runs f and reports whether it returned normally: a runtime
// exception (IsRuntime) is recovered and gives false; any other panic goes
// on. Java: try { f } catch(RuntimeException e) { } (GoGenerator.t:512-526).
func CatchRuntime(f func()) (ok bool) {
	defer func() {
		if v := recover(); v != nil {
			if !IsRuntime(v) {
				panic(v)
			}
			ok = false
		}
	}()
	f()
	return true
}

// Run is the single recovery point of the driver (spec J4 §4.4): it runs
// f; a runtime exception becomes the ERROR exceptionMessage at line -1, as
// in BackendPlugin.run (BackendPlugin.t:159-166), followed by the panic
// text on a plain line (Java: printStackTrace), and Run returns false.
func Run(r *Reporter, file string, f func()) (ok bool) {
	defer func() {
		if v := recover(); v != nil {
			if !IsRuntime(v) {
				panic(v)
			}
			r.Error(file, -1, ExceptionMessage, file)
			fmt.Fprintf(r.w, "tomgo: %v\n", v)
			ok = false
		}
	}()
	f()
	return true
}

// JavaString is String.valueOf(o) for the values of the compiler: "null"
// for nil, fmt.Sprint otherwise (a Gom term's String() is Java's
// toString()).
func JavaString(v any) string {
	if v == nil {
		return "null"
	}
	return fmt.Sprint(v)
}

// Frozen prototype copy — the maintained version is src/go/tom/library/sl (module tom).

package sl

import (
	"errors"
	"testing"
)

func visitThroughPanic(err error) (res any, out error) {
	defer RecoverVisitFailure(&out)
	return MustVisit(42, err), nil
}

func TestMustVisitPassesValue(t *testing.T) {
	res, err := visitThroughPanic(nil)
	if err != nil || res != 42 {
		t.Fatalf("got (%v, %v), want (42, nil)", res, err)
	}
}

func TestMustVisitCarriesFailure(t *testing.T) {
	_, err := visitThroughPanic(ErrVisitFailure)
	if !errors.Is(err, ErrVisitFailure) {
		t.Fatalf("got %v, want ErrVisitFailure", err)
	}
}

func TestRecoverVisitFailureRepanicsOthers(t *testing.T) {
	defer func() {
		if r := recover(); r != "boom" {
			t.Fatalf("recovered %v, want boom", r)
		}
	}()
	var err error
	func() {
		defer RecoverVisitFailure(&err)
		panic("boom")
	}()
}

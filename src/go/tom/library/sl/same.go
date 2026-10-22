package sl

import "reflect"

// same is Java's reference comparison (==) of two subjects, used by the
// *Id combinators and All to detect an unchanged child. Values of a
// comparable dynamic type are compared with ==; a slice, map or func
// (which == would panic on) is the same as another one of the same type
// when they share their data pointer (and, for a slice, its length):
// the closest Go counterpart of the identity of a Java object.
func same(a, b any) bool {
	if a == nil || b == nil {
		return a == nil && b == nil
	}
	ta, tb := reflect.TypeOf(a), reflect.TypeOf(b)
	if ta != tb {
		return false
	}
	if ta.Comparable() {
		return a == b
	}
	va, vb := reflect.ValueOf(a), reflect.ValueOf(b)
	switch va.Kind() {
	case reflect.Slice:
		return va.Len() == vb.Len() && va.Pointer() == vb.Pointer()
	case reflect.Map, reflect.Func:
		return va.Pointer() == vb.Pointer()
	}
	return false
}

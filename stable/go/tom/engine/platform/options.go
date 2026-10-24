// Package platform holds what the Go suffix of the compiler takes from the
// Java platform (tom.platform) and from tom.engine.TomMessage (spec J4
// §4.4; plan J4.1a): the options of the compilation, read from the bridge
// bundle; the messages and the diagnostics protocol GoBridgePlugin relays;
// the Go form of Java's runtime exceptions.
package platform

import (
	"strconv"

	"tom/engine/bridge"
)

// Options are the options of the compilation as the bridge bundle carries
// them (spec J4 §4.2: every option of TomOptionManager, by its canonical
// name). Java: TomOptionManager.getOptionValue (TomOptionManager.t:184)
// and the getOption{Boolean,Integer,String}Value of TomGenericPlugin
// (TomGenericPlugin.t:258-280).
type Options struct {
	byName map[string]bridge.Option
}

// NewOptions indexes the options of a bundle.
func NewOptions(opts []bridge.Option) *Options {
	o := &Options{byName: make(map[string]bridge.Option, len(opts))}
	for _, x := range opts {
		o.byName[x.Name] = x
	}
	return o
}

// Has reports whether the option exists.
func (o *Options) Has(name string) bool {
	_, ok := o.byName[name]
	return ok
}

// value is the value of an option of the given kind. An unknown option is
// Java's optionNotFound error followed by a RuntimeException
// (TomOptionManager.t:286-292), a kind mismatch Java's ClassCastException:
// both are runtime exceptions here.
func (o *Options) value(name, kind string) string {
	x, ok := o.byName[name]
	if !ok {
		panic(&RuntimeError{Msg: OptionNotFound.Text(name)})
	}
	if x.Kind != kind {
		panic(&RuntimeError{Msg: "option " + name + " is a " + x.Kind + ", not a " + kind})
	}
	return x.Value
}

// Bool is getOptionBooleanValue.
func (o *Options) Bool(name string) bool { return o.value(name, "bool") == "true" }

// Int is getOptionIntegerValue.
func (o *Options) Int(name string) int {
	n, err := strconv.Atoi(o.value(name, "int"))
	if err != nil {
		panic(&RuntimeError{Msg: "option " + name + ": " + err.Error()})
	}
	return n
}

// String is getOptionStringValue.
func (o *Options) String(name string) string { return o.value(name, "string") }

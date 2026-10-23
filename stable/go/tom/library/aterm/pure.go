package aterm

import "sync"

// PureFactory is the Go name of the Java class aterm.pure.PureFactory,
// the factory Tom programs use; it is the same type as [Factory].
type PureFactory = Factory

// NewPureFactory is Java's `new PureFactory(initialSize)`. The size
// hint only sizes the Java hash table; it is ignored here.
func NewPureFactory(initialSize int) *PureFactory { return NewFactory() }

var (
	singletonOnce sync.Once
	singleton     *PureFactory
)

// GetInstance is Java's aterm.pure.SingletonFactory.getInstance(): the
// process-wide factory, created on first use.
func GetInstance() *PureFactory {
	singletonOnce.Do(func() { singleton = NewPureFactory(16) })
	return singleton
}

// GetName is Java's ATermAppl.getName(): the name of the head symbol.
func (t *ATermAppl) GetName() string { return t.fun.name }

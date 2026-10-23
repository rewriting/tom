package harness

import (
	"context"
	"errors"
	"testing"
	"time"
)

func TestRunInTimeout(t *testing.T) {
	start := time.Now()
	_, err := RunInTimeout(t.TempDir(), 200*time.Millisecond, nil, "sleep", "30")
	if !errors.Is(err, context.DeadlineExceeded) {
		t.Fatalf("err = %v, want a deadline error", err)
	}
	if time.Since(start) > 10*time.Second {
		t.Fatal("sleep was not killed by the timeout")
	}
	out, err := RunInTimeout(t.TempDir(), 10*time.Second, []string{"X=1"}, "echo", "hi")
	if err != nil || out != "hi\n" {
		t.Fatalf("out %q err %v", out, err)
	}
}

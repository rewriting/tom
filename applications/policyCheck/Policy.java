import verify.example.types.RequestUponState;


public interface Policy {
	Response transition (RequestUponState rus);
}

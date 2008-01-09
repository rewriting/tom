package policy;
import verify.example.types.*;

public interface Policy {
  public Response transition(RequestUponState rus);
}

import accesscontrol.types.*;

public interface Policy {
  public Response transition(RequestUponState rus);
}

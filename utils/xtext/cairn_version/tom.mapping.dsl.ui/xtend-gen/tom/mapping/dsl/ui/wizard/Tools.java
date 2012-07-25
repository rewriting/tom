package tom.mapping.dsl.ui.wizard;

@SuppressWarnings("all")
public class Tools {
  public String path(final String p) {
    String _replaceAll = p.replaceAll("\\.", "/");
    return _replaceAll;
  }
}

package definition;

public class Constructor{
  private String name;
  private Field[] fields;
  
  public Constructor(String name, Field[] fields){
    this.name = name;
    this.fields = fields;
  }
  
  public Field[] getFields(){
    return fields;
  }
  
  public String getName(){
    return name;
  }
}
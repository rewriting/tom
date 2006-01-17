public class Author{

  private String firstName;
  private String lastName;
  private String status;

  public Author(String firstName, String lastName){
    this.firstName = firstName;
    this.lastName = lastName;
    status = "";
  }

  public Author(String firstName, String lastName, String status){
    this.firstName = firstName;
    this.lastName = lastName;
    this.status = status;
  }

  public String getLastname(){
    return lastName;
  }

  public String getFirstname(){
    return firstName;
  }

  public String getStatus(){
    return status;
  }
}

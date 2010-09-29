package hello.account;

/**
 *
 * @author Rahul Biswas
 */

import javax.persistence.*;
import java.util.*;

@Entity
public class AccountOwner {
  
  @Id  
  Long id;
  String name;

  @OneToMany(cascade=CascadeType.ALL, mappedBy="accountOwner")
  Collection<Account> accounts;
  
  public AccountOwner(){}
  
  public String getName(){
    return name;
  }
  
  public void setName(String name){
    this.name = name;
  }
  
  public Collection<Account> getAccounts(){
    return accounts;
  }
  
  public void setAccounts(Collection<Account> accounts){
    this.accounts = accounts;
  }
  
  public void setId(Long id){
    this.id = id;
  }
  
  public Long getId(){
    return id;
  }

}
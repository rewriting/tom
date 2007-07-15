package hello;  

import java.util.*; 
import javax.persistence.*;
import hello.account.*;
 
public class HelloWorldAccount {
   
  %include{ MappingAccount.tom }          
 
  public static void main(String[] args) {

    // Start EntityManagerFactory
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("helloworld");
   
    // First unit of work
//    EntityManager em = emf.createEntityManager();
//    EntityTransaction tx = em.getTransaction();
//    tx.begin();
//    
//    AccountOwner owner = new AccountOwner();
//    owner.setId((long)1);
//    owner.setName("John Doe");
//    
//    Account account1 = new SavingsAccount();
//    account1.setAcctNum("1");
//    account1.setBalance(100);
//    account1.setDescription("first account");
//    account1.setName("savings account 1");
//    account1.setCreated((new Date()).getTime() + "");
//    ((SavingsAccount)account1).setSavingsRate(5);
//    account1.setAccountOwner(owner);
//    
//    Account account2 = new CheckingAccount();
//    account2.setAcctNum("2");
//    account2.setBalance(200);
//    account2.setDescription("second account");
//    account2.setName("checking account 1");
//    account2.setCreated((new Date()).getTime() + "");
//    ((CheckingAccount)account2).setIsOverDraftAllowed(false);
//    account2.setAccountOwner(owner);
//    
//    Account account3 = new CreditCardAccount();
//    account3.setAcctNum("3");
//    account3.setBalance(300);
//    account3.setDescription("third account");
//    account3.setName("credit card account 1");
//    account3.setCreated((new Date()).getTime() + "");
//    ((CreditCardAccount)account3).setIssuingBank("BRD");
//    ((CreditCardAccount)account3).setCreditCardNumber("123124124124124");
//    ((CreditCardAccount)account3).setExpiresOn("does not expire");
//    account3.setAccountOwner(owner);
//
//    em.persist(owner);
//    em.persist(account1);
//    em.persist(account2);
//    em.persist(account3);
//
//    tx.commit();
//    em.close();

    // Second unit of work
    EntityManager newEm = emf.createEntityManager();
    EntityTransaction newTx = newEm.getTransaction();
    newTx.begin(); 

    List accounts = newEm.createQuery("select m from Account m").getResultList();
    System.out.println( accounts.size() + " account(s) found" );
     
    %match(accounts) {
      accountList(_*,Account[name=n,accountOwner=AccountOwner[name=owname]],_*) -> { 
        System.out.println("Account name:" + `n + " with owner name:" + `owname); 
        }
    }
    
    // TODO - a nice example with owner  
    
    %match(accounts) {
      accountList(_*,x,_*) -> { 
        System.out.println("x=" + `x);
        %match(x){          
          SavingsAccount[name=n]  -> { System.out.println("a savings, with name=" + `n);}
          CreditCardAccount[] -> { System.out.println("a credit card");}   
          CheckingAccount[] -> { System.out.println("checking");}                   
        }
      }
    }


    newTx.commit();
    newEm.close();  

    // Shutting down the application
    emf.close();
  }
}

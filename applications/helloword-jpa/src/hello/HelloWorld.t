package hello;

import java.util.*;
import javax.persistence.*;
import beans.*;
import beans.entities.types.accounts.*;
import beans.entities.types.accountslist.*;
import beans.entities.types.*;

public class HelloWorld {
 
  %include{ MappingMessage.tom }     

  public static void main(String[] args) {

    // Start EntityManagerFactory
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("helloworld");
   
    // First unit of work
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    
    Message mes1 = new Message("cucu_1");
    em.persist(mes1);
    Message mes2 = new Message("cucu_2");
    mes2.setNextMessage(mes1);
    em.persist(mes2);

    tx.commit();
    em.close();

    // Second unit of work
    EntityManager newEm = emf.createEntityManager();
    EntityTransaction newTx = newEm.getTransaction();
    newTx.begin(); 

    List messages = newEm.createQuery("select m from Message m").getResultList();
    System.out.println( messages.size() + " messages(s) found:" );
    
    %match(messages) {
      messList(_*,Message[text=tx,nextMessage=Message[text=nextText]],_*) -> {
        System.out.println("Message with text=" + `tx + " and next mes Text:" + `nextText);
      }
    }


    newTx.commit();
    newEm.close();  

    // Shutting down the application
    emf.close();
  }
}

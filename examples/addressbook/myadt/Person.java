package myadt;

import java.util.Date;

/*
 * Created on Sep 23, 2003
 */

/**
 * @author julien
 *
 */
public class Person {
	private Date birthdate;
	private String firstName, lastName;
	
	public Person(String fn, String ln, Date bd) {
		this.firstName = fn;
		this.lastName = ln;
		this.birthdate = bd;
	}

	public Date getBirthdate() {
		return birthdate;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}

	public void setBirthdate(Date date) {
		birthdate = date;
	}
	public void setLastName(String string) {
		lastName = string;
	}
	public void setFirstName(String string) {
		firstName = string;
	}

}

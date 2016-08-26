package tel_ran.persons.tests;

import java.util.function.Predicate;

import tel_ran.persons.model.entities.Employee;
import tel_ran.persons.model.entities.Person;

public class PredicateCompany implements Predicate<Person> {
String company;

	public PredicateCompany(String company) {
	super();
	this.company = company;
}

	@Override
	public boolean test(Person p) {
		// TODO Auto-generated method stub
		return p instanceof Employee && ((Employee)p).getCompany().equals(company);
	}

}

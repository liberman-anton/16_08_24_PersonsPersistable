package tel_ran.persons.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tel_ran.persons.model.dao.PersonsRepositoryMaps;
import tel_ran.persons.model.entities.Address;
import tel_ran.persons.model.entities.Child;
import tel_ran.persons.model.entities.Employee;
import tel_ran.persons.model.entities.Person;
import tel_ran.persons.model.interfaces.PersonsCrudRepository;

import java.io.IOException;
import java.util.*;

public class PersonsModelTests {

	Person[] persons = {new Child(123, "Moshe", 2011, new Address("Rehovot", "Plaut", 10), "tel-ran"),
			new Employee(124, "Vasya", 1980, new Address("Rehovot", "Plaut", 10), "Tel-ran", 15000),
			new Child(125, "Sara", 2013, new Address("Rehovot", "Plaut", 10), "none"),
			new Child(126, "Olya", 2010, new Address("Beersheva", "Yalim", 3), "klita"),
			new Child(127, "Sasha", 2012, new Address("Beersheva", "Yalim", 3), "klita"),
			new Employee(128, "David", 1970, new Address("Beersheva", "Yalim", 3), "Motorola", 20000),
			new Child(129, "Tolya", 2010, new Address("Rehovot", "Plaut", 10), "Salut"),
			new Employee(130, "Serg", 1975, new Address("Beersheva", "Yalim", 3), "Motorola", 18000)
	};
	PersonsCrudRepository personsRepository;
	
	@Before
	public void setUp() throws Exception {
		personsRepository = new PersonsRepositoryMaps();
		for(Person person : persons){
			personsRepository.addPerson(person);
		}
	}

	@Test
	public void saveRestoreTest() {
		try {
			personsRepository.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Iterator<Person> it = personsRepository.iterator();
		while(it.hasNext()){
			Person per = null;
			try {
				per = it.next();
			} catch (Exception e) {
				break;
			}
			if(per != null)
				personsRepository.removePerson(per .getId());
		}
		
		try {
			personsRepository.restore();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		iteratorTest();
	}
	@Test
	public void iteratorTest() {
		int[] indexes = {0,1,2,3,4,5,6,7};
		HashSet<Person> setPersons = getSet(indexes);
		testIterable(setPersons,personsRepository);
	}

	private void testIterable(HashSet<Person> setPersons, Iterable<Person> iterable) {
		int ind = 0;
		for(Person person : iterable){
			assertTrue(setPersons.contains(person));
			ind++;
		}
		assertEquals(setPersons.size(), ind);
	}

	private HashSet<Person> getSet(int[] indexes) {
		HashSet<Person> res = new HashSet<>();
		for(int index : indexes){
			res.add(persons[index]);
		}
		return res;
	}
	@Test
	public void removeAddTest(){
		assertFalse(personsRepository.addPerson(persons[0]));
		assertEquals(persons[0], personsRepository.removePerson(123));
		assertTrue(personsRepository.addPerson(persons[0]));
		iteratorTest();
	}
	@Test
	public void getPersonTest(){
		assertEquals(persons[1], personsRepository.getPerson(124));
	}
	@Test
	public void updateSalaryTest(){
		Iterable<Person> personsSalary = personsRepository.getPersonsBySalary(15000, 15001);
		int indexes[] = {1};
		testIterable(getSet(indexes), personsSalary);
		assertTrue(personsRepository.updateSalary(124, 20000));
		testIterable(getSet(new int[0]), personsRepository.getPersonsBySalary(15000, 15001));
		testIterable(getSet(new int[]{1,5}), personsRepository.getPersonsBySalary(20000, 20001));
		assertFalse(personsRepository.updateSalary(125, 100));
	}
	@Test
	public void updateAddressTest(){
		assertTrue(personsRepository.updateAddress(128, new Address("Rehovot", "Plaut", 10)));
		int[] indexesRehovot = {0,1,2,5,6};
		testIterable(getSet(indexesRehovot), personsRepository.getPersonsByAddress(new Address("Rehovot", "Plaut", 10)));
		int[] indexesBeersheva = {3,4,7};
		testIterable(getSet(indexesBeersheva), personsRepository.getPersonsByAddress(new Address("Beersheva", "Yalim", 3)));
	}
	@Test
	public void getPersonsTest(){
		int[] indexes = {5,7};
		testIterable(getSet(indexes), 
				personsRepository.getPersons(p->p instanceof Employee && ((Employee)p).getCompany().equals("Motorola")));
				//personsRepository.getPersons(new PredicateCompany("Motorola")));
		
	}
	@Test
	public void getPersonsByYearTest(){
		int[] indexes = {0,2,3,4,6};
		testIterable(getSet(indexes), personsRepository.getPersonsByBirthYear(2010,2015));
		
	}
}

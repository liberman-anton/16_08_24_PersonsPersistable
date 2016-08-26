package tel_ran.persons.model.dao;

import java.util.Map.Entry;
import java.util.function.Predicate;

import tel_ran.persons.model.entities.Address;
import tel_ran.persons.model.entities.Employee;
import tel_ran.persons.model.entities.Person;
import tel_ran.persons.model.interfaces.PersonsCrudRepository;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class PersonsRepositoryMaps implements PersonsCrudRepository {
	Map<Integer,Person> persons = new HashMap<>();// key - id
	Map<Integer,List<Person>> personsSalary = new TreeMap<>();// key - salary
	Map<Address,List<Person>> personsAddress = new HashMap<>();// key - address
	Map<Integer,List<Person>> personsYear = new TreeMap<>();// key - birthYear
	
	@Override
	public Iterator<Person> iterator() {
		return persons.values().iterator();
	}

	@Override
	public boolean addPerson(Person person) {
		int id = person.getId();
		if(persons.containsKey(id))
			return false;
		persons.put(id, person);
		addPersonsSalary(person);
		addPersonsAddress(person);
		addPersonsYear(person);
		return true;
	}

	private void addPersonsYear(Person person) {
		int year = person.getBirthYear();
		List<Person> persons = personsYear.get(year);
		if(persons == null){
			persons = new LinkedList<>();
			personsYear.put(year, persons);
		}
		persons.add(person);		
	}

	private void addPersonsSalary(Person person) {
		//cast to Employee if Exception the throw don't add
		Employee employee = new Employee(0, null, 0, null, null, null);
		try{
			employee = (Employee) person;  
		}catch (Throwable e){
			return;
		}
		Integer salary = employee.getSalary();
		List<Person> persons = personsSalary.get(salary);
		if(persons == null){
			persons = new LinkedList<>();
			personsSalary.put(salary, persons);
		}
		persons.add(employee);
	}

	private void addPersonsAddress(Person person) {
		Address address = person.getAddress();
		List<Person> persons = personsAddress.get(address);
		if(persons == null){
			persons = new LinkedList<>();
			personsAddress.put(address, persons);
		}
		persons.add(person);
	}

	@Override
	public Person removePerson(int id) {
		Person person = persons.remove(id);
		if(person != null){
			removePersonsSalary(person);
			removePersonsAddress(person);
			removePersonsYear(person);
		}
		return person;
	}

	private void removePersonsYear(Person person) {
		int year = person.getBirthYear();
		List<Person> persons = personsYear.get(year);
		persons.remove(person);
	}

	private void removePersonsAddress(Person person) {
		Address address = person.getAddress();
		List<Person> persons = personsAddress.get(address);
		persons.remove(person);
	}

	private void removePersonsSalary(Person person) {
		Employee employee = new Employee(0, null, 0, null, null, null);
		try{
			employee = (Employee) person;  
		}catch (Exception e){
			return;
		}
		Integer salary = employee.getSalary();
		List<Person> persons = personsSalary.get(salary);
		persons.remove(employee);
	}

	@Override
	public Person getPerson(int id) {
		
		return persons.get(id);
	}

	@Override
	public Iterable<Person> getPersonsBySalary(int minSalary, int maxSalary) {
		
		return getSubPersons(minSalary,maxSalary,personsSalary);
	}
	
	private Iterable<Person> getSubPersons(int keyFrom, int keyTo, Map<Integer,List<Person>> personsByKey){
		NavigableMap<Integer, List<Person>> map = (NavigableMap<Integer, List<Person>>) personsByKey;
		NavigableMap<Integer, List<Person>> mapRes = map.subMap(keyFrom, true, keyTo, true);
		Collection<List<Person>> collection = mapRes.values();
		List<Person> res = new LinkedList<>(); 
		for(List<Person> list : collection){
			res.addAll(list);
		}
		return res;
	}

	@Override
	public Iterable<Person> getPersonsByAddress(Address address) {
		
		return personsAddress.get(address);
	}

	@Override
	public Iterable<Person> getPersonsByBirthYear(int fromYear, int toYear) {
		return getSubPersons(fromYear,toYear,personsYear);
	}	

	@Override
	public Iterable<Person> getPersons(Predicate<Person> predicate) {
		HashSet<Person>	res = new HashSet<>();
		for(Entry<Integer,Person> entry : persons.entrySet()){
			Person person = entry.getValue();
			if(predicate.test(person))
				res.add(person);
		}
		return res;
	}

	@Override
	public boolean updateSalary(int id, int newSalary) {
		Person person = persons.get(id);
		Employee employee = new Employee(0, null, 0, null, null, null);
		try{
			employee = (Employee) person; 
			this.removePerson(id);
			employee.setSalary(newSalary);
			this.addPerson(employee);
			return true;
		}catch (Throwable e){
			return false;
		}
	}

	@Override
	public boolean updateAddress(int id, Address newAddress) {
		Person person = persons.get(id);
		this.removePerson(id);
		person.setAddress(newAddress);;
		this.addPerson(person);
		return true;
	}

	@Override
	public void save() throws FileNotFoundException, IOException {
		ObjectOutputStream output = new ObjectOutputStream
				(new FileOutputStream("persons.txt"));
		for(Entry<Integer,Person> entry : persons.entrySet()){
			output.writeObject(entry.getValue());
		}
		output.close();
	}

	@Override
	public void restore() throws FileNotFoundException, IOException, ClassNotFoundException {
		ArrayList<Person> personsArray = new ArrayList<>();
		try(ObjectInputStream input = new ObjectInputStream
				(new FileInputStream("persons.txt"))){
			while(true){
				Person person = (Person) input.readObject();
				personsArray.add(person);
			}
		}catch(EOFException e){
			
		}
		for(Person person : personsArray){
			this.addPerson(person);
		}
		
	}

}

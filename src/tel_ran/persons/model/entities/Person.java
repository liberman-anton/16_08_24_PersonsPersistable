package tel_ran.persons.model.entities;

import java.io.Serializable;

public abstract class Person implements Serializable {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())//два объекта принадлежат одному классу, сравниваем ссылки. сингл тон у каждого класса только один обьект
			return false;
		Person other = (Person) obj;
		if (id != other.id)
			return false;
		return true;
	}

	int id;
	String name;
	int birthYear;
	Address address;
	
	abstract public String toString();

	public Person(int id, String name, int birthYear, Address address) {
		super();
		this.id = id;
		this.name = name;
		this.birthYear = birthYear;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public int getBirthYear() {
		return birthYear;
	}
	
	
}

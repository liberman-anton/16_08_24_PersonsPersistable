package tel_ran.persons.model.entities;

public class Child extends Person {

	String garten;
	
	public Child(int id, String name, int birthYear, Address address, String garten) {
		super(id, name, birthYear, address);
		this.garten = garten;
	}

	@Override
	public String toString() {
		return "Child [garten=" + garten + ", id=" + id + ", name=" + name + ", birthYear=" + birthYear + ", address="
				+ address + "]";
	}

	public String getGarten() {
		return garten;
	}

	public void setGarten(String garten) {
		this.garten = garten;
	}

	

}

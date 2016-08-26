package tel_ran.persons.model.entities;

import java.io.Serializable;

public class Address implements Serializable {
	String city;
	String street;
	int bld;
	
	public Address(String city, String street, int bld) {
		super();
		this.city = city;
		this.street = street;
		this.bld = bld;
	}
	public String getCity() {
		return city;
	}
	public String getStreet() {
		return street;
	}
	public int getBld() {
		return bld;
	}
	@Override
	public String toString() {
		return "Address [city=" + city + ", street=" + street + ", bld=" + bld + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bld;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (bld != other.bld)
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		return true;
	}
	
	
}

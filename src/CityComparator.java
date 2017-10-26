import java.util.Comparator;

public class CityComparator implements Comparator<Customer> {
	SortOrder sortOrder;
	
	public CityComparator(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public int compare(Customer c1, Customer c2) {
		String city1 = c1.getCity();
		String city2 = c2.getCity();
		
		if(sortOrder == SortOrder.ascending)return city1.compareTo(city2);
		else return city2.compareTo(city1);
	}
}


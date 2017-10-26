import java.util.Comparator;

public class CompanyComparator implements Comparator<Customer> {
	SortOrder sortOrder;
	
	public CompanyComparator(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public int compare(Customer c1, Customer c2) {
		String company1 = c1.getCompany();
		String company2 = c2.getCompany();
		
		if(sortOrder == SortOrder.ascending)return company1.compareTo(company2);
		else return company2.compareTo(company1);
	}
}


package hpx.android.perfcounters;

import java.util.ArrayList;
import java.util.List;

public class Node {
	public int number;
	public List<Locality> localities;
	
	public Node() {
		localities = new ArrayList<Locality>();
		fakeLocalityList();
	}
	
	
	public void fakeLocalityList() {
		Locality loc;
		for(int i = 0; i < 6; i++) {
			loc = new Locality();
			localities.add(loc);
		}
	}
	

}

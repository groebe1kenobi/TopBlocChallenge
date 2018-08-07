package topbloc;

import java.util.ArrayList;

public class JSONData {
	private final String id;
	private final Double[] numSetOne;;
	private final Double[] numSetTwo;
	private final String[] wordSetOne;
	
	public JSONData(String id, Double[] numSetOne, Double[] numSetTwo, String[] wordSetOne) {
		this.id = id;
		this.numSetOne = numSetOne;
		this.numSetTwo = numSetTwo;
		this.wordSetOne = wordSetOne;
	}

}

package fmi.project.food.analyzer.food.objects;

import java.io.Serializable;
import java.util.List;

public class Report implements Serializable {
	private static final long serialVersionUID = 1L;
	private String sr;
	private String groups;
	private String subset;
	private int end;
	private int start;
	private int total;
	private List<FoodInfo> foods;

	@Override
	public String toString() {
		return "groups=" + groups + " foods: " + foods;
	}

	public String getSr() {
		return sr;
	}

	public String getGroups() {
		return groups;
	}

	public String getSubset() {
		return subset;
	}

	public int getEnd() {
		return end;
	}

	public int getStart() {
		return start;
	}

	public int getTotal() {
		return total;
	}

	public List<FoodInfo> getFoods() {
		return foods;
	}
}

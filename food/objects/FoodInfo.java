package fmi.project.food.analyzer.food.objects;

import java.io.Serializable;
import java.util.List;

public class FoodInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ndbno;
	private String name;
	private double weight;
	private String measure;
	private List<Nutrient> nutrients;

	@Override
	public String toString() {
		return "FoodInfo: ndbno=" + ndbno + ", name=" + name + ", weight=" + weight + ", measure=" + measure
				+ ", nutrients=" + nutrients ;
	}

	public String getNdbno() {
		return ndbno;
	}

	public String getName() {
		return name;
	}

	public double getWeight() {
		return weight;
	}

	public String getMeasure() {
		return measure;
	}

	public List<Nutrient> getNutrients() {
		return nutrients;
	}

	
}

package fmi.project.food.analyzer.food.objects;

import java.io.Serializable;

public class Nutrient implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nutrient_id;
	private String nutrient;
	private String unit;
	private String value;
	private double gm;

	@Override
	public String toString() {
		return "Nutrient:  nutrient=" + nutrient + ", unit=" + unit + ", value=" + value + ", gm=" + gm + "\n";
	}

	public String getNutrient_id() {
		return nutrient_id;
	}

	public String getNutrient() {
		return nutrient;
	}

	public String getUnit() {
		return unit;
	}

	public String getValue() {
		return value;
	}

	public double getGm() {
		return gm;
	}
}

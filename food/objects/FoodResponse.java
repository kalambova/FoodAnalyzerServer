package fmi.project.food.analyzer.food.objects;

import java.io.Serializable;

public class FoodResponse implements Serializable {
	static final long serialVersionUID = 1L;
	private Food list;

	@Override
	public String toString() {
		return "Food:" + list ;
	}

	public Food getList() {
		return list;
	}

}

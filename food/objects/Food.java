package fmi.project.food.analyzer.food.objects;

import java.io.Serializable;
import java.util.List;

public class Food implements Serializable {

	private static final long serialVersionUID = 1L;
	private String q;
	private String sr;
	private String ds;
	private int start;
	private int end;
	private int total;
	private String group;
	private String sort;
	private List<Item> item;

	@Override
	public String toString() {
		return "Food: q=" + q + ", group=" + group  + "\n" + "items=" + item;
	}

	public String getQ() {
		return q;
	}

	public String getSr() {
		return sr;
	}

	public String getDs() {
		return ds;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public int getTotal() {
		return total;
	}

	public String getGroup() {
		return group;
	}

	public String getSort() {
		return sort;
	}

	public List<Item> getItem() {
		return item;
	}

}

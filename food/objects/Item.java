package fmi.project.food.analyzer.food.objects;

import java.io.Serializable;

public class Item implements Serializable {

	private static final long serialVersionUID = 1L;
	private int offset;
	private String group;
	private String name;
	private String ndbno;
	private String ds;
	private String manu;

	@Override
	public String toString() {
		return "Item:" + "group=" + group + ", name=" + name + ", ndbno=" + ndbno + ", manu=" + manu + "\n";
	}

	public int getOffset() {
		return offset;
	}

	public String getGroup() {
		return group;
	}

	public String getName() {
		return name;
	}

	public String getNdbno() {
		return ndbno;
	}

	public String getDs() {
		return ds;
	}

	public String getManu() {
		return manu;
	}
	public String getUPC() {
		String[] splitted = name.split("UPC: ");
		if (splitted.length > 1) {
			return splitted[1];
		}
		return "";

	}

}

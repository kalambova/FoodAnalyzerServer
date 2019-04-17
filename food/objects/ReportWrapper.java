package fmi.project.food.analyzer.food.objects;

import java.io.Serializable;

public class ReportWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	private Report report;

	@Override
	public String toString() {
		return "Report: " + report ;
	}

	public Report getReport() {
		return report;
	}

}

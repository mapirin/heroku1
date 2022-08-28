package com.report.web;

import java.io.Serializable;

public class ReportCreateForm implements Serializable {
	public static long serialVersionUID = 1L;

	private String remarks;

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}

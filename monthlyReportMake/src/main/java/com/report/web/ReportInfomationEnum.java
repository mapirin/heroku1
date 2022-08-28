package com.report.web;

public enum ReportInfomationEnum {
	INFOMATION_EXCEL("Excel"), INFOMATION_VBA("VBA"), INFOMATION_LINUX("tera term"), INFOMATION_LOG("WinSCP"),
	INFOMATION_JAVA("Java生成ツール");

	private String InformationName;

	private ReportInfomationEnum(String InformationName) {
		this.InformationName = InformationName;
	}

	public String getInformationName() {
		return InformationName;
	}

}

package com.report.web;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.report.service.ReportService;

@Controller
@SessionAttributes("reportForm")
public class MonthlyReportController {

	@Autowired
	private ReportService reportService;

	@ModelAttribute("reportForm")
	public ReportCreateForm setForm() {
		return new ReportCreateForm();
	}

	// メニュー画面
	@RequestMapping("/work-report")
	public String reportMenu() {
		return "web/monthlyReportMenu";
	}

	// メニュー→入力画
	@RequestMapping(value = "/work-report-input", params = "create")
	public String reportMenuToReportInput() {
		return "web/monthlyReportInput";
	}

	// 作業実績報告書を操作
	@RequestMapping(value = "/work-report-input", params = "file")
	public String reportMenuToFileSetup() {
		return "web/monthlyReportMenu";
	}

	// 入力画面→メニュー
	@RequestMapping(value = "/work-report-conf", params = "back")
	public String reportInputToReportMenu() {
		return "web/monthlyReportMenu";
	}

	// 入力画面→確認画面
	@RequestMapping(value = "/work-report-conf", params = "conf")
	public String reportInputToReportConf(@ModelAttribute("reportForm") ReportCreateForm reportForm) {
		return "web/monthlyReportConf";
	}

	// 確認画面→入力画面
	@RequestMapping(value = "/work-report-end", params = "back")
	public String reportConfToReportInput() {
		return "web/monthlyReportInput";
	}

	// 確認画面→完了画面（お試し）
	@RequestMapping(value = "/work-report-end", params = "sendTest")
	public String reportConfToReportTestEnd(@ModelAttribute("reportForm") ReportCreateForm reportForm) {
		reportService.memoOutput(reportForm);
		return "redirect:/work-report-end?finish";
	}

	// 確認画面→完了画面
	@RequestMapping(value = "/work-report-end", params = "send")
	public String reportConfToReportEnd(@ModelAttribute("reportForm") ReportCreateForm reportForm) {
		
		try {
			reportService.sendMail(reportForm);
		}catch(MessagingException e) {
			e.printStackTrace();
		}
		
		return "redirect:/work-report-end?finish";
	}

	// 完了画面
	@RequestMapping(value = "/work-report-end", params = "finish")
	public String reportEnd() {
		return "web/monthlyReportEnd";
	}

	// 完了画面→メニュー
	@RequestMapping(value = "/work-report-menu", params = "create")
	public String reportEndToReportMenu() {
		return "web/monthlyReportMenu";
	}

}

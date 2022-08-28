package com.report.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.report.web.ReportCreateForm;

@Service
public class ReportService {

	@Autowired
	JavaMailSender javaMailSender;

	// ファイル名を修正
	public void renameFile() throws IOException {
		String year = String.valueOf(LocalDate.now().getYear());
		String month = String.format("%02d", LocalDate.now().getMonthValue());

		File readFileName = new File("C:\\Users\\neo-matrix506\\Downloads\\作業実績報告書_林政裕_" + year + month + ".xls");
		File writeFileName = new File("C:\\work\\作業実績報告書_" + year + month + ".xls");

		@SuppressWarnings("resource")
		FileChannel readCh = new FileInputStream(readFileName).getChannel();
		@SuppressWarnings("resource")
		FileChannel writeCh = new FileOutputStream(writeFileName).getChannel();

		readCh.transferTo(0, readCh.size(), writeCh);
	}

	// 出力情報用
	protected static Context outputParams(ReportCreateForm reportForm) {
		// メールテンプレ用パラメータ
		Map<String, Object> variables = new HashMap<>();

		Integer month = LocalDate.now().getMonthValue() - 1;
		// セット
		variables.put("thisMonth", month);
		variables.put("remarks", "");
		// 実行してテキストを取得
		Context context = new Context();
		context.setVariables(variables);

		return context;
	}

	//メール送信
	public void sendMail(ReportCreateForm reportForm) throws MessagingException {
		String year = String.valueOf(LocalDate.now().getYear());
		String month = String.format("%02d", LocalDate.now().getMonthValue()-1);

		String fileName = "作業実績報告書_" + year + month + ".xls";
		FileSystemResource fileResource = new FileSystemResource("C:\\work\\" + fileName);

		MimeMessage mimeMsg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, true);

		//配列に格納してCc送信作戦
		String[] ccList=new String[2];
//		ccList[0]="hayashi.masahiro@n-matrix.com";
//		ccList[1]="<hayashi.masahiro@n-matrix.com>";
		ccList[0]="usmovers20@gmail.com";
		ccList[1]="812matahiro29@gmail.com";
		
		helper.setFrom("matahiro884@gmail.com");
		helper.setTo("matahiro884@gmail.com");
		helper.setCc(ccList);
//		helper.setTo("somu-jinji@n-matrix.com");
//		helper.setCc("saito@n-matrix.com");
//		helper.setCc("goto@n-matrix.com");
		helper.setSubject("作業実績報告書の提出");
		helper.addAttachment(fileName, fileResource);

		// テンプレートエンジン作成
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		// テンプレートエンジンの種類、エンコード
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		templateResolver.setCharacterEncoding("UTF-8");
		// 上記で設定したテンプレエンジン情報を反映
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver);

		// テンプレのファイル名とパラメータを取得（一項：ファイル、二項：パラメータ）
		String text = engine.process("/templates/sample.txt", ReportService.outputParams(reportForm));
		helper.setText(text);

		this.javaMailSender.send(mimeMsg);
	}
	
	// メモ出力
	public void memoOutput(ReportCreateForm reportForm) {
		// 入力された情報を上記テキストメール形式で受け取った後、
		// メモを起動して書き込む bufferdWriter FileOutputStream
		// テンプレートエンジン作成
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		// テンプレートエンジンの種類、エンコード
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		templateResolver.setCharacterEncoding("UTF-8");
		// 上記で設定したテンプレエンジン情報を反映
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver);

		// テンプレのファイル名とパラメータを取得（一項：ファイル、二項：パラメータ）
		String text = engine.process("/templates/sample.txt", ReportService.outputParams(reportForm));

		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter("C:\\temp\\MonthlyDemo\\demo.txt"));
			bw.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

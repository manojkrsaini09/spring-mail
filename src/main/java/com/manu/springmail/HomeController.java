package com.manu.springmail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message.RecipientType;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.manu.springmail.EmailService.MailService;
import com.manu.springmail.mailutil.MailAddress;
import com.manu.springmail.mailutil.MailAttachment;
import com.manu.springmail.mailutil.MailObject;
import com.manu.springmail.mailutil.Recipient;

@RestController
public class HomeController {

	/*@Autowired
	private JavaMailSender sender;*/

	@Autowired
	private MailService mailService;

	public static final String mailID1 = "";
	public static final String mailID2 = "";
	public static final String mailID3 = "";
	
	@RequestMapping(value = "/simpleTextMail" , method = RequestMethod.GET)
	public void sendEmail() throws Exception{

		// initialized new mail object
		MailObject email = new MailObject();
		
		// initialized recipient list
		List<Recipient> recipients = new ArrayList<Recipient>();
		
		// initialized first recipient 
		Recipient recipient1 = new Recipient();
		
		// Initialized mail address
		MailAddress mailAddress = new MailAddress("User", mailID1);
		
		// set mail address in a recipient object
		recipient1.setMailAddress(mailAddress);
		
		// set recipient type
		recipient1.setRecipientType(RecipientType.TO);
		
		// add recipient in recipients list
		recipients.add(recipient1);

		// Created second recipient
		Recipient recipient2 = new Recipient();
		mailAddress = new MailAddress("User", mailID2);
		recipient2.setMailAddress(mailAddress);
		recipient2.setRecipientType(RecipientType.BCC);
		recipients.add(recipient2);

		//Created second recipient
		Recipient recipient3 = new Recipient();
		mailAddress = new MailAddress("User", mailID3);
		recipient3.setMailAddress(mailAddress);
		recipient3.setRecipientType(RecipientType.CC);
		recipients.add(recipient3);

		// set recipient list in email object
		email.setRecipients(recipients);
		
		// set subject
		email.setSubject("Spring mail test.");
		
		// set text message
		email.setTextMessage("Hi this is a test mail");
		
		// Call service method to send mail
		boolean status =mailService.sendmail(email);
	}

	@RequestMapping(value = "/pdfAttachmentMail" , method = RequestMethod.GET)
	public void sendPdfInmail() {
		
		// initialized output stream
		ByteArrayOutputStream  outputStream = new ByteArrayOutputStream();
		
		// write pdf content to output stream
		this.generatePdf(outputStream);
		
		// get byte array
		byte[] bytes = outputStream.toByteArray();

        // Initialized mail object
		MailObject email = new MailObject();
		List<Recipient> recipients = new ArrayList<Recipient>();
		Recipient recipient1 = new Recipient();
		MailAddress mailAddress = new MailAddress("User", mailID1);
		recipient1.setMailAddress(mailAddress);
		recipient1.setRecipientType(RecipientType.TO);
		recipients.add(recipient1);

		Recipient recipient2 = new Recipient();
		mailAddress = new MailAddress("User",mailID2);
		recipient2.setMailAddress(mailAddress);
		recipient2.setRecipientType(RecipientType.BCC);
		recipients.add(recipient2);

		Recipient recipient3 = new Recipient();
		mailAddress = new MailAddress("User", mailID3);
		recipient3.setMailAddress(mailAddress);
		recipient3.setRecipientType(RecipientType.CC);
		recipients.add(recipient3);

		email.setRecipients(recipients);
		email.setSubject("Spring mail test.");
		//email.setTextMessage("Hi this is a test mail");

		// set html content
		email.setHtmlBody("<html><body> <h1>Hi,</h1> this is html content<body></html>");

		// initialized attachment object 
		MailAttachment attachment = new MailAttachment();
		
		// set file content and file name 
		attachment.setFile(ArrayUtils.toObject(bytes));
		attachment.setFilename("test.pdf");

		// created second attachment
		MailAttachment attachment2 = new MailAttachment();
		attachment2.setFile(ArrayUtils.toObject(bytes));
		attachment2.setFilename("test2.pdf");

		// added attachments in attacchment list
		List<MailAttachment> mailAttachments = new ArrayList<MailAttachment>();
		mailAttachments.add(attachment);
		mailAttachments.add(attachment2);
		
		// set attachment list in attachment array
		email.setAttachments(mailAttachments);
		
		// send mail
		boolean status = mailService.sendmail(email);

	}

	public void generatePdf(OutputStream outputStream) {
		// Initialize velocity engine
		VelocityEngine ve = new VelocityEngine();

		// set class path to locate template
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");                             
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();

		// get vm template
		Template t = ve.getTemplate("templates/VMDemo.vm");

		//Put values in context to evaluate in template
		VelocityContext ctx = new VelocityContext();
		ctx.put("name", "Manoj");
		ctx.put("lastName", "Saini");

		// write output to writer
		Writer writer = new StringWriter();
		t.merge(ctx, writer);



		// Create new document
		Document document = new Document();
		PdfWriter pdfWriter;

		// set content type to download file

		try {
			//pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
			
			//initialized pdf writer instance to write output stream
			pdfWriter = PdfWriter.getInstance(document,outputStream);

			document.open();
			// File file = new File(new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8));
			//write html content to pdf file and then output stream
			XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document,new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8))); 
			document.close();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

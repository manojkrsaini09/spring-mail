package com.manu.springmail.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.manu.springmail.mailutil.MailAttachment;
import com.manu.springmail.mailutil.MailObject;
import com.manu.springmail.mailutil.Recipient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

@Service
public class SpringMailServiceImpl implements MailService{

	private static MimetypesFileTypeMap mediaTypes = new MimetypesFileTypeMap();
	
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public boolean sendmail(MailObject email) {
		// MimeMessagePreparator preparator = getMessagePreparator(email);
		MimeMessage mimeMessage;
		try {
			
			// get required mime message object from mail object
			mimeMessage = this.getMessageFromMailObject(email);
			mailSender.send(mimeMessage);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	private MimeMessage getMessageFromMailObject(MailObject email) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = null;
		
		// check attachment size and initialized helper with appropriate constructor
		if(email.getAttachments().size()>0) {
			 helper = new MimeMessageHelper(message,true);
		}else {
			 helper = new MimeMessageHelper(message);
		}
		
		// get different type of recipient from mail object
		List<String> toUser = new ArrayList<>();
		List<String> ccUser = new ArrayList<>();
		List<String> bccUser = new ArrayList<>();
		for(Recipient recipient : email.getRecipients()) {
			if(RecipientType.TO.toString().equalsIgnoreCase(recipient.getRecipientType().toString())) {
				toUser.add(recipient.getMailAddress().getEmail());
			}
			if(RecipientType.CC.toString().equalsIgnoreCase(recipient.getRecipientType().toString())) {
				ccUser.add(recipient.getMailAddress().getEmail());
			}
			if(RecipientType.BCC.toString().equalsIgnoreCase(recipient.getRecipientType().toString())) {
				bccUser.add(recipient.getMailAddress().getEmail());
			}
		}
		
		// set recipient list in mail helper according to recipient type
		helper.setTo(toUser.toArray(new String[toUser.size()]));
		helper.setBcc(bccUser.toArray(new String[bccUser.size()]));
		helper.setCc(ccUser.toArray(new String[ccUser.size()]));
		
		// check presence of html body and text message in mail object
		if(!StringUtils.isEmpty(email.getHtmlBody())) {
			helper.setText(email.getHtmlBody(),true);
		}else if(!StringUtils.isEmpty(email.getTextMessage())) {
			helper.setText(email.getTextMessage());
		}
		
		// set email subject
		helper.setSubject(email.getSubject());
		byte[] data = null;
		DataSource source = null;
		
		// create data source from attachments and set in mail helper
		for(MailAttachment attachment:email.getAttachments()) {
			 data = new byte[attachment.getFile().length];
			for(int i=0;i<attachment.getFile().length;i++)	{
				data[i]=attachment.getFile()[i].byteValue();
			}
			 source = new ByteArrayDataSource(data,mediaTypes.getContentType(attachment.getFilename()));
			/* if(attachment.isInline()) {
				 helper.addInline("id1414", source);
			 }else {*/
				 helper.addAttachment(attachment.getFilename(), source);
			// }
			
		}
		return message;
	}

}

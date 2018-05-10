package com.manu.springmail.EmailService;


import com.manu.springmail.mailutil.MailObject;


public interface MailService {

	public boolean sendmail(MailObject email);
}

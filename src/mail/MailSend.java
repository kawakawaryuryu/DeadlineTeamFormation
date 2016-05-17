package mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.PasswordAuthentication;

import config.Configuration;

public class MailSend {

	private Properties properties;
	private Session session;
	
	public MailSend() {
		properties = new Properties();
		properties.setProperty("mail.smtp.host", Configuration.MAIL_HOST);
		properties.setProperty("mail.smtp.auth", "true"); 
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.port", "587");
		properties.setProperty("mail.debug", "true");
		session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Configuration.MAIL_USER, Configuration.MAIL_PASS);
			}
		});
	}


	public void send(String subject, String msg) {
		MimeMessage message = new MimeMessage(session);
		
		try {
			InternetAddress to[] = InternetAddress.parse(Configuration.MAIL_TO);
			
			message.setRecipients(MimeMessage.RecipientType.TO, to);
			message.setFrom(new InternetAddress(Configuration.MAIL_FROM));
			message.setSubject(subject, "iso-2022-jp");
			message.setText(msg, "iso-2022-jp");
			message.saveChanges();
			
			Transport.send(message);
		} catch(MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
}

package common.util;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import org.apache.log4j.Logger;

public class MailBean
{
	static Logger log = Logger.getLogger(MailBean.class.toString());
	static Properties email = new Properties();
	static boolean blnFlag = false;

	public static void mailingComponent(String fromAddress, String toAddrArray, String subject, String msgBody, String smtpHost, String smtpPort)
			throws MessagingException
	{
	    log.info("Inside MailBean : mailingComponent ");
		Session session;
		MimeMessage mimeMessage = null;
		final Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpHost);
		props.setProperty("mail.smtp.port", smtpPort);
		session = Session.getDefaultInstance(props, null);
		final InternetAddress[] toAddress = { new InternetAddress(toAddrArray) };
		// create a message
		mimeMessage = new MimeMessage(session);
		mimeMessage.setFrom(new InternetAddress(fromAddress));
		if (toAddrArray != null)
		{
			mimeMessage.setRecipients(Message.RecipientType.TO, toAddress);
		}
		mimeMessage.setSubject(subject);
		// create and fill the first message part
		final MimeBodyPart bodyText = new MimeBodyPart();
		// bodyText.setText(msgBody);
		bodyText.setContent(msgBody, "text/html");
		// create the Multipart and add its parts to it
		final Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(bodyText);
		// add the Multipart to the message
		mimeMessage.setContent(multipart);
		// set the Date: header
		mimeMessage.setSentDate(new Date());
		// send the message
		Transport.send(mimeMessage);
	}
}

package common.email;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains method for sending Email to single recipient and multiple
 * recipients.
 * 
 * @author manjunatha_gh
 */
public class EmailComponent {

	/**
	 * default constructor for EmailComponent.
	 */
	private EmailComponent() {
	}

	/**
	 * Getting the logger Instance.
	 */

	private static final Logger LOG = LoggerFactory.getLogger(EmailComponent.class);

	/**
	 * The method for sending emails.
	 * 
	 * @param fromAddress
	 *            of the user.
	 * @param toAddrArray
	 *            as multiple recepients.
	 * @param subject
	 *            of the mail.
	 * @param msgBody
	 *            as email content are the request parameters.
	 * @param smtpHost
	 *            smatp server IP addresss or host name.
	 * @param smtpPort
	 *            server port number.
	 * @throws MessagingException
	 *             for any exceptions occured while sending email.
	 */
	public static void multipleUsersmailingComponent(String fromAddress, String[] toAddrArray, String subject, String msgBody, String smtpHost,
			String smtpPort) throws MessagingException {

		Session session;
		MimeMessage mimeMessage = null;

		final Properties props = System.getProperties();

		/*
		 * ClassLoader loader = ClassLoader.getSystemClassLoader(); URL url =
		 * loader.getResource("scansee.properties"); Properties properties = new
		 * Properties(); properties.load( url.openStream()); String
		 * host=properties.getProperty("mail.smtp.host"); String
		 * post=properties.getProperty("mail.smtp.port");
		 */

		props.put("mail.smtp.host", smtpHost);
		props.setProperty("mail.smtp.port", smtpPort);

		session = Session.getDefaultInstance(props, null);

		// InternetAddress[] toAddress2 = { new InternetAddress(toAddrArray) };

		mimeMessage = new MimeMessage(session);
		mimeMessage.setFrom(new InternetAddress(fromAddress));
		final InternetAddress[] toAddress2 = new InternetAddress[toAddrArray.length];

		for (int i = 0; i < toAddrArray.length; i++) {
			toAddress2[i] = new InternetAddress(toAddrArray[i]);
		}

		mimeMessage.setRecipients(Message.RecipientType.TO, toAddress2);
		mimeMessage.addRecipients(Message.RecipientType.BCC, toAddress2);

		// create a message

		/*
		 * if (toAddrArray != null) {
		 * mimeMessage.addRecipient(Message.RecipientType.TO, new
		 * InternetAddress(toAddrArray) );
		 * mimeMessage.setRecipients(Message.RecipientType.TO, toAddress); }
		 */

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

		// Below code for fixing email sending issue in public server
		Thread.currentThread().setContextClassLoader(mimeMessage.getClass().getClassLoader());

		// send the message
		Transport.send(mimeMessage);

	}

	/**
	 * This method will send the mail to the specified address with attachment.
	 * 
	 * @param strTo
	 *            the to Address to which the mail is to be sent
	 * @param strCc
	 *            array of recipients for cc.
	 * @param strFrom
	 *            the from Address
	 * @param strSubject
	 *            the Subject Line of the mail
	 * @param strBody
	 *            the message to be sent
	 * @param fileAttachment
	 *            the file name for the file to be attached.
	 * @param smtpHost
	 *            smatp server IP addresss or host name.
	 * @param smtpPort
	 *            server port number.
	 * @return boolean returns true if its success, else false
	 */
	public static boolean sendMailWithAttachment(String[] strTo, String[] strCc, String strFrom, String strSubject, String strBody,
			String fileAttachment, String smtpHost, String smtpPort) {
		LOG.info("MailBean:sendMailWithAttachment");
		final Properties props = System.getProperties();
		try {
			props.put("mail.smtp.host", smtpHost);
			props.setProperty("mail.smtp.port", smtpPort);
			final Session session = Session.getDefaultInstance(props, null);
			final Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(strFrom));
			for (int i = 0; i < strTo.length; i++) {
				InternetAddress[] address = InternetAddress.parse(strTo[i]);
				msg.addRecipients(Message.RecipientType.TO, address);
			}
			for (int i = 0; i < strCc.length; i++) {
				InternetAddress[] address = InternetAddress.parse(strCc[i]);
				msg.addRecipients(Message.RecipientType.CC, address);
			}
			msg.setSubject(strSubject);
			final MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent(strBody, "text/plain");
			final MimeBodyPart attachFilePart = new MimeBodyPart();
			final FileDataSource fds = new FileDataSource(fileAttachment);
			attachFilePart.setDataHandler(new DataHandler(fds));
			attachFilePart.setFileName(fds.getName());
			final Multipart mp = new MimeMultipart();
			mp.addBodyPart(textPart);
			mp.addBodyPart(attachFilePart);
			msg.setContent(mp);

			// Below code for fixing email sending issue in public server
			Thread.currentThread().setContextClassLoader(msg.getClass().getClassLoader());
			Transport.send(msg);
		} catch (MessagingException mex) {
			LOG.error("Exception occured in sending mail with attachment" + mex);
			return false;
		}
		return true;
	}

	/**
	 * This method will send the mail to the specified address with attachment.
	 * 
	 * @param strTo
	 *            the to Address to which the mail is to be sent
	 * @param strFrom
	 *            the from Address
	 * @param strSubject
	 *            the Subject Line of the mail
	 * @param strBody
	 *            the message to be sent
	 * @param fileAttachment
	 *            the file name for the file to be attached
	 * @param smtpHost
	 *            smatp server IP addresss or host name.
	 * @param smtpPort
	 *            server port number.
	 * @return boolean returns true if its success, else false
	 */
	public static boolean sendMailWithAttachmentWithoutCC(String strTo, String strFrom, String strSubject, String strBody, String fileAttachment,
			String smtpHost, String smtpPort) {
		LOG.info("Inside EmailComponent:sendMailWithAttachmentWithoutCC");
		final Properties props = System.getProperties();
		try {
			props.put("mail.smtp.host", smtpHost);
			props.setProperty("mail.smtp.port", smtpPort);
			final Session session = Session.getDefaultInstance(props, null);
			final Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(strFrom));
			if (strTo != null) {
				final InternetAddress[] address = InternetAddress.parse(strTo);
				msg.addRecipients(Message.RecipientType.TO, address);
			}
			msg.setSubject(strSubject);
			final MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent(strBody, "text/html");
			final MimeBodyPart attachFilePart = new MimeBodyPart();
			final FileDataSource fds = new FileDataSource(fileAttachment);
			attachFilePart.setDataHandler(new DataHandler(fds));
			attachFilePart.setFileName(fds.getName());
			final Multipart mp = new MimeMultipart();
			mp.addBodyPart(textPart);
			mp.addBodyPart(attachFilePart);
			msg.setContent(mp);
			// Below code for fixing email sending issue in public server
			Thread.currentThread().setContextClassLoader(msg.getClass().getClassLoader());
			Transport.send(msg);
		} catch (MessagingException mex) {
			LOG.error("Inside EmailComponent:sendMailWithAttachmentWithoutCC : " + mex);
			return false;
		}
		return true;
	}

	/**
	 * This method will send the mail to the specified address with out
	 * attachment.
	 * 
	 * @param fromAddress
	 *            the from Address.
	 * @param toAddrArray
	 *            the to Address to which the mail is to be sent.
	 * @param subject
	 *            the Subject Line of the mail
	 * @param msgBody
	 *            the message to be sent
	 * @param smtpHost
	 *            smtp server IP addresss or host name.
	 * @param smtpPort
	 *            server port number.
	 * @throws MessagingException
	 *             exception while sending an email.
	 */
	public static void sendMailWithOutAttachmentWithoutCC(String fromAddress, String toAddrArray, String subject, String msgBody, String smtpHost,
			String smtpPort) throws MessagingException {
		LOG.info("Inside MailBean : sendMailWithOutAttachmentWithoutCC ");
		Session session;
		MimeMessage mimeMessage = null;
		final Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpHost);
		props.setProperty("mail.smtp.port", smtpPort);
		session = Session.getDefaultInstance(props, null);
		final InternetAddress[] toAddress = { new InternetAddress(toAddrArray) };
		mimeMessage = new MimeMessage(session);
		mimeMessage.setFrom(new InternetAddress(fromAddress));
		if (toAddrArray != null) {
			mimeMessage.setRecipients(Message.RecipientType.TO, toAddress);
		}
		mimeMessage.setSubject(subject);
		final MimeBodyPart bodyText = new MimeBodyPart();
		bodyText.setContent(msgBody, "text/html");
		final Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(bodyText);
		mimeMessage.setContent(multipart);
		mimeMessage.setSentDate(new Date());
		// Below code for fixing email sending issue in public server
		Thread.currentThread().setContextClassLoader(mimeMessage.getClass().getClassLoader());
		Transport.send(mimeMessage);
	}
}

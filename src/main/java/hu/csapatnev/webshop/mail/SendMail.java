package hu.csapatnev.webshop.mail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

import hu.csapatnev.webshop.App;
import hu.csapatnev.webshop.Config;

public class SendMail {
	public static void send(String email, String subject, String message) {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				Properties prop = Config.getInstance().getProperties();

				MailSSLSocketFactory sf;
				try {
					sf = new MailSSLSocketFactory();
					sf.setTrustAllHosts(true);
					prop.put("mail.smtp.ssl.socketFactory", sf);
				} catch (GeneralSecurityException e1) {
					e1.printStackTrace();
				}

				Authenticator auth = new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(prop.getProperty("mail.smtp.user"),
								prop.getProperty("mail.smtp.pass"));
					}
				};
				Session session = Session.getInstance(prop, auth);

				try {
					MimeMessage msg = new MimeMessage(session);
					msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
					msg.addHeader("Content-Encoding","UTF-8");
					msg.addHeader("format", "flowed");
					msg.addHeader("Content-Transfer-Encoding", "8bit");
					msg.setFrom(new InternetAddress(prop.getProperty("mail.smtp.email"),
							prop.getProperty("mail.smtp.emailName")));
					msg.setReplyTo(InternetAddress.parse(prop.getProperty("mail.smtp.email"), false));
					msg.setSubject(subject, "UTF-8");
					msg.setContent(message, "text/html");
					msg.setSentDate(new Date());
					msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
					Transport.send(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
		th.start();
	}

	public static String readTemplate(String template, String body, String head, String foot, String postscript) {
		InputStream inputStream = App.class.getClassLoader().getResourceAsStream(template + ".email");
		String text = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
				.collect(Collectors.joining("\n"));
		Properties prop = Config.getInstance().getProperties();
		text = text.replace("{{url}}", prop.getProperty("site.url"));
		text = text.replace("{{logo}}", prop.getProperty("site.url") + "assets/images/icon.svg");
		text = text.replace("{{sitename}}", prop.getProperty("site.name"));
		text = text.replace("{{body}}", body);
		text = text.replace("{{head}}", head);
		text = text.replace("{{foot}}", foot);
		text = text.replace("{{postscript}}", postscript);
		return text;
	}
	
	public static void sendTemplate(String email, String subject, String template, String body, String head, String foot, String postscript) {
		send(email, subject, readTemplate(template, body, head, foot, postscript));
	}
}

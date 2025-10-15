package kr.or.hamaeyu.utils;

import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

	 private static final String FROM = "hagreen0hy@gmail.com";         // 변경
	  private static final String APP_PASSWORD = "umvj pqte pmjw euqm";  // 변경(앱 비번)

	  public static void sendText(String to, String subject, String body, boolean useSsl) throws MessagingException {
	    Properties p = new Properties();
	    p.put("mail.smtp.auth", "true");
	    p.put("mail.smtp.host", "smtp.gmail.com");
	    if (useSsl) {            // SSL(465)
	      p.put("mail.smtp.port", "465");
	      p.put("mail.smtp.ssl.enable", "true");
	    } else {                 // TLS(587) - 권장
	      p.put("mail.smtp.port", "587");
	      p.put("mail.smtp.starttls.enable", "true");
	    }

	    Session session = Session.getInstance(p, new Authenticator() {
	      protected PasswordAuthentication getPasswordAuthentication() {
	        return new PasswordAuthentication(FROM, APP_PASSWORD);
	      }
	    });

	    Message msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(FROM));
	    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
	    msg.setSubject(subject);
	    msg.setText(body);
	    Transport.send(msg);
	  }

}

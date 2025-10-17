package kr.or.hamaeyu.utils;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

    private static final String FROM = "hagreen0hy@gmail.com";         
    private static final String APP_PASSWORD = "umvj pqte pmjw euqm";  

    // 동기 전송 (기존)
    public static void sendText(String to, String subject, String body, boolean useSsl) throws MessagingException {
        Properties p = new Properties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.host", "smtp.gmail.com");
        if (useSsl) {
            p.put("mail.smtp.port", "465");
            p.put("mail.smtp.ssl.enable", "true");
        } else {
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

    // ✅ 비동기 전송
    public static void sendTextAsync(String to, String subject, String body, boolean useSsl) {
        CompletableFuture.runAsync(() -> {
            try {
                sendText(to, subject, body, useSsl);
                System.out.println("[EmailUtil] Async mail sent to " + to);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

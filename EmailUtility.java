import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailUtility {
    private static final String SMTP_HOST = "smtp.gmail.com"; // Change to your SMTP server
    private static final String SMTP_PORT = "587"; // TLS port
    private static final String EMAIL_USERNAME = "your-email@gmail.com"; // Replace with your email
    private static final String EMAIL_PASSWORD = "your-app-password"; // Replace with your app password

    public static void sendApprovalNotification(String recipientEmail, String recipientName, boolean isApproved) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("GuidancePro Account " + (isApproved ? "Approval" : "Rejection") + " Notification");

            String body = "Dear " + recipientName + ",\n\n" +
                         "Your account registration for GuidancePro has been " +
                         (isApproved ? "approved" : "rejected") + ".\n\n" +
                         (isApproved ? "You can now log in to the system using your credentials." :
                                       "Please contact an administrator for more information.") +
                         "\n\nBest regards,\nGuidancePro Administration Team";

            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully to " + recipientEmail);

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

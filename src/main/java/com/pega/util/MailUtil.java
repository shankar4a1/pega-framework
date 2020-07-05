

package com.pega.util;

import com.pega.exceptions.*;
import org.apache.commons.io.*;
import org.jsoup.*;
import org.testng.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class MailUtil {


    public MailUtil() {

    }

    public static String readNewMail(final Host mailHost, final String userName, final String password, final String subjectMatch, final String downloadDirForAttchmnts, final Timeout timeout) {
        String content = "";
        Message message = null;
        Folder emailFolder = null;
        final Store store = connect(mailHost, userName, password);
        try {
            emailFolder = store.getFolder("inbox");
            emailFolder.open(2);
        } catch (MessagingException e3) {
            throw new PegaMailException("Unable to open folder: INBOX in read-write mode");
        }
        try {
            final Flags seen = new Flags(Flags.Flag.SEEN);
            final FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
            message = readMessage(emailFolder, unseenFlagTerm, subjectMatch);
            if (message == null && timeout != null) {
                for (int value = timeout.getValue(), totalTime = 0; message == null && totalTime <= value; message = readMessage(emailFolder, unseenFlagTerm, subjectMatch)) {
                    Reporter.log("Next check will happen after 10 secs...", true);
                    sleep(10);
                    totalTime += 10;
                    if (emailFolder.isOpen()) {
                        emailFolder.close(true);
                    }
                    emailFolder.open(2);
                }
            }
            if (message != null) {
                content = processMessageBody(message, downloadDirForAttchmnts);
                return content;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PegaMailException("Unable to read emails from 'INBOX' folder", e);
        } finally {
            try {
                emailFolder.close(true);
                store.close();
            } catch (MessagingException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static boolean sendEmail(final Host mailHost, final String senderEmailId, final String senderPassword, final String toEmailId, final String subject, final String body) {
        final Properties properties = System.getProperties();
        properties.setProperty("java.net.preferIPv4Stack", "true");
        properties.setProperty("mail.smtp.host", mailHost.getHostName());
        final String port = mailHost.getPort();
        if (port != "") {
            properties.setProperty("mail.smtp.port", port);
        }
        Session session;
        if (senderPassword != null) {
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.starttls.enable", "true");
            final String fromId = senderEmailId;
            final String fromPassword = senderPassword;
            session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromId, fromPassword);
                }
            });
        } else {
            session = Session.getDefaultInstance(properties);
        }
        try {
            final MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmailId));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailId));
            message.setSubject(subject);
            message.setContent(body, "text/plain; charset=UTF-8");
            Transport.send(message);
            Reporter.log("Mail sent successfully to email id: " + toEmailId + "with subject " + subject, true);
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
    }

    private static Store connect(final Host mailHost, final String userName, final String password) {
        final Session emailSession = Session.getDefaultInstance(System.getProperties());
        Store store;
        try {
            store = emailSession.getStore("imaps");
            store.connect(mailHost.getHostName(), userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PegaMailException("Unable to establish connection");
        }
        return store;
    }

    private static Message readMessage(final Folder emailFolder, final FlagTerm flagTerm, final String subjectMatch) throws MessagingException {
        final Message[] messages = emailFolder.search(flagTerm);
        Message message = null;
        final int length = messages.length;
        String subject = null;
        Calendar calendar = null;
        Reporter.log("Subject to match: " + subjectMatch, true);
        Reporter.log("Current unread messages count is " + length, true);
        if (length > 10) {
            for (int i = length; i >= length - 10; --i) {
                message = messages[i - 1];
                subject = message.getSubject();
                calendar = Calendar.getInstance();
                calendar.setTime(message.getReceivedDate());
                Reporter.log("Subject: " + subject, true);
                if (((subject == null && subjectMatch == null) || (subject != null && subjectMatch != null && subject.trim().startsWith(subjectMatch.trim()))) && calendar.get(5) == GlobalConstants.CURRENT_DAY_OF_MONTH) {
                    message.setFlag(Flags.Flag.SEEN, true);
                    return message;
                }
            }
        }
        if (length <= 10) {
            for (int i = length; i >= 1; --i) {
                message = messages[i - 1];
                subject = message.getSubject();
                calendar = Calendar.getInstance();
                calendar.setTime(message.getReceivedDate());
                Reporter.log("Subject on mail: " + subject, true);
                if (((subject == null && subjectMatch == null) || (subject != null && subjectMatch != null && subject.trim().startsWith(subjectMatch.trim()))) && calendar.get(5) == GlobalConstants.CURRENT_DAY_OF_MONTH) {
                    message.setFlag(Flags.Flag.SEEN, true);
                    return message;
                }
            }
        }
        return null;
    }

    private static String processMessageBody(final Message message, final String downloadDir) {
        String mesgContent = null;
        try {
            final Object content = message.getContent();
            if (content instanceof String) {
                return content.toString();
            }
            if (content instanceof Multipart) {
                final Multipart multiPart = (Multipart) content;
                mesgContent = procesMultiPart(multiPart, downloadDir);
            } else if (content instanceof InputStream) {
                final InputStream inStream = (InputStream) content;
                int ch;
                while ((ch = inStream.read()) != -1) {
                    mesgContent = String.valueOf(mesgContent) + (char) ch;
                }
                inStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e2) {
            e2.printStackTrace();
        }
        if (mesgContent == null) {
            try {
                mesgContent = processMessageBodyForRecursiveMultiPart(message);
            } catch (Exception e3) {
                mesgContent = null;
                throw new PegaMailException("Problem in readiing mail content:", e3);
            }
        }
        return mesgContent;
    }

    private static String processMessageBodyForRecursiveMultiPart(final Message message) throws Exception {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            final MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private static String getTextFromMimeMultipart(final MimeMultipart mimeMultipart) throws Exception {
        String result = "";
        for (int count = mimeMultipart.getCount(), i = 0; i < count; ++i) {
            final BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break;
            }
            if (bodyPart.isMimeType("text/html")) {
                final String html = (String) bodyPart.getContent();
                result = result + "\n" + Jsoup.parse(html).text();
            } else if (bodyPart.isMimeType("image/*")) {
                result = result + "\n" + bodyPart.getFileName() + "\n";
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    private static String procesMultiPart(final Multipart content, String downloadDirectory) throws IOException, MessagingException {
        String textContent = null;
        InputStream inStream = null;
        File f = null;
        Label_0384:
        {
            try {
                if (downloadDirectory == null) {
                    Reporter.log("Download directory is not provided... Attachments will be stored in default download directory: " + DataUtil.getDataFolder() + System.getProperty("file.separator") + "Attachments", true);
                    downloadDirectory = DataUtil.getDataFolder() + System.getProperty("file.separator") + "Attachments";
                }
                final File dir = new File(downloadDirectory);
                if (dir.exists()) {
                    FileUtils.deleteDirectory(dir);
                }
                dir.mkdir();
                for (int i = 0; i < content.getCount(); ++i) {
                    final BodyPart bodyPart = content.getBodyPart(i);
                    final Object o = bodyPart.getContent();
                    if (o instanceof String) {
                        Reporter.log("Mail content is plain text..!!", true);
                        textContent = o.toString();
                        inStream = new ByteArrayInputStream(textContent.getBytes(StandardCharsets.UTF_8));
                    } else if (bodyPart.getDisposition() != null && bodyPart.getDisposition().equalsIgnoreCase("attachment")) {
                        Reporter.log("Mail content is not just plain text..!!", true);
                        String fileName = bodyPart.getFileName();
                        if (fileName.contains("\\")) {
                            fileName = fileName.substring(fileName.lastIndexOf(92) + 1);
                        }
                        if (fileName.contains("/")) {
                            fileName = fileName.substring(fileName.lastIndexOf(47) + 1);
                        }
                        inStream = bodyPart.getInputStream();
                        f = new File(dir.getAbsolutePath() + System.getProperty("file.separator") + fileName);
                        if (f.exists()) {
                            f.delete();
                        }
                        f.createNewFile();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                break Label_0384;
            } finally {
                if (inStream != null) {
                    inStream.close();
                }
            }
            if (inStream != null) {
                inStream.close();
            }
            try {
                Throwable t = null;
                try {
                    final FileOutputStream outStream = new FileOutputStream(f);
                    try {
                        final byte[] tempBuffer = new byte[4096];
                        while (inStream.read(tempBuffer) != -1) {
                            outStream.write(tempBuffer);
                        }
                    } finally {
                        if (outStream != null) {
                            outStream.close();
                        }
                    }
                } finally {
                    if (t == null) {
                        final Throwable exception = new Throwable();
                        t = exception;
                    } else {
                        final Throwable exception = new Throwable();
                        if (t != exception) {
                            t.addSuppressed(exception);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return textContent;
    }

    private static void sleep(final int secs) {
        try {
            Thread.sleep(secs * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public enum Host {
        GMAIL("GMAIL", 0, "smtp.gmail.com", "587"),
        PEGA_INDIA_MAIL("PEGA_INDIA_MAIL", 1, "mail.in.pega.com", "");

        private String hostName;
        private String port;

        Host(final String name, final int ordinal, final String hostName, final String port) {
            this.hostName = hostName;
            this.port = port;
        }

        public String getHostName() {
            return this.hostName;
        }

        public String getPort() {
            return this.port;
        }
    }

    public enum Timeout {
        ONE_MIN("ONE_MIN", 0, 60),
        FIVE_MINS("FIVE_MINS", 1, 300),
        TEN_MINS("TEN_MINS", 2, 600);

        private int value;

        Timeout(final String name, final int ordinal, final int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}

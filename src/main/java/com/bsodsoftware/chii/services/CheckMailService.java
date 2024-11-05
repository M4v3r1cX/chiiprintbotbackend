package com.bsodsoftware.chii.services;

import java.util.Base64;
import java.util.Date;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;

@Service
public class CheckMailService {

	@Autowired
	private PrintFileService printService;
	
	@Scheduled(fixedDelay = 5000)
	public void checkearMail() {
		Folder carpeta = null;
		Store store = null; 	// I went to the STO sto sto stoooore
		try {
			String mailDeVuelta = "";
			String subjectDeVuelta = "";
			String receipentDeVuelta = "";
			
			Properties properties = new Properties();

            properties.put("mail.pop3.host", "pop.gmail.com");
            properties.put("mail.pop3.port", 995 + "");
            properties.put("mail.pop3.starttls.enable", "true");

            Session emailSession = Session.getDefaultInstance(properties);

            store = emailSession.getStore("pop3s");

            store.connect("pop.gmail.com", "chiiprintbot@gmail.com", "XXXXXXX");

            carpeta = store.getFolder("INBOX");
            carpeta.open(Folder.READ_WRITE);

            Message[] messages = carpeta.getMessages();
            for (final Message msg : messages) {
                subjectDeVuelta = msg.getSubject();
                InternetAddress sender = (InternetAddress) msg.getFrom()[0];
                receipentDeVuelta = sender.getAddress();
                String contentType = msg.getContentType();
                if (contentType.contains("multipart")) {
                	Multipart multiPart = (Multipart) msg.getContent();
                    
                    for (int i = 0; i < multiPart.getCount(); i++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            String fileb64 = Base64.getEncoder().encodeToString(part.getInputStream().readAllBytes());
                            boolean printed = printService.setPrint(fileb64);
                            if (printed) {
                            	mailDeVuelta = "Archivo " + part.getFileName() + " impreso correctamente, nyaa!~";
                            	msg.setFlag(Flags.Flag.DELETED, true);
                            	sendMail(emailSession, receipentDeVuelta, subjectDeVuelta + " : OK", mailDeVuelta);
                            } else {
                            	mailDeVuelta = "Archivo " + part.getFileName() + " no pudo ser impreso por un error ;____;";
                            	msg.setFlag(Flags.Flag.DELETED, true);
                            	sendMail(emailSession, receipentDeVuelta, subjectDeVuelta + " : NO OK", mailDeVuelta);
                            }
                        }
                    }
                } else {
                	mailDeVuelta = "Mail no contiene archivos adjuntos ;___;";
                	msg.setFlag(Flags.Flag.DELETED, true);
                	sendMail(emailSession, receipentDeVuelta, subjectDeVuelta + " : NO OK", mailDeVuelta);
                }
                
                
            }
            carpeta.close(true);
		} catch (Exception ex) {
			
		}
	}
	
	private void sendMail(Session session, String toEmail, String subject, String body) {
		try
	    {
	      MimeMessage msg = new MimeMessage(session);
	      //set message headers
	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      msg.addHeader("format", "flowed");
	      msg.addHeader("Content-Transfer-Encoding", "8bit");

	      msg.setFrom(new InternetAddress("chiiprintbot@gmail.com", "Chii Print Bot!"));

	      msg.setReplyTo(InternetAddress.parse("chiiprintbot@gmail.com", false));

	      msg.setSubject(subject, "UTF-8");

	      msg.setText(body, "UTF-8");

	      msg.setSentDate(new Date());

	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	      System.out.println("Message is ready");
    	  Transport.send(msg);  

	      System.out.println("EMail Sent Successfully!!");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}
}

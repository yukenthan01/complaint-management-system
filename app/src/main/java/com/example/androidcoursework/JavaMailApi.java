package com.example.androidcoursework;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class JavaMailApi extends AsyncTask<Void,Void,Void> {

    String subject,description,category,
            address,city,postcode,state,date,fullaname,imageUrl,categoryEmail,userEmail;
    private Context context;
    private Multipart _multipart = new MimeMultipart();
    private ProgressDialog progressDialog;
    private Session session;
    public JavaMailApi(Context context, String subject, String description, String category,
                       String address, String city, String postcode, String state, String date,
                       String fullaname, String imageUrl,String categoryEmail,String userEmail) {
        this.subject = subject;
        this.description = description;
        this.category = category;
        this.address = address;
        this.city = city;
        this.postcode = postcode;
        this.state = state;
        this.date = date;
        this.fullaname = fullaname;
        this.imageUrl = imageUrl;
        this.context = context;
        this.categoryEmail = categoryEmail;
        this.userEmail = userEmail;


    }


    @Override
    protected Void doInBackground(Void... voids) {
//        Log.d("TAG22",
//                "sendMail: "+ subject +" | "+description +" | "+category +" | "+ address +" | "+ city+" | "+postcode +" | "+ state+" | "+date
//                        +" | "+ fullaname+" | "+ imageUrl+" | "+ categoryEmail+" | "+ userEmail);
//        //Creating properties
        Properties props = new Properties();
        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //This is your from email
         final String EMAIL = "yukenthanportuno@gmail.com";

        //This is your from email password
        final String PASSWORD = "hjgzfjcruyihxrpx";
        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL,PASSWORD);
                    }
                });

        try {
            Address[] toAddress= new Address[1];
           // String subject = "New User Added";
            String body = "Hi";
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setFrom(new InternetAddress("no-reply@astromyntra.in"));
            message.setSender(new InternetAddress("yukenthanportuno@gmail.com"));
            message.setSubject(subject);
            message.setDataHandler(handler);
            BodyPart messageBodyPart = new MimeBodyPart();
            InputStream is = context.getAssets().open("user_profile.html");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String str = new String(buffer);
            str =str.replace("$$headermessage$$","You have a New Compliant.");
            str=str.replace("$$username$$", fullaname);
            str=str.replace("$$replayTo$$", userEmail);
            str=str.replace("$$postCode$$",postcode);
            str=str.replace("$$city$$",city);
            str=str.replace("$$description$$",description);
            str=str.replace("$$address$$",address);
            str=str.replace("$$imageUrl$$",imageUrl);
            messageBodyPart.setContent(str,"text/html; charset=utf-8");
            _multipart.addBodyPart(messageBodyPart);

            // Put parts in message
            message.setContent(_multipart);
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(categoryEmail));
            Transport.send(message);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            Log.d("tag", "doInBackground: " + e.getMessage());
        }
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Show progress dialog while sending email
        progressDialog = ProgressDialog.show(context,"Sending message", "Please wait...",false,
                false);
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismiss progress dialog when message successfully send
        progressDialog.dismiss();
        //Show success toast
        Toast.makeText(context,"Message Sent",Toast.LENGTH_SHORT).show();
    }


}

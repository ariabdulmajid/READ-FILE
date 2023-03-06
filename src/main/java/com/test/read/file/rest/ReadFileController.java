package com.test.read.file.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Ari Abdul Majid
 * @version :$, (Created on 05/03/2023)
 * @since 1.0.Alpha1
 */

@RestController
@RequestMapping("/input")
@Slf4j
public class ReadFileController {

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping
    public ResponseEntity readFile(@RequestParam("file") MultipartFile file) {

        // Check if the file is empty or extension not txt
        if (file.isEmpty() || !file.getOriginalFilename().split("\\.")[1].equalsIgnoreCase("txt")) {
            return new ResponseEntity<>("Please upload file with extension .txt", HttpStatus.BAD_REQUEST);
        }

        String bniTitle = "";
        String bniBody;
        String bniMessage = "";
        String mandiriTitle = "";
        String mandiriBody;
        String mandiriMessage = "";
        String allMessage;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String words;
            String[] word;

            while ((words = reader.readLine()) != null) {
                word = words.split(";");

                if (word[0].equalsIgnoreCase("BNI")) {
                    bniTitle = "Selamat Siang Rekan Bank "+ word[0] +",\n\nMohon bantuan untuk Sign on pada envi berikut:\n";
                    bniMessage += "- Envi "+ word[1] +" Port "+ word[2] +" terpantau "+ word[4] +"\n";
                }

                if (word[0].equalsIgnoreCase("MDR")) {
                    mandiriTitle = "Selamat Siang Rekan Bank "+ word[0] +",\n\nMohon bantuan untuk Sign on pada envi berikut:\n";
                    mandiriMessage += "- Envi "+ word[1] +" Port "+ word[2] +" terpantau "+ word[4] +"\n";
                }
            }
            reader.close();

            bniBody = bniTitle + "\n" + bniMessage;
            mandiriBody = mandiriTitle + "\n" + mandiriMessage;

            if (!bniTitle.equals("")){
                allMessage = bniBody+"\n------------------------------------------------\n\n"+mandiriBody;
            } else {
                allMessage = mandiriBody;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error reading file", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Send email messages to BNI and Mandiri
        if (!bniTitle.equals("")){
            String email = "bniemail@gmail.com";
            String subject = "TEST SENT EMAIL BNI";
            sendEmail(email, subject, bniBody);
            log.info("Email sent to BNI");
        }

        if (!mandiriTitle.equals("")){
            String email = "mandiriemail@gmail.com";
            String subject = "TEST SENT EMAIL MANDIRI";
            sendEmail(email, subject, mandiriBody);
            log.info("Email sent to Mandiri");
        }

        return new ResponseEntity<>(allMessage, HttpStatus.OK);
    }

    private void sendEmail(String email, String subject, String bodyEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("testfrom@gmail.com");
        message.setTo(email);
        message.setText(bodyEmail);
        message.setSubject(subject);
        javaMailSender.send(message);
    }

}

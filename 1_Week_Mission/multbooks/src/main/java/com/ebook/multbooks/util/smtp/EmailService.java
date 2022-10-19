package com.ebook.multbooks.util.smtp;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
/**
 * smtp 관련 서비스
 * */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final MailSender mailSender;

    //새 비밀번호 문자열 발급
    public String makeRandomPw(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    //이메일 전송
    public void sendEmail(String email,String subject,String body) {
        String addr = "shzero211@gmail.com";

        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom(addr);
        smm.setTo(email);
        smm.setSubject(subject);
        smm.setText(body);

        mailSender.send(smm);
    }
}

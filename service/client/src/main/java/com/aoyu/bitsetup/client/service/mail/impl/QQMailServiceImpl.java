package com.aoyu.bitsetup.client.service.mail.impl;

import com.aoyu.bitsetup.client.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @ClassName：QQMailServiceImpl
 * @Author: aoyu
 * @Date: 2025-09-20 13:04
 * @Description: QQ邮箱
 */

@Service
public class QQMailServiceImpl implements MailService {


    @Autowired
    private JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送验证码邮件
     *
     * @param to      收件人邮箱地址，例如 "example@domain.com"
     * @param subject 邮件主题，例如 "验证码邮件"
     * @param content 邮件正文内容，例如 "您的验证码是 123456"
     */
    @Override
    public void sendCodeMail(String to, String subject, String content) {
        // 创建一个简单邮件对象
        SimpleMailMessage message = new SimpleMailMessage();

        // 设置邮件发送者，通常在配置文件中配置，例如 "noreply@domain.com"
        message.setFrom(from);

        // 设置收件人邮箱地址
        message.setTo(to);

        // 设置邮件主题
        message.setSubject(subject);

        // 设置邮件正文
        message.setText(content);

        // 发送邮件
        mailSender.send(message);
    }

}

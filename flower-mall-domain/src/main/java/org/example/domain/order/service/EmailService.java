package org.example.domain.order.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-15 23:32
 */
@Component
public class EmailService {
    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Async // 异步执行，防止发邮件卡住下单主线程
    public void sendOrderConfirm(String toEmail, String orderId) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(toEmail); // 假设用户表里存了邮箱，或者下单时填了邮箱
            message.setSubject("【鲜花商城】订单确认通知");
            message.setText("亲爱的用户，您的订单 " + orderId + " 已创建成功！我们将尽快发货。");

            mailSender.send(message);
            System.out.println("邮件发送成功: " + toEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

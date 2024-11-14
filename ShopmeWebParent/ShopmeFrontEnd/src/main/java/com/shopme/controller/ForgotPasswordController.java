package com.shopme.controller;

import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.entity.EmailSettingBag;
import com.shopme.service.CustomerService;
import com.shopme.service.SettingService;
import com.shopme.util.Utility;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/forgot-password")
    public String viewForgotPasswordForm() {
        return "customer/forgot_password_form";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email,
                                        Model model,
                                        HttpServletRequest request) {
        try {
            String token = customerService.updateResetPasswordToken(email);
            String link = Utility.getSiteURL(request) + "/reset-password?token=" + token;

            sendMail(link, email);

            model.addAttribute("message", "Đường dẫn đặt lại mật khẩu đã được gửi tới email của bạn!");
        } catch (ResourceNotFoundException e) {
            model.addAttribute("messageError", e.getMessage());
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("messageError", "Không thể gửi mail!");
        }

        return "customer/forgot_password_form";
    }

    @GetMapping("/reset-password")
    public String viewResetPasswordForm(@RequestParam("token") String token, Model model) {
        try {
            customerService.getCustomerByResetPasswordToken(token);
            model.addAttribute("token", token);
            return "customer/reset_password_form";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("pageTitle", "Token không hợp lệ");
            return "message";
        }
    }

    @PostMapping("/reset-password")
    public String processResetPasswordForm(@RequestParam("token") String token,
                                           @RequestParam("password") String password,
                                           Model model) {
        try {
            customerService.updatePassword(token, password);

            model.addAttribute("title", "Đặt lại mật khẩu");
            model.addAttribute("message", "Bạn đã đặt lại mật khẩu thành công!");
        } catch (ResourceNotFoundException e) {
            model.addAttribute("message", e.getMessage());
        }

        model.addAttribute("pageTitle", "Đặt lại mật khẩu");

        return "message";
    }

    private void sendMail(String link, String email) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettingBag();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = email;
        String subject = "Đây là đường dẫn đặt lại mật khẩu của bạn";
        String content = "<p>Xin chào,</p>" +
                "<p>Bạn đã yêu cầu đặt lại mật khẩu.</p>" +
                "<p>Bấm vào link dưới đây để thay đổi mật khẩu:</p>" +
                "<p><a href=\"" + link + "\">Thay đổi mật khẩu</a></p>" +
                "<br>" +
                "<p>Bỏ qua email này nếu bạn nhớ mật khẩu, hoặc không gửi yêu cầu này.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
}

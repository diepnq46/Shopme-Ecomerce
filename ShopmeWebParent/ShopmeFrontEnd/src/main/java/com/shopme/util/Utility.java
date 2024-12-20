package com.shopme.util;

import com.shopme.entity.EmailSettingBag;
import com.shopme.security.oauth.CustomerOauth2User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Properties;

public class Utility {
    public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
        String customerEmail = null;
        Object principal = request.getUserPrincipal();
        if (principal instanceof UsernamePasswordAuthenticationToken ||
                principal instanceof RememberMeAuthenticationToken) {
            customerEmail = request.getUserPrincipal().getName();
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
            CustomerOauth2User oauth2User = (CustomerOauth2User) oauth2Token.getPrincipal();

            customerEmail = oauth2User.getEmail();
        }

        return customerEmail;
    }

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();

        return siteURL.replace(request.getServletPath(), "");
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(settings.getHost());
        mailSender.setPort(settings.getPort());
        mailSender.setUsername(settings.getUserName());
        mailSender.setPassword(settings.getPassword());

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
        properties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());

        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }
}

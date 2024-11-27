package com.shopme.controller;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.entity.EmailSettingBag;
import com.shopme.security.CustomerUserDetails;
import com.shopme.security.oauth.CustomerOauth2User;
import com.shopme.service.CustomerService;
import com.shopme.service.SettingService;
import com.shopme.util.Utility;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.shopme.util.Utility.getEmailOfAuthenticatedCustomer;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    private final SettingService settingService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        List<Country> listCountries = customerService.getAllCountries();

        model.addAttribute("listCountries", listCountries);
        model.addAttribute("pageTitle", "Tạo tài khoản mới");
        model.addAttribute("customer", new Customer());

        return "register/register_form";
    }

    @PostMapping("/create-customer")
    public String createCustomer(@ModelAttribute("customer") Customer customer, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        customerService.registerCustomer(customer);

        sendVerificationEmail(request, customer);

        return "register/register_success";
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("code") String code) {
        boolean verify = customerService.verify(code);

        return "register/" + (verify ? "verify_success" : "verify_failure");
    }

    @GetMapping("/account-details")
    public String showAccountDetails(Model model, HttpServletRequest request) {
        String userEmail = getEmailOfAuthenticatedCustomer(request);
        Customer customer = customerService.getCustomerByEmail(userEmail);
        List<Country> listCountries = customerService.getAllCountries();

        model.addAttribute("customer", customer);
        model.addAttribute("listCountries", listCountries);

        return "customer/account_form";
    }


    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettingBag();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = customer.getEmail();
        String subject = emailSettings.getCustomerVerifySubject();
        String content = emailSettings.getCustomerVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", customer.getFirstName());

        String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
        content = content.replace("[[URL]]",  verifyURL);
        helper.setText(content, true);

        mailSender.send(message);

        System.out.println("To Adress: " + toAddress);
        System.out.println("Verify URL: " + verifyURL);
    }

    @PostMapping("/update-account-details")
    public String updateAccountDetails(@ModelAttribute Customer customer,
                                       Model model, RedirectAttributes redirectAttributes,
                                       HttpServletRequest request) {
        customerService.updateCustomer(customer);
        updateNameForAuthenticatedCustomer(customer, request);

        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công");
        String redirectURL = "redirect:/account-details";
        String redirectOption = request.getParameter("redirect");

        if ("cart".equals(redirectOption)) {
            redirectURL = "redirect:/cart";
        }

        return redirectURL;
    }

    private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
        Object principal = request.getUserPrincipal();
        if (principal instanceof UsernamePasswordAuthenticationToken ||
                principal instanceof RememberMeAuthenticationToken) {
            CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);

            userDetails.setFullName(customer.getFullName());
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
            CustomerOauth2User oauth2User = (CustomerOauth2User) oauth2Token.getPrincipal();

            oauth2User.setFullName(customer.getFullName());
        }
    }

    private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) principal;
            return (CustomerUserDetails) authenticationToken.getPrincipal();
        }else if (principal instanceof RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken authenticationToken = (RememberMeAuthenticationToken) principal;
            return (CustomerUserDetails) authenticationToken.getCredentials();
        }

        return null;
    }
}

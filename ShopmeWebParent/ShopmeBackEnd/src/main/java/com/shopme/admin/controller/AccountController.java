package com.shopme.admin.controller;

import com.shopme.admin.security.UserDetailsConfig;
import com.shopme.admin.service.UserService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {
    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewAccountDetails(@AuthenticationPrincipal UserDetailsConfig userDetails,
                                     Model model) {
        String email = userDetails.getUsername();
        User user = userService.findUserByEmail(email);

        model.addAttribute("user", user);

        return "users/account_form";
    }

    @PostMapping("/account/update")
    public String updateAccount(@ModelAttribute User user,
                                @RequestParam("image") MultipartFile image,
                                RedirectAttributes redirectAttributes) throws IOException {
        if (!image.isEmpty()) {
            String filename = StringUtils.cleanPath(image.getOriginalFilename());
            user.setPhotos(filename);
            String uploadDir = "user-photos/" + user.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, filename, image);
        } else {
            user.setPhotos(null);
        }

        userService.updateAccount(user);
        redirectAttributes.addFlashAttribute("message", "Thông tin tài khoản được cập nhật thành công!");
        return "redirect:/account";
    }
}

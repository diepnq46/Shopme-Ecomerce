package com.shopme.admin.controller;

import com.lowagie.text.DocumentException;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.UserService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.admin.util.UserCsvExporter;
import com.shopme.admin.util.UserExcelExporter;
import com.shopme.admin.util.UserPdfExporter;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.common.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static com.shopme.admin.service.impl.UserServiceImpl.USER_PER_PAGE;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping()
    public String listAll() {
        return "redirect:/users/page/1?sortField=firstName&sortDir=asc";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        List<Role> listRoles = service.getAllRoles();
        User user = new User();
        user.setEnabled(true);

        model.addAttribute("pageTitle", "Thêm mới người dùng");
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);

        return "users/user_form";
    }

    @PostMapping
    public String saveUser(@ModelAttribute User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = service.save(user);
            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            service.save(user);
        }

        redirectAttributes.addFlashAttribute("message", "Người dùng đã được lưu thành công!");

        return getRedirectUrlToAffectedUser(user);
    }

    private static String getRedirectUrlToAffectedUser(User user) {
        String firstPathOfEmail = user.getEmail().split("@")[0];

        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPathOfEmail;
    }

    @GetMapping("/update/{userId}")
    public String updateUser(@PathVariable(name = "userId") Integer userId,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        List<Role> listRoles = service.getAllRoles();
        try {
            User user = service.findUserById(userId);

            model.addAttribute("pageTitle", "Cập nhật người dùng (ID: " + userId + ")");
            model.addAttribute("user", user);
            model.addAttribute("listRoles", listRoles);

            return "users/user_form";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable(name = "userId") Integer userId,
                             RedirectAttributes redirectAttributes) {
        service.deleteUserById(userId);
        redirectAttributes.addFlashAttribute("message", "Đã xóa người dùng với ID: " + userId);
        return "redirect:/users/page/1?sortField=firstName&sortDir=asc";
    }

    @GetMapping("/{userId}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable("userId") Integer id,
                                      @PathVariable("status") boolean enabled,
                                      RedirectAttributes redirectAttributes) {
        String status = enabled ? " đã kích hoạt" : " đã vô hiệu hóa";

        service.updateEnabledStatus(id, enabled);
        redirectAttributes.addFlashAttribute("message", "Người dùng với ID " + id + status + " thành công!");

        return "redirect:/users/page/1?sortField=firstName&sortDir=asc";
    }

    @GetMapping("/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") Integer pageNum,
                             @PagingAndSortingParam(listName = "listUsers") PagingAndSortingHelper helper) {
        Page<User> page = service.getUserByPage(pageNum, helper);
        helper.updateModelAttribute(pageNum, page);

        return "users/users";
    }

    @GetMapping("/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<User> userList = service.getAllUsers();

        UserCsvExporter exporter = new UserCsvExporter();

        exporter.export(userList, response);
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> userList = service.getAllUsers();

        UserExcelExporter exporter = new UserExcelExporter();

        exporter.export(userList, response);
    }

    @GetMapping("/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws DocumentException, IOException {
        List<User> userList = service.getAllUsers();

        UserPdfExporter exporter = new UserPdfExporter();

        exporter.export(userList, response);
    }
}

package com.shopme.admin.controller;

import com.shopme.admin.service.SettingService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.GeneralSettingBag;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/settings")
public class SettingController {
    @Autowired
    private SettingService service;

    @GetMapping
    public String listAll(Model model) {
        List<Setting> listSettings = service.getAllSettings();
        List<Currency> listCurrencies = service.getAllCurrencies();

        model.addAttribute("listCurrencies", listCurrencies);
        for (Setting setting : listSettings) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        return "settings/settings";
    }

    @PostMapping("/save-general")
    public String saveGeneral(@RequestParam("fileImage") MultipartFile fileImage,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) throws IOException {
        GeneralSettingBag settingBag = service.getGeneralSettings();
        saveSiteLogo(fileImage, settingBag);
        saveCurrencySymbol(settingBag, request);
        updateSettingValuesFromForm(settingBag.list(), request);

        redirectAttributes.addFlashAttribute("message", "Cài đặt chung đã được lưu!");

        return "redirect:/settings";
    }

    @PostMapping("/save-mail-server")
    public String saveMailServer(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<Setting> mailServerSettings = service.getSettingsByCategory(SettingCategory.MAIL_SERVER);

        updateSettingValuesFromForm(mailServerSettings, request);
        redirectAttributes.addFlashAttribute("message", "Cài đặt mail server đã được lưu!");

        return "redirect:/settings";
    }

    @PostMapping("/save-mail-template")
    public String saveMailTemplate(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<Setting> mailTemplateSettings = service.getSettingsByCategory(SettingCategory.MAIL_TEMPLATE);

        updateSettingValuesFromForm(mailTemplateSettings, request);
        redirectAttributes.addFlashAttribute("message", "Cài đặt mail template đã được lưu!");

        return "redirect:/settings";
    }

    private void saveCurrencySymbol(GeneralSettingBag settingBag, HttpServletRequest request) {
        int currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        service.updateCurrencySymbol(currencyId, settingBag);
    }

    private static void saveSiteLogo(MultipartFile fileImage, GeneralSettingBag settingBag) throws IOException {
        if (!fileImage.isEmpty()) {
            String fileName = StringUtils.cleanPath(fileImage.getOriginalFilename());
            String value = "/site-logo/" + fileName;
            settingBag.updateSiteLogo(value);
            String uploadDir = "../site-logo/";
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, fileImage);
        }
    }

    private void updateSettingValuesFromForm(List<Setting> listSettings, HttpServletRequest request) {
        for (Setting setting : listSettings) {
            String value = request.getParameter(setting.getKey());

            if (value != null) {
                setting.setValue(value);
            }
        }
        service.saveAllSettings(listSettings);
    }
}

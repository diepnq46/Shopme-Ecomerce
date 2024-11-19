package com.shopme.admin.controller;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.CountryService;
import com.shopme.admin.service.ShippingRateService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/shipping-rates")
public class ShippingRateController {
    @Autowired
    private ShippingRateService shippingRateService;

    @Autowired
    private CountryService countryService;

    @GetMapping
    public String listFirstPage() {
        return "redirect:/shipping-rates/page/1?sortDir=asc&sortField=id";
    }

    @GetMapping("/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum,
                             @PagingAndSortingParam(listName = "shippingRates") PagingAndSortingHelper helper,
                             @RequestParam(name = "keyword", required = false) String keyword) {
        Page<ShippingRate> page = shippingRateService.listByPage(pageNum, helper);

        helper.updateModelAttribute(pageNum, page);

        return "shipping_rates/shipping_rates";
    }

    @GetMapping("/new")
    public String createNewShippingRate(Model model) {
        List<Country> countries = countryService.getAllCountries();
        ShippingRate shippingRate = new ShippingRate();

        model.addAttribute("shippingRate", shippingRate);
        model.addAttribute("countries", countries);
        model.addAttribute("pageTitle", "Thêm mới");

        return "shipping_rates/shipping_rate_form";
    }

    @PostMapping
    public String saveShippingRate(@ModelAttribute ShippingRate shippingRate, RedirectAttributes redirectAttributes) {
        shippingRateService.saveShippingRate(shippingRate);
        redirectAttributes.addFlashAttribute("message", "Bảng giá được lưu thành công!");

        return "redirect:/shipping-rates/page/1?sortDir=asc&sortField=id";
    }

    @GetMapping("/{id}/update-cod/{supported}")
    public String updateSupportedCOD(@PathVariable("id") Integer id,
                                     @PathVariable("supported") boolean supported,
                                     RedirectAttributes redirectAttributes) {
        String status = supported ? " đã kích hoạt" : " đã vô hiệu hóa";
        try {
            shippingRateService.updateSupportedCOD(id, supported);
            redirectAttributes.addFlashAttribute("message", "Hỗ trợ COD" + status + " thành công!");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
        }
        return "redirect:/shipping-rates/page/1?sortDir=asc&sortField=id";
    }

    @GetMapping("/update/{id}")
    public String updateShippingRate(@PathVariable("id") Integer id,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            ShippingRate shippingRate = shippingRateService.getShippingRateById(id);
            List<Country> countries = countryService.getAllCountries();

            model.addAttribute("shippingRate", shippingRate);
            model.addAttribute("countries", countries);
            model.addAttribute("pageTitle", "Cập nhật");

            return "shipping_rates/shipping_rate_form";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/shipping-rates/page/1?sortDir=asc&sortField=id";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteShippingRate(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        shippingRateService.deleteShippingRateById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công bảng giá với ID: " + id);

        return "redirect:/shipping-rates/page/1?sortDir=asc&sortField=id";
    }
}

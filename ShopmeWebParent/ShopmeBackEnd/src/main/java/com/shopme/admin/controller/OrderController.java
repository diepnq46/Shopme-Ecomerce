package com.shopme.admin.controller;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.OrderService;
import com.shopme.admin.service.SettingService;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.common.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private SettingService settingService;

    @GetMapping
    public String listFirstPage() {
        return "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";
    }

    @GetMapping("/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") Integer pageNum,
                             @PagingAndSortingParam(listName = "orders") PagingAndSortingHelper helper,
                             HttpServletRequest request) {
        Page<Order> page = orderService.listByPage(pageNum, helper);

        helper.updateModelAttribute(pageNum, page);
        loadCurrencySetting(request);
        return "orders/orders";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer orderId, RedirectAttributes redirectAttributes) {
        orderService.deleteOrderById(orderId);

        redirectAttributes.addFlashAttribute("message", "Xóa thành công đơn hàng với ID: " + orderId);
        return "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";
    }

    @GetMapping("/detail/{orderId}")
    public String viewDetailOrder(@PathVariable("orderId") Integer orderId,
                                  Model model,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.getOrderById(orderId);
            loadCurrencySetting(request);
            model.addAttribute("order", order);

            return "orders/order_detail_modal";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";
        }
    }

    private void loadCurrencySetting(HttpServletRequest request) {
        List<Setting> currencySettings = settingService.getSettingsByCategory(SettingCategory.CURRENCY);

        for (Setting setting : currencySettings) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }
}

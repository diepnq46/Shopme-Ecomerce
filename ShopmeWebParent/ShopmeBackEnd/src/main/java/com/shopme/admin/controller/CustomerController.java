package com.shopme.admin.controller;

import com.shopme.admin.service.CountryService;
import com.shopme.admin.service.CustomerService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.shopme.admin.service.impl.CustomerServiceImpl.CUSTOMERS_PER_PAGE;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CountryService countryService;

    @GetMapping
    public String listFirstPage(Model model) {
        return listByPage(1, "firstName", "asc", null ,model);
    }

    @PostMapping
    public String saveCustomer(@ModelAttribute Customer customer,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(customer);
            String keyword = updatedCustomer.getEmail().split("@")[0];

            redirectAttributes.addFlashAttribute("message", "Cập nhật thành công khách hàng với ID: " + customer.getId());
            return "redirect:/customers/page/1?sortField=firstName&sortDir=asc&keyword=" + keyword;
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/customers";
        }
    }

    @GetMapping("/page/{pageNum}")
    public String listByPage(@PathVariable Integer pageNum,
                             @RequestParam(value = "sortField", defaultValue = "firstName") String sortField,
                             @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             Model model) {
        Page<Customer> page = customerService.listByPage(pageNum, sortField, sortDir, keyword);

        int startCount = (pageNum - 1) * CUSTOMERS_PER_PAGE + 1;
        int endCount = startCount + CUSTOMERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = (int) page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("listCustomers", page.getContent());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "customers/customers";
    }

    @GetMapping("/{customerId}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable("customerId") Integer customerId,
                                      @PathVariable("status") boolean enabledStatus,
                                      RedirectAttributes redirectAttributes) {
        String status = enabledStatus ? " đã kích hoạt" : " đã vô hiệu hóa";

        customerService.updateEnabledStatus(customerId, enabledStatus);

        redirectAttributes.addFlashAttribute("message", "Khách hàng với ID: " + customerId + status + " thành công!");
        return  "redirect:/customers";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            Customer customer = customerService.getCustomer(id);
            List<Country> listCountries = countryService.getAllCountries();

            model.addAttribute("customer", customer);
            model.addAttribute("listCountries", listCountries);
            model.addAttribute("pageTitle", "Cập nhật khách hàng (ID: " + id + ")");

            return "customers/customer_form";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/customers";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        customerService.deleteCustomer(id);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công khách hàng với ID: " + id);

        return "redirect:/customers";
    }

    @GetMapping("/detail/{id}")
    public String viewDetailCustomer(@PathVariable Integer id,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            Customer customer = customerService.getCustomer(id);

            model.addAttribute("customer", customer);
            return "customers/customer_detail_modal";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/customers";
        }
    }
}

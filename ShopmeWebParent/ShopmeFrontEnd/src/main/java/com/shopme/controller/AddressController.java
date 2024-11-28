package com.shopme.controller;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.service.AddressService;
import com.shopme.service.CustomerService;
import com.shopme.util.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/address-book")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;


    @GetMapping
    public String showAddressBook(Model model, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        List<Address> listAddresses = addressService.listAddressBook(customer);
        boolean isPrimaryDefaultAddress = true;
        for (Address address : listAddresses) {
            if (address.isDefaultAddress()) {
                isPrimaryDefaultAddress = false;
            }
        }

        model.addAttribute("isPrimaryDefaultAddress", isPrimaryDefaultAddress);
        model.addAttribute("customer", customer);
        model.addAttribute("listAddresses", listAddresses);
        return "address_book/addresses";
    }

    @GetMapping("/new")
    public String newAddress(Model model, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);

        Address address = new Address();
        address.setCustomer(customer);

        model.addAttribute("address", address);
        model.addAttribute("pageTitle", "Thêm địa chỉ mới");

        return "address_book/address_form";
    }

    @PostMapping
    public String saveAddressBook(@ModelAttribute Address address, RedirectAttributes redirectAttributes) {
       addressService.saveAddress(address);

        redirectAttributes.addFlashAttribute("message", "Lưu địa chỉ thành công!");
        return "redirect:/address-book";
    }

    @GetMapping("/default/{id}")
    public String setDefaultAddress(@PathVariable("id") Integer addressId, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        addressService.setDefaultAddress(addressId, customer);
        String redirectURL = "redirect:/address-book";
        String redirectOption = request.getParameter("redirect");
        if ("cart".equals(redirectOption)) {
            redirectURL = "redirect:/cart";
        }else if("checkout".equals(redirectOption)) {
            redirectURL = "redirect:/checkout";
        }

        return redirectURL;
    }

    @GetMapping("/delete/{addressId}")
    public String deleteAddressBook(@PathVariable("addressId") Integer addressId, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        addressService.deleteAddress(addressId, customer);

        redirectAttributes.addFlashAttribute("message", "Xóa thành công địa chỉ với ID: " + addressId);
        return "redirect:/address-book";
    }

    @GetMapping("/edit/{addressId}")
    public String editAddressBook(@PathVariable("addressId") Integer addressId,
                                  Model model,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {
        try {
            Customer customer = getAuthenticatedCustomer(request);

            Address address = addressService.getAddress(addressId, customer);
            List<Country> countries = customerService.getAllCountries();

            model.addAttribute("address", address);
            model.addAttribute("countries", countries);
            model.addAttribute("pageTitle", "Cập nhật địa chỉ");

            return "address_book/address_form";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());

            return "redirect:/address-book";
        }
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(email);
    }
}

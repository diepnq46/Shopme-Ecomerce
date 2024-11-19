package com.shopme.admin.controller;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.BrandService;
import com.shopme.admin.service.CategoryService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static com.shopme.admin.service.impl.BrandServiceImpl.BRAND_PER_PAGE;

@Controller
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    private final CategoryService categoryService;


    @GetMapping
    public String listAllBrands() {
        return "redirect:/brands/page/1?sortField=name&sortDir=asc";
    }

    @GetMapping("/page/{pageNum}")
    public String listBrandPage(@PathVariable Integer pageNum,
                                @PagingAndSortingParam(listName = "brands") PagingAndSortingHelper helper) {
        Page<Brand> brandPage = brandService.getBrandsByPage(pageNum, helper);

        helper.updateModelAttribute(pageNum, brandPage);

        return "brands/brands";
    }

    @GetMapping("/new")
    public String newBrand(Model model) {
        List<Category> categoriesInForm = categoryService.getAllCategoriesInform();
        Brand brand = new Brand();

        model.addAttribute("brand", brand);
        model.addAttribute("categoriesInForm", categoriesInForm);
        model.addAttribute("pageTitle", "Thêm mới nhãn hàng");

        return "brands/brand_form";
    }

    @PostMapping()
    public String saveBrand(@ModelAttribute("brand") Brand brand,
                            RedirectAttributes redirectAttributes,
                            @RequestParam("fileImage") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            brand.setLogo(fileName);
            Brand savedBrand = brandService.save(brand);

            String uploadDir = "../brand-logos/" + savedBrand.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, file);
        } else {
            brandService.save(brand);
        }
        redirectAttributes.addFlashAttribute("message", "Nhãn hàng được lưu thành công!");

        return "redirect:/brands";
    }

    @GetMapping("/update/{id}")
    public String updateBrand(@PathVariable("id") Integer id,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try {
            Brand brand = brandService.findBrandById(id);
            List<Category> categoriesInForm = categoryService.getAllCategoriesInform();

            model.addAttribute("categoriesInForm", categoriesInForm);
            model.addAttribute("brand", brand);
            model.addAttribute("pageTitle", "Cập nhật nhãn hàng (ID: " + id + ")");

            return "brands/brand_form";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/brands";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        brandService.deleteBrandById(id);

        redirectAttributes.addFlashAttribute("message", "Đã xóa nhãn hàng với ID: " + id);
        return "redirect:/brands/page/1?sortField=name&sortDir=asc";
    }
}

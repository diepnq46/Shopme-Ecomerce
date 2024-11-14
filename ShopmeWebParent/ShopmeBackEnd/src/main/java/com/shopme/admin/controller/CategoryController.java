package com.shopme.admin.controller;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.response.PageCategoryResponse;
import com.shopme.admin.service.CategoryService;
import com.shopme.admin.util.CategoryCsvExporter;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.shopme.admin.service.impl.CategoryServiceImpl.ROOT_CATEGORY_PER_PAGE;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping()
    public String listAllCategories(Model model) {
        return listByPage(1, "asc", null, model);
    }

    @GetMapping("/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum,
                             @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             Model model) {
        PageCategoryResponse response = service.getAllCategories(pageNum, sortDir, keyword);

        List<Category> categories = response.getCategories() != null ? response.getCategories() : new ArrayList<Category>();
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        int startCount = (pageNum - 1) * ROOT_CATEGORY_PER_PAGE + 1;
        int endCount = startCount + ROOT_CATEGORY_PER_PAGE - 1;
        if (endCount > response.getTotalElements()) {
            endCount = (int) response.getTotalElements();
        }


        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("categories", categories);
        model.addAttribute("sortField", "name");
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", response.getTotalPages());
        model.addAttribute("totalItems", response.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("keyword", keyword);

        return "categories/categories";
    }

    @GetMapping("/new")
    public String newCategory(Model model) {
        Category category = new Category();

        List<Category> categoriesInForm = service.getAllCategoriesInform();

        model.addAttribute("category", category);
        model.addAttribute("pageTitle", "Thêm mới danh mục");
        model.addAttribute("categoriesInForm", categoriesInForm);

        return "categories/category_form";
    }

    @PostMapping()
    public String saveCategory(@ModelAttribute Category category,
                               @RequestParam("fileImage") MultipartFile image,
                               RedirectAttributes redirectAttributes) throws IOException {
        if (!image.isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            category.setPhotos(fileName);

            Category savedCategory = service.save(category);

            String uploadDir = "../category-images/" + savedCategory.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, image);
        } else {
            service.save(category);
        }

        redirectAttributes.addFlashAttribute("message", "Danh mục được lưu thành công!");
        return "redirect:/categories";
    }

    @GetMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") Integer id,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        try {
            Category category = service.findCategoryById(id);

            List<Category> parentCategories = service.getAllCategoriesInform();

            model.addAttribute("category", category);
            model.addAttribute("pageTitle", "Cập nhật danh mục (ID: " + id + ")");
            model.addAttribute("categoriesInForm", parentCategories);


            return "categories/category_form";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        String categoryDir = "../category-images/" + id;

        service.deleteCategoryById(id);
        FileUploadUtil.removeDir(categoryDir);

        redirectAttributes.addFlashAttribute("message", "Đã xóa danh mục với ID: " + id);
        return "redirect:/categories";
    }

    @GetMapping("/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable("id") Integer id,
                                      @PathVariable("status") boolean enabled,
                                      RedirectAttributes redirectAttributes) {
        String status = enabled ? " đã kích hoạt" : " đã vô hiệu hóa";

        service.updateEnabledStatus(id, enabled);

        redirectAttributes.addFlashAttribute("message", "Danh mục với ID: " + id + status + " thành công!");

        return "redirect:/categories";
    }

    @GetMapping("/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<Category> categories = service.getAllCategoriesInform();

        CategoryCsvExporter exporter = new CategoryCsvExporter();
        exporter.export(categories, response);
    }
}

package com.shopme.controller;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.service.CategoryService;
import com.shopme.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.shopme.service.impl.ProductServiceImpl.PRODUCTS_PER_PAGE;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/c/{categoryAlias}")
    public String viewByCategoryFirstPage(@PathVariable("categoryAlias") String categoryAlias, Model model) {
        return viewByCategoryPage(categoryAlias, 1, model);
    }

    @GetMapping("/c/{categoryAlias}/page/{pageNum}")
    public String viewByCategoryPage(@PathVariable("categoryAlias") String categoryAlias,
                             @PathVariable("pageNum") Integer pageNum,
                             Model model) {
        try {
            Category category = categoryService.getCategory(categoryAlias);

            List<Category> listParentCategories = categoryService.getParentCategories(category);
            Page<Product> pageProducts = productService.getProductsByCategory(pageNum, category.getId());

            int startCount = (pageNum - 1) * PRODUCTS_PER_PAGE + 1;
            int endCount = startCount + PRODUCTS_PER_PAGE - 1;
            if (endCount > pageProducts.getTotalElements()) {
                endCount = (int) pageProducts.getTotalElements();
            }

            model.addAttribute("category", category);
            model.addAttribute("listParentCategories", listParentCategories);
            model.addAttribute("pageTitle", category.getName());
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("totalPages", pageProducts.getTotalPages());
            model.addAttribute("totalItems", pageProducts.getTotalElements());
            model.addAttribute("listProducts", pageProducts.getContent());

            return "product/products_by_category";
        } catch (ResourceNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/p/{productAlias}")
    public String viewDetail(@PathVariable("productAlias") String productAlias, Model model) {
        try {
            Product product = productService.getProductByAlias(productAlias);
            List<Category> listParentCategories = categoryService.getParentCategories(product.getCategory());


            model.addAttribute("product", product);
            model.addAttribute("listParentCategories", listParentCategories);
            model.addAttribute("pageTitle", product.getName());

            return "product/product_detail";

        } catch (ResourceNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("search")
    public String searchFirstPage(@RequestParam("keyword") String keyword, Model model) {
        return searchByPage(1, keyword, model);
    }

    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(@PathVariable("pageNum") int pageNum,
                               @RequestParam("keyword") String keyword,
                               Model model) {
        Page<Product> page = productService.searchProducts(pageNum, keyword);

        int startCount = (pageNum - 1) * PRODUCTS_PER_PAGE + 1;
        int endCount = startCount + PRODUCTS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = (int) page.getTotalElements();
        }


        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Tìm kiếm - " + keyword);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listProducts", page.getContent());

        return "product/search_results";
    }
}

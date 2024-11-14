package com.shopme.admin.controller;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.UserDetailsConfig;
import com.shopme.admin.service.BrandService;
import com.shopme.admin.service.CategoryService;
import com.shopme.admin.service.ProductService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.admin.util.ProductSaveHelper;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static com.shopme.admin.service.impl.ProductServiceImpl.PRODUCTS_PER_PAGE;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    @GetMapping()
    public String listAllProducts() {
        return "redirect:/products/page/1?sortField=name&sortDir=asc";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        List<Brand> brands = brandService.getAllBrands();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("totalExtraImages", 0);
        model.addAttribute("product", product);
        model.addAttribute("brands", brands);
        model.addAttribute("pageTitle", "Thêm mới sản phẩm");

        return "products/product_form";
    }

    @PostMapping
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
                              @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
                              @RequestParam(value = "detailIDs", required = false) String[] detailIDs,
                              @RequestParam(value = "detailNames", required = false) String[] detailNames,
                              @RequestParam(value = "detailValues", required = false) String[] detailValues,
                              @RequestParam(value = "imageIDs", required = false) String[] imageIds,
                              @RequestParam(value = "imageNames", required = false) String[] imageNames,
                              @AuthenticationPrincipal UserDetailsConfig loggedUser,
                              RedirectAttributes redirectAttributes) throws IOException {
        if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
            if (loggedUser.hasRole("Salesperson")) {
                productService.saveProductPrice(product);

                redirectAttributes.addFlashAttribute("message", "Sản phẩm được lưu thành công!");
                return "redirect:/products";
            }
        }
        ProductSaveHelper.setMainImageName(mainImageMultipart, product);
        ProductSaveHelper.setExistingExtraImages(imageIds, imageNames, product);
        ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
        setProductDetails(detailIDs, detailNames, detailValues, product);

        Product savedProduct = productService.saveProduct(product);
        ProductSaveHelper.saveUploadImages(mainImageMultipart, extraImageMultiparts, savedProduct);
        ProductSaveHelper.deleteExtraImageWeredRemoveInForm(product);

        redirectAttributes.addFlashAttribute("message", "Sản phẩm được lưu thành công!");

        return "redirect:/products";
    }

    private void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
        if (detailNames == null || detailNames.length == 0) {
            return;
        }

        for (int i = 0; i < detailNames.length; i++) {
            String name = detailNames[i];
            String value = detailValues[i];
            int id = Integer.parseInt(detailIDs[i]);
            if (id != 0) {
                product.addDetail(id, name, value);
            } else if (!name.isEmpty() && !value.isEmpty()) {
                product.addDetail(name, value);
            }
        }
    }

    @GetMapping("/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable("id") Integer id,
                                      @PathVariable("status") boolean enabled,
                                      RedirectAttributes redirectAttributes) {
        productService.updateEnabledStatus(id, enabled);

        String status = enabled ? " được kích hoạt" : " vô hiệu hóa";

        redirectAttributes.addFlashAttribute("message", "Sản phẩm với ID: " + id + status + " thành công!");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        productService.deleteProductById(id);
        String productExtraImagesDir = "../product-images/" + id + "/extras";
        String productImagesDir = "../product-images/" + id;
        FileUploadUtil.removeDir(productExtraImagesDir);
        FileUploadUtil.removeDir(productImagesDir);
        redirectAttributes.addFlashAttribute("message", "Sản phẩm với ID: " + id + " được xóa thành công!");
        return "redirect:/products";
    }

    @GetMapping("/update/{id}")
    public String editProduct(@PathVariable("id") Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal UserDetailsConfig loggedUser) {
        try {
            Product product = productService.findProductById(id);
            List<Brand> brands = brandService.getAllBrands();
            int totalExtraImages = product.getImages().size();

            boolean isReadOnlyForSalePerson = false;

            if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
                if (loggedUser.hasRole("Salesperson")) {
                    isReadOnlyForSalePerson = true;
                }
            }

            model.addAttribute("isReadOnlyForSalePerson", isReadOnlyForSalePerson);
            model.addAttribute("totalExtraImages", totalExtraImages);
            model.addAttribute("brands", brands);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Cập nhật sản phẩm (ID: " + id + ")");

            return "products/product_form";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/detail/{id}")
    public String viewDetailProduct(@PathVariable("id") Integer id,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.findProductById(id);

            model.addAttribute("product", product);

            return "products/product_detail_modal";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("page/{pageNum}")
    public String listProductsByPage(@PathVariable("pageNum") Integer pageNum,
                                     @PagingAndSortingParam(listName = "products") PagingAndSortingHelper helper,
                                     @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                     Model model
    ) {
        Page<Product> page = productService.listByPage(pageNum, helper, categoryId);
        List<Category> categories = categoryService.getAllCategoriesInform();

        helper.updateModelAttribute(pageNum, page);

        model.addAttribute("categories", categories);
        model.addAttribute("categoryId", categoryId);

        return "products/products";
    }
}

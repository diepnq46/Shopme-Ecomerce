package com.shopme.admin.util;

import com.shopme.admin.controller.ProductController;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ProductSaveHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    public static void setMainImageName(MultipartFile mainMultipart, Product product) {
        if (!mainMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainMultipart.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }


    public static void setExistingExtraImages(String[] imageIds, String[] imageNames, Product product) {
        if (imageIds == null || imageIds.length == 0) {
            return;
        }

        Set<ProductImage> images = new HashSet<>();

        for (int i = 0; i < imageIds.length; i++) {
            int id = Integer.parseInt(imageIds[i]);
            String name = imageNames[i];
            ProductImage image = new ProductImage(id, name, product);

            images.add(image);
        }

        product.setImages(images);
    }

    public static void deleteExtraImageWeredRemoveInForm(Product product) {
        String extraImageDir = "../product-images/" + product.getId() + "/extras";
        Path dirPath = Paths.get(extraImageDir);

        try {
            Files.list(dirPath).forEach(file -> {
                String fileName = file.toFile().getName();

                if (!product.containsImageName(fileName)) {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        LOGGER.error("Could not delete extra image:" + fileName);
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.error("Could not list directory:" + extraImageDir);
        }

    }

    public static void saveUploadImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            String uploadDir = "../product-images/" + savedProduct.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
        }

        if (extraImageMultiparts == null) {
            return;
        }

        String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
        for (MultipartFile extraImage : extraImageMultiparts) {
            if (extraImage.isEmpty()) {
                continue;
            }

            String fileName = StringUtils.cleanPath(extraImage.getOriginalFilename());
            FileUploadUtil.saveFile(uploadDir, fileName, extraImage);
        }
    }

    public static void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
        if (extraImageMultiparts == null) {
            return;
        }

        for (MultipartFile extraImage : extraImageMultiparts) {
            if (!extraImage.isEmpty()) {
                String fileName = StringUtils.cleanPath(extraImage.getOriginalFilename());

                if (!product.containsImageName(fileName)) {
                    product.addExtraImage(fileName);
                }
            }
        }
    }
}

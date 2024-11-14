package com.shopme.admin.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

@AllArgsConstructor
public class PagingAndSortingHelper {
    private ModelAndViewContainer model;
    private String listName;

    @Getter
    private String sortField;

    @Getter
    private String sortDir;

    @Getter
    private String keyword;

    public void updateModelAttribute(int pageNum, Page<?> page)  {
        List<?> listObject = page.getContent();
        int totalElements = (int) page.getTotalElements();
        int pageSize = page.getSize();

        int startCount = (pageNum - 1) * pageSize + 1;
        int endCount = startCount + pageSize - 1;
        if (endCount > totalElements) {
            endCount = totalElements;
        }

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", totalElements);
        model.addAttribute(listName, listObject);
    }

}

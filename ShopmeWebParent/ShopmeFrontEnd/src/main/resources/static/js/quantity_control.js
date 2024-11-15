$(document).ready(function () {
    $(".linkMinus").on("click", function (e) {
        e.preventDefault();
        productId = $(this).attr("pid");
        quantityInput = $("#quantity" + productId);
        newQuantity = parseInt(quantityInput.val()) - 1;

        if (newQuantity > 0) {
            quantityInput.val(newQuantity);
        }else {
            showWarningModal("Số lượng nhỏ nhất là 1");
        }
    })
    $(".linkPlus").on("click", function (e) {
        e.preventDefault();
        productId = $(this).attr("pid");
        quantityInput = $("#quantity" + productId);
        newQuantity = parseInt(quantityInput.val()) + 1;

        if (newQuantity <= 5) {
            quantityInput.val(newQuantity);
        }else {
            showWarningModal("Số lượng lớn nhất là 5");
        }
    })
})
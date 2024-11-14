$(document).ready(function () {
    $("#buttonAddToCart").on("click", function () {
        addToCart();
    })
})

function addToCart() {
    quantity = $("#quantity" + productId).val();

    url = contextPath + "carts/add/" + productId + "/" + quantity;

    $.get(url, function (response) {
        showModalDialog("Shopping Cart", response);
    }).fail(function () {
        showErrorModal("Có lỗi khi thêm sản phẩm vào giỏ hàng");
    })
}
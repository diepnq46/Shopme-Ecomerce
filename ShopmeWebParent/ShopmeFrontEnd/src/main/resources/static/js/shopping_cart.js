var quantityInput;
var productId;
decimalSeperator = decimalDigits === 'COMMA' ? ',' : '.';
thousandSeperator = thousandPointType === 'COMMA' ? ',' : '.';
$(document).ready(function () {
    $(".linkMinus").on("click", function (e) {
        e.preventDefault();
        productId = $(this).attr("pid");
        quantityInput = $("#quantity" + productId);
        newQuantity = parseInt(quantityInput.val()) - 1;

        if (newQuantity > 0) {
            quantityInput.val(newQuantity);
            updateQuantity(newQuantity);
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
            updateQuantity(newQuantity);
            quantityInput.val(newQuantity);
        }else {
            showWarningModal("Số lượng lớn nhất là 5");
        }
    })

    $(".linkRemove").on("click", function (e) {
        e.preventDefault();
        removeProduct($(this));
    })
})

function removeProduct(link){
    url = link.attr("href");

    $.ajax({
        url,
        type: "DELETE",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue)
        }
    }).done(function (response) {
        row = link.attr("rowNumber");
        removeProductHTML(row);
        showModalDialog("Thông báo", response);
    })
}

function removeProductHTML(row) {
    $("#row" + row).remove();
    updateTotal();
    updateCountNumbers();
}

function updateCountNumbers() {
    $(".divCount").each(function (index, element) {
        element.innerHTML = "" + (index + 1);
    })
}

function updateQuantity(newQuantity) {
    url = contextPath + "carts/update/" + productId + "/" + newQuantity;
    jsonData = {productId: parseInt(productId), quantity: newQuantity};
    console.log(jsonData);
    $.get(url, function (updatedSubTotal) {
        updateSubTotal(updatedSubTotal);
    })
    .fail(function () {
        showErrorModal("Có lỗi khi cập nhật số lượng");
    })
}

function updateSubTotal(updatedSubTotal) {
    formatSubTotal = $.number(updatedSubTotal, 2);
    $("#subTotal" + productId).text(formatSubTotal);
    updateTotal();
}

function updateTotal() {
    total = 0.0;
    productCount = 0;
    $(".sub-total").each(function (index, element)  {
        productCount++;
        total += parseFloat(clearCurrencyFormat(element.innerHTML));
    })

    if (productCount > 0) {
        $("#total").text(formatPrice(total));
    }else {
        showEmptyShoppingCart();
    }
}

function clearCurrencyFormat(numberString) {
    result = numberString.replaceAll(thousandSeperator, "");

    return result.replaceAll(decimalSeperator, ".");
}

function formatPrice(price) {
    return $.number(price, decimalDigits, decimalSeperator, thousandSeperator);
}

function showEmptyShoppingCart() {
    $("#sectionTotal").hide();
    $("#sectionEmptyCartMessage").removeClass("d-none");
}
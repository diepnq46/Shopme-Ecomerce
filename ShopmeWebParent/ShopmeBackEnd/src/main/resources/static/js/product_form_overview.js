dropdownBrands = $("#brand");
dropdownCategories = $("#category");

$(document).ready(function () {
    $("#shortDescription").richText();
    $("#fullDescription").richText();

    dropdownBrands.change(function () {
        dropdownCategories.empty();
        getCategories();
    })
    getCategoriesInNewForm();

})

function getCategoriesInNewForm() {
    catField = $("#categoryId").val();


    editMode = catField && catField.length;

    if (!editMode) getCategories();
}

function getCategories() {
    brandId = dropdownBrands.val();
    url = urlBrands + "/" + brandId + "/categories";

    $.get(url, function (responeJson) {
        $.each(responeJson, function (index, category) {
            $("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
        })
    })
}

function checkUnique(form) {
    productId = $("#id").val();
    productName = $("#name").val();

    csrfValue = $("input[name='_csrf']").val();

    params = {id: productId, name: productName, _csrf: csrfValue};

    $.post(urlCheckUnique, params, function (response) {
        if (response === "OK") {
            form.submit();
        } else if (response === "Duplicated") {
            showWarningModal("Sản phẩm " + productName + " đã tồn tại!");
        } else {
            showErrorModal("Không xác định được phản hòi từ server!");
        }
    }).fail(function () {
        showErrorModal("Không thể kết nối tới server");
    })
    return false;
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}

function showErrorModal(message) {
    showModalDialog("Error", message);
}

function showWarningModal(message) {
    showModalDialog("Warning", message);
}

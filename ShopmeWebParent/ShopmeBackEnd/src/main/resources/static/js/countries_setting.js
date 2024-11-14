var buttonLoad;
var dropdownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;


$(document).ready(function () {
    buttonLoad = $("#buttonLoadCountries");
    dropdownCountry = $("#dropdownCountries");
    buttonAddCountry = $("#buttonAddCountry");
    buttonUpdateCountry = $("#buttonUpdateCountry");
    buttonDeleteCountry = $("#buttonDeleteCountry");
    labelCountryName = $("#labelCountryName");
    fieldCountryCode = $("#fieldCountryCode");
    fieldCountryName = $("#fieldCountryName");

    buttonLoad.on("click", function () {
        loadCountries();

    })

    dropdownCountry.on("change", function () {
        changFormStateToSelectedCountry();
    })

    buttonAddCountry.click(function () {
        if (buttonAddCountry.val() === "Thêm") {
            addCountry();
        }else {
            changeFormStateToNew();
        }
    })

    buttonUpdateCountry.click(function () {
        updateCountry()
    })

    buttonDeleteCountry.click(function () {
        deleteCountry();
    })
})

function validateFormCountry() {
    formCountry = document.getElementById("formCountry");

    if (!formCountry.checkValidity()) {
        formCountry.reportValidity();
        return false;
    }

    return true;
}

function deleteCountry() {
    countryId = dropdownCountry.val().split("-")[0];
    url = contextPath + "countries/" + countryId;


    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function () {
        $("#dropdownCountries option:selected").remove();
        showToastMessage("Xóa quốc gia thành công!");
        changeFormStateToNew();
    }).fail(function () {
        showToastMessage("ERROR: Có lỗi từ Server!");
    })
}

function updateCountry() {
    if (!validateFormCountry()) return;

    url = contextPath + "countries";
    countryId = dropdownCountry.val().split("-")[0];
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    jsonData = {id:countryId, name: countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: "application/json"
    }).done(function (countryId) {
        $("#dropdownCountries option:selected").val(countryId + "-" + countryCode)
        $("#dropdownCountries option:selected").text(countryName);
        showToastMessage("Cập nhật quốc gia thành công!");
        changeFormStateToNew()
    })
}

function addCountry() {
    if (!validateFormCountry()) return;
    url = contextPath + "countries";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    jsonData = {name: countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: "application/json"
    }).done(function (countryId) {
       showToastMessage("Thêm quốc gia mới thành công!");
       selectedNewlyAddedCountry(countryId, countryName, countryCode);
    })
}

function selectedNewlyAddedCountry(countryId, countryName, countryCode) {
    optionValue = countryCode + "-" + countryName;
    $("<option>").val(optionValue).text(countryName).appendTo(dropdownCountry);

    $("#dropdownCountries option[value='" + optionValue + "']").prop("selected", true);
    buttonAddCountry.prop("value", "Thêm");
    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function changeFormStateToNew() {
    buttonAddCountry.prop("value", "Thêm");
    buttonUpdateCountry.prop("disabled", "disabled");
    buttonDeleteCountry.prop("disabled", "disabled");
    labelCountryName.text("Tên quốc gia:");
    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function loadCountries() {
    url = contextPath + "countries";
    debugger;
    $.get(url, function (responseJson) {
        try {
            console.log(responseJson)
            dropdownCountry.empty();
            $.each(responseJson, function (index, country) {
                optionValue = country.id + "-" + country.code;
                $("<option>").val(optionValue).text(country.name).appendTo(dropdownCountry)
            });
        } catch (error) {
            console.error("Lỗi trong callback:", error);
            showToastMessage("Lỗi khi xử lý dữ liệu từ server!");
        }
    }).done(function () {
        buttonLoad.val("Làm mới danh sách");
        showToastMessage("Tất cả quốc gia đã được tải lên");
    }).fail(function () {
        showToastMessage("ERROR: Không thể kết nối tới server hoặc đã có lỗi với server!");
    })
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast("show");
}

function changFormStateToSelectedCountry() {
     buttonAddCountry.prop("value", "Tạo mới");
     buttonUpdateCountry.prop("disabled", false);
     buttonDeleteCountry.prop("disabled", false);

     labelCountryName.text("Quốc gia đã chọn:");
     selectedCountryName = $("#dropdownCountries option:selected").text();
     fieldCountryName.val(selectedCountryName);

     selectedCountryCode = $("#dropdownCountries option:selected").val().split("-")[1];
     fieldCountryCode.val(selectedCountryCode);
}
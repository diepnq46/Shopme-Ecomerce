var buttonLoadCountriesForState;
var dropdownCountriesForState;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;


$(document).ready(function () {
    buttonLoadCountriesForState = $("#buttonLoadCountriesForStates");
    dropdownCountriesForState = $("#dropdownCountryForStates");
    dropdownState = $("#dropdownSates");
    buttonAddState = $("#buttonAddState");
    buttonUpdateState = $("#buttonUpdateSate");
    buttonDeleteState = $("#buttonDeleteState");
    labelStateName = $("#labelCountryName");
    fieldStateName = $("#fieldStateName");

    buttonLoadCountriesForState.on("click", function () {
        loadCountriesForStates();
    })

    dropdownCountriesForState.on("change", function () {
        debugger;
        loadStates();
    })

    dropdownState.on("change", function () {
        changeFormStateToSelectedState();
    })

    buttonAddState.on("click", function () {
        if (buttonAddState.val() === "Tạo mới") {
            changeFormStateToNewState();
        }else {
            addState();
        }
    })

    buttonUpdateState.on("click", function () {
        updateState();
    })
    buttonDeleteState.on("click", function () {
        deleteState()
    })
})

function validateFormState() {
    formState = document.getElementById("formState");

    if (!formState.checkValidity()) {
        formState.reportValidity();
        return false;
    }

    return true;
}

function deleteState() {
    stateId = dropdownState.val();
    url = contextPath + "states/" + stateId;


    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function () {
        $("#dropdownSates option:selected").remove();
        showToastMessageInState("Xóa tỉnh thành công!");
        changeFormStateToNewState();
    }).fail(function () {
        showToastMessage("ERROR: Có lỗi từ Server!");
    })
}

function updateState() {
    if (!validateFormState()) return;

    url = contextPath + "states";

    selectedCountry = $("#dropdownCountryForStates option:selected")
    countryId = selectedCountry.val().split("-")[0];
    countryName = selectedCountry.text();
    stateName = fieldStateName.val();
    stateId = parseInt($("#dropdownSates option:selected").val());

    jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: "application/json"
    }).done(function () {
        $("#dropdownSates option:selected").text(stateName);

        showToastMessageInState("Cập nhật thông tin tỉnh thành công!");
        changeFormStateToNewState();
    })
}

function addState() {
    if (!validateFormState()) return;

    url = contextPath + "states";
    stateName = fieldStateName.val();
    selectedCountry = $("#dropdownCountryForStates option:selected");
    countryId = selectedCountry.val().split("-")[0];
    countryName = selectedCountry.text();
    jsonData = {name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: "application/json"
    }).done(function (state) {
        showToastMessageInState("Thêm tỉnh mới thành công!");
        selectedNewlyAddedState(state);
    })
}

function selectedNewlyAddedState(state) {
    $("<option>").text(state.name).val(state.id).appendTo(dropdownState);

    $("#dropdownStates option[value='" + state.id + "']").prop("selected", true);
    buttonAddState.prop("value", "Thêm");
    fieldStateName.val("").focus();
    buttonUpdateState.prop("disabled", true);
    buttonDeleteState.prop("disabled", true);
}

function changeFormStateToNewState() {
    buttonAddState.prop("value", "Thêm");
    fieldStateName.val("").focus();
    buttonUpdateState.prop("disabled", true);
    buttonDeleteState.prop("disabled", true);
}
function changeFormStateToSelectedState() {
    buttonAddState.prop("value", "Tạo mới");
    buttonUpdateState.prop("disabled", false);
    buttonDeleteState.prop("disabled", false);

    labelStateName.text("Tỉnh đã chọn:");
    selectedStateName = $("#dropdownSates option:selected").text();
    fieldStateName.val(selectedStateName);
}

function loadStates() {
    countryId = dropdownCountriesForState.val().split("-")[0];
    url = contextPath + "states/list-by-country/" + countryId;

    $.get(url, function (responseJson) {
        dropdownState.empty();

        $.each(responseJson, function (index, state) {
            $("<option>").val(state.id).text(state.name).appendTo(dropdownState);
        })
        showToastMessageInState("Các vùng của quốc gia được tải lên thành công");
    })
}

function loadCountriesForStates() {
    url = contextPath + "countries";
    $.get(url, function (responseJson) {
        dropdownCountriesForState.empty();

        $.each(responseJson, function (index, country) {
            optionValue = country.id + "-" + country.code;
            $("<option>").val(optionValue).text(country.name).appendTo(dropdownCountriesForState)
        })
    }).done(function () {
        buttonLoadCountriesForState.val("Làm mới danh sách");
        showToastMessageInState("Tất cả quốc gia đã được tải lên");
    }).fail(function () {
        showToastMessageInState("ERROR: Không thể kết nối tới server hoặc đã có lỗi với server!");
    })
}

function showToastMessageInState(message) {
    $("#toastMessageState").text(message);
    $(".toast").toast("show");
}

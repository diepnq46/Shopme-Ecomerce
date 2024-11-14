var dropDownCountry;
var dataListStates;

$(document).ready(function () {
    dropDownCountry = $("#country");
    dataListStates = $("#dataListStates");

    dropDownCountry.change(function () {
        loadStatesForCountry();
        $("#state").val("").focus();
    })

    loadStatesForCountry();
})

function checkPasswordMatch(confirmPassword) {
    if (confirmPassword.value !== $("#password").val()) {
        confirmPassword.setCustomValidity("Passwords do not match");
    } else {
        confirmPassword.setCustomValidity("");
    }
}

function loadStatesForCountry() {
    selectedCountry = $("#country option:selected")
    countryId = selectedCountry.val();
    url = contextPath + "states/list-by-country/" + countryId;

    $.get(url, function (responseJson) {
        debugger;
        dataListStates.empty();
        $.each(responseJson, function (index, state) {
            $("<option>").val(state.name).text(state.name).appendTo(dataListStates);
        })
    })
}

function showErrorModal(message) {
    showModalDialog("Error", message);
}

function showWarningModal(message) {
    showModalDialog("Cảnh báo", message)
}

function showDeleteConfirmModal(link, entityName) {
    $("#yesButton").attr("href", link.attr("href"));
    $("#confirmText").text("Bạn chắc chắc muốn xóa " + entityName + " này?");
    $("#confirmModal").modal();
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}
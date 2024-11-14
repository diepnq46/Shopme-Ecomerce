function showDeleteConfirmModal(link, entityName) {
    $("#yesButton").attr("href", link.attr("href"));
    $("#confirmText").text("Bạn chắc chắc muốn xóa " + entityName + " này?");
    $("#confirmModal").modal();
}

function clearFilter() {
    window.location = moduleURL;
}
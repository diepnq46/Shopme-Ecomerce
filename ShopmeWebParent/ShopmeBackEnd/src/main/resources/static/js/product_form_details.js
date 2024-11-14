$(document).ready(function () {
    $("a[name='linkRemoveDetail']").each(function (index) {
        $(this).click(function () {
            removeDetailSectionByIndex(index);
        })
    })
})

function addNextDetailSection() {
    allDivDetails = $("[id^='divDetail']");
    divDetailsCount = allDivDetails.length;


    html = `
        <div class="form-inline" id="divDetail${divDetailsCount}">
            <input type="hidden" name="detailIDs" value="0">
            <label class="m-3">Tên:</label>
            <input type="text" class="form-control w-25" name="detailNames" maxlength="255"/>
            <label class="m-3">Nội dung:</label>
            <input type="text" class="form-control w-25" name="detailValues" maxlength="255"/>
        </div>
    `;


    $("#divProductDetails").append(html);

    previousDivDetailSection = allDivDetails.last();
    previousDivDetailId = previousDivDetailSection.attr("id");

    htmlLinkRemove = `
        <a class="btn fas fa-times-circle fa-2x icon-dark"
            href="javascript:removeDetailSectionById('${previousDivDetailId}')"
            title="Xóa chi tiết này"
        ></a>
    `;

    previousDivDetailSection.append(htmlLinkRemove);
    $("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(divSection) {
    $("#" + divSection).remove();
}

function removeDetailSectionByIndex(index) {
    $("#divDetail" + index).remove();
}
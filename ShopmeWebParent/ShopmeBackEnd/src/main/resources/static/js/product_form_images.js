let extraImagesCount = $("[id^='divExtraImage']").length - 1;

$(document).ready(function () {
    $("input[name='extraImage']").each(function (index) {
        $(this).change(function () {
            if (checkFileSize(this)) {
                showExtraImageThumbnail(this, index);
            }
        })
    })

    $("a[name='linkRemoveExtraImage']").each(function (index) {
        $(this).click(function () {
            removeExtraImage(index);
        })
    })
})

function showExtraImageThumbnail(fileInput, index) {
    var file = fileInput.files[0];

    fileName = file.name;
    imageNameHiddenField = $("#imageName" + index);

    if (imageNameHiddenField.length) {
        imageNameHiddenField.val(fileName);
    }

    var reader = new FileReader();
    reader.onload = function (e) {
        $("#extraThumbnail" + index).attr("src", e.target.result);
    };

    reader.readAsDataURL(file);
    if (index >= extraImagesCount) {
        addNextExtraImageSection(index + 1);
    }

}

function addNextExtraImageSection(index) {
    htmlExtraImage = `
        <div class="col border m-3 p-2" id="divExtraImage${index}">
               <div id="extraImageHeader${index}"><label>Ảnh bổ sung #${index + 1}:</label></div>
               <div>
                   <img id="extraThumbnail${index}" alt="Xem trước ảnh bổ sung #${index + 1}" class="img-fluid" 
                        src="${defaultImageThumbnail}">
                   <input type="file" id="extraImage${index}" name="extraImage" accept="image/png, image/jpeg" 
                        onchange="showExtraImageThumbnail(this, ${index})"
                   >
               </div>
           </div>
    `;
    htmlLinkRemove = `
        <a class="btn fas fa-times-circle fa-2x icon-dark float-right"
            href="javascript:removeExtraImage(${index - 1})"
            title="Xóa ảnh này"
        ></a>
    `;

    $("#divProductImages").append(htmlExtraImage);
    $("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
    extraImagesCount++;
}

function removeExtraImage(index) {
    $("#divExtraImage" + index).remove();
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

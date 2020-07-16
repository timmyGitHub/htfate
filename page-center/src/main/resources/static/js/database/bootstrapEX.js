$(document).ready(function () {
    // input 和 select 组件
    $("#tableData").on("change", ".ht-input-select-db", function () {
        var that = this;
        var originValue = $(that).val();
        $(that).siblings(".ht-input-select-1").val(originValue);
        $(that).siblings(".ht-page-form-type").empty();
    });
    $("#tableData").on("change", ".ht-input-select-form", function () {
        var that = this;
        var originValue = $(that).val();
        var value = originValue.split("(")[0];
        // $(that).siblings(".ht-input-select-1").prop('disabled', false);
        $(that).siblings(".ht-input-select-1").val("");
        if ("radio" === value) {
            $(that).siblings(".ht-input-select-1").attr("placeholder", "例 key:value,key2:value2");
        } else if ("checkbox" === value) {
            $(that).siblings(".ht-input-select-1").attr("placeholder", "例 key:value,key2:value2");
        } else if ('select' === value) {
            $(that).siblings(".ht-input-select-1").attr("placeholder", "例 key:value,key2:value2");
        } else if ('text' === value) {
            $(that).siblings(".ht-input-select-1").attr("placeholder", "默认值");
        } else if ('date' === value) {
            $(that).siblings(".ht-input-select-1").val(YHTUtils.getNowDate("yyyy-MM-dd"));
            $(that).siblings(".ht-input-select-1").attr("placeholder", "yyyy-MM-dd");
        } else if ('datetime' === value) {
            $(that).siblings(".ht-input-select-1").val(YHTUtils.getNowDate("yyyy-MM-dd HH:mm:ss"));
            $(that).siblings(".ht-input-select-1").attr("placeholder", "yyyy-MM-dd HH:mm:ss");
        } else {
            $(that).siblings(".ht-input-select-1").attr("placeholder", originValue+"的默认值");
            // $(that).siblings(".ht-input-select-1").prop('disabled', true);
        }
        $(that).siblings(".ht-page-form-type").empty();
    });

    $("body").on("click", ".ht-tr", function () {
        $(this).toggleClass("table-active");
        var obj = $(this).find(".ht-checkbox");
        if (obj.prop("checked")) {
            obj.prop("checked", false)
        } else {
            obj.prop("checked", true)
        }
        console.log($(this).attr("id"));
    })

});

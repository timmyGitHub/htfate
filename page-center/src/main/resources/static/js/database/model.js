$(document).ready(function () {
    var tableDataObj = $("#tableData");
    // 添加字段
    tableDataObj.on("click", "#addDBField", function () {
        if (vm.dbFieldLength === 0) {
            vm.dbFieldLength = vm.dbField.length;
        }
        // 序号
        vm.dbFieldLength = vm.dbFieldLength + 1;
        // 若点击了某行,就在某行下面添加,否则默认最后一行添加
        var that = this;
        var table = $(that).parents("table");
        var tableId = table.attr("id");
        var htDel = table.find("[name='ht-del']");
        // '页面字段设置' 的添加
        if ("pageTable" === tableId || "dbTable" === tableId) {
            $.each(htDel, function (i) {
                var that2 = $(this);
                if (that2.prop("checked")) {
                    // 添加字段
                    addDBFieldPageField(i);
                    return false;
                }
                if (htDel.length === i + 1) {
                    // 默认 id 后面添加
                    // addDBFieldPageField(htDel.length);
                    addDBFieldPageField(2);
                }
            });
        }
    });
    // 删除字段
    tableDataObj.on("click", "#delDBField", function () {
        if (vm.dbFieldLength === 0) {
            vm.dbFieldLength = vm.dbField.length;
        }
        // 序号
        vm.dbFieldLength = vm.dbFieldLength + 1;

        var that = this;
        var table = $(that).parents(".table").attr("id");
        var obj = $(that).parents(".table").find("[name='ht-del']");
        $.each(obj, function (i) {
            var that = $(this);
            if (that.prop("checked")) {
                that.parent().parent().parent().remove();
                var id = that.parent().parent().parent().attr("id");
                var idLength = id.length;
                var index = id.substring(id.lastIndexOf("-") + 1, idLength);
                console.log(table, index);
                if ("dbTable" === table) {
                    $("#page-table-" + index).remove();
                } else if ("pageTable" === table) {
                    $("#db-table-" + index).remove();
                }
                // 删除校验
                // $("#tableData").bootstrapValidator('removeField',$("#field-name-"+index));
                // $("#tableData").bootstrapValidator('removeField',$("#page-java-variable-"+index));
            }
        })
    });
    // 删除表单
    $(".ht-del-table").on("click", function () {
        var obj = $("#tableInfo").find("[name='ht-del']");
        var tableIdArr = [];
        $.each(obj, function (i) {
            var that = $(this);
            if (that.prop("checked")) {
                var tableId = that.parent().parent().parent().attr("id");
                tableIdArr.push(tableId);
            }
        });
        if (tableIdArr.length > 0) {
            var tipObj = vm.initTip();
            tipObj.messages = "确定删除吗？";
            tipObj.confirmFunction = "delTable";
            tipObj.param = tableIdArr.toString();
            $("#tips").modal("show");
            // vm.delTable(tableIdArr.toString());
        } else {
            var tipObj = vm.initTip();
            tipObj.messages = "请选择一条数据！";
            tipObj.cancel = '好的';
            tipObj.showConfirm = false;
            $('#tips').modal('show');
        }
    });
    // change 事件
    tableDataObj.on("change", ".ht-input-select-1", function () {
        var that = this;
        var value = $(that).siblings("select").val();
        if ("radio" === value) {
            var radioArr = $(that).val().split(",");
            var id = $(that).siblings("select").attr("id");
            var idLength = id.length;
            var index = id.substring(id.lastIndexOf("-") + 1, idLength);
            $(that).siblings("#page-form-type-plus-" + index).empty();
            for (var i = 0; i < radioArr.length; i++) {
                var selectArrElement = radioArr[i];
                var selectObj = selectArrElement.split(':');
                var selectValue, selectName;
                if (1 === selectObj.length) {
                    selectValue = selectName = selectArrElement;
                } else {
                    selectValue = selectObj[0];
                    selectName = selectObj[1];
                }
                $(that).siblings("#page-form-type-plus-" + index).append(
                    "<div class=\"form-check\">" +
                    "    <input class=\"form-check-input\" type=\"radio\" name=\"pageFormRadio-" + index + "\" id=\"page-form-radio-" + i + "-" + index + "\" value=\"" + selectValue + "\">" +
                    "    <label class=\"form-check-label\" for=\"page-form-radio-" + i + "-" + index + "\">" + selectName + "</label>" +
                    "</div>"
                )
            }
        } else if ("checkbox" === value) {
            var checkboxArr = $(that).val().split(",");
            var id = $(that).siblings("select").attr("id");
            var idLength = id.length;
            var index = id.substring(id.lastIndexOf("-") + 1, idLength);
            $(that).siblings("#page-form-type-plus-" + index).empty();
            for (var x = 0; x < checkboxArr.length; x++) {
                var selectArrElement = checkboxArr[x];
                var selectObj = selectArrElement.split(':');
                var selectValue, selectName;
                if (1 === selectObj.length) {
                    selectValue = selectName = selectArrElement;
                } else {
                    selectValue = selectObj[0];
                    selectName = selectObj[1];
                }
                $(that).siblings("#page-form-type-plus-" + index).append(
                    "<div class=\"form-check\">" +
                    "  <input class=\"form-check-input\" type=\"checkbox\" name='pageFormCheckbox-" + index + "' value=\"" + selectValue + "\" id=\"page-form-checkbox-" + x + "-" + index + "\">" +
                    "  <label class=\"form-check-label\" for=\"page-form-checkbox-" + x + "-" + index + "\">" + selectName + "</label>" +
                    "</div>"
                )
            }
        } else if ('select' === value) {
            var selectArr = $(that).val().split(",");
            var id = $(that).siblings("select").attr("id");
            var idLength = id.length;
            var index = id.substring(id.lastIndexOf("-") + 1, idLength);
            $(that).siblings("#page-form-type-plus-" + index).empty();

            var selectOption = '<option value=""></option>';
            for (var i = 0; i < selectArr.length; i++) {
                var selectArrElement = selectArr[i];
                var selectObj = selectArrElement.split(':');
                if (1 === selectObj.length) {
                    selectOption += '<option value="' + selectArrElement + '">' + selectArrElement + '</option>';
                } else {
                    selectOption += '<option value="' + selectObj[0] + '">' + selectObj[1] + '</option>';
                }
            }
            $(that).siblings("#page-form-type-plus-" + index).append('' +
                '<div class="">\n' +
                '    <select class="form-control form-control-sm" id="page-form-select-1-' + index + '" name="pageFormSelect-' + index + '" style="width: 90%;margin-left: 0.7rem;margin-top: 0.5rem;">\n' +
                selectOption +
                '    </select>\n' +
                '  </div>')
        }
        console.log(value);
    });
    // 数据库字段与页面字段绑定
    tableDataObj.on("change", '[name="fieldName"]', function () {
        var that = this;
        var value = $(that).val();
        var id = $(that).parents("tr").attr("id");
        var idLength = id.length;
        var index = id.substring(id.lastIndexOf("-") + 1, idLength);
        var value2 = $("#page-field-name-" + index).val(value);
    });
    tableDataObj.on("change", '[name="fieldRemark"]', function () {
        var that = this;
        var value = $(that).val();
        var id = $(that).parents("tr").attr("id");
        var idLength = $(that).parents("tr").attr("id").length;
        var index = id.substring(id.lastIndexOf("-") + 1, idLength);
        var value2 = $("#page-field-remark-" + index).val(value);
    });
    // 数据库字段与页面字段绑定
    tableDataObj.on("change", '[name="pageFieldName"]', function () {
        var that = this;
        var value = $(that).val();
        var id = $(that).parents("tr").attr("id");
        var idLength = $(that).parents("tr").attr("id").length;
        var index = id.substring(id.lastIndexOf("-") + 1, idLength);
        var value2 = $("#field-name-" + index).val(value);
    });
    tableDataObj.on("change", '[name="pageFieldRemark"]', function () {
        var that = this;
        var value = $(that).val();
        var id = $(that).parents("tr").attr("id");
        var idLength = $(that).parents("tr").attr("id").length;
        var index = id.substring(id.lastIndexOf("-") + 1, idLength);
        var value2 = $("#field-remark-" + index).val(value);
    });
    // 全选按钮
    $("#ht-all-select").on("click", function () {
        var that = this;
        var bRight = $(that).prop("checked");
        if (bRight) {
            $(that).parents("#tableInfo").find("[name='ht-del']").prop("checked", true);
            $(that).parents("#tableInfo").find("[name='ht-del']").parents(".ht-tr").addClass("table-active");
        } else {
            $(that).parents("#tableInfo").find("[name='ht-del']").prop("checked", false);
            $(that).parents("#tableInfo").find("[name='ht-del']").parents(".ht-tr").removeClass("table-active");
        }
    });
    // 表单重置
    $(".modal-footer").on("click", ".ht-table-reset", function () {
        var formInfo = beforeTableInfo();
        // 判断是'添加表单(id 空值)'还是'已有表单(id 不为空)'
        console.log(formInfo.tableInfo.id);
        if ("" === formInfo.tableInfo.id) {
            vm.tableInfo = JSON.parse(JSON.stringify(TABLE_INFO));
            vm.dbField = DB_FIELD.slice(0);
            vm.pageField = PAGE_FIELD.slice(0);
        } else {
            vm.tableInfo = vm.tableInfoTemp;
            vm.dbField = vm.dbFieldTemp;
            vm.pageField = vm.pageFieldTemp;
        }
        // 重新初始化验证，先销毁之后初始化
        tableDataObj.data('bootstrapValidator').destroy();
        formDataValidator(tableDataObj);
    });
    // form 提交 保存
    $(".modal-footer").on("click", ".ht-submit", submitOrUpdate);
    // form 提交 保存更改
    $(".modal-footer").on("click", ".ht-update", submitOrUpdate);
    // 生成代码按钮
    $(".ht-create-code").on("click", function () {
        var tableObjs = $("#tableInfo").find("[name='ht-del']");
        var bRight = true;
        var bReturn = false;
        vm.createCode = [];
        $.each(tableObjs, function (i) {
            var that = $(this);
            if (that.prop("checked")) {
                var tableId = that.parent().parent().parent().attr("id");
                var createCodeTable = {};
                for (i in vm.table) {
                    if (tableId === vm.table[i].id) {
                        createCodeTable["tableId"] = vm.table[i].id;
                        createCodeTable["tableName"] = vm.table[i].tableName;
                        createCodeTable["tableRemark"] = vm.table[i].tableRemark;
                        if (0 === vm.table[i].dbFlag) {
                            var tip = vm.initTip();
                            tip.messages = vm.table[i].tableName + " 表数据库未创建！";
                            tip.showConfirm = false;
                            tip.cancel = '好的';
                            createCodeTable = {};
                            bRight = false;
                            bReturn = true;
                            $('#tips').modal('show');
                            return false;
                        }
                        if (1 === vm.table[i].dbFlag) {
                            var tip = vm.initTip();
                            tip.messages = vm.table[i].tableName + " 表数据库未同步！";
                            tip.showConfirm = false;
                            tip.cancel = '好的';
                            createCodeTable = {};
                            bRight = false;
                            $('#tips').modal('show');
                            return false;
                            bReturn = true;
                        }
                        createCodeTable["dbFlag"] = vm.table[i].dbFlag;
                        createCodeTable["tableAuthor"] = "";
                        vm.createCode.push(createCodeTable);
                        createCodeTable = {};
                        bRight = false;
                    }
                }
            }
        });
        if (bReturn) {
            return false;
        }
        if (bRight) {
            var tip = vm.initTip();
            tip.messages = "请选择一条数据！";
            tip.showConfirm = false;
            tip.cancel = "好的";
            $("#tips").modal('show');
        } else {
            $('#createCodeModal').modal('show');
        }
    });
    // form 表单数据校验初始化
    formDataValidator(tableDataObj);
    // css 样式
    $("body").on('change', '[name="ht-del"]', function () {
        $(this).parent().parent().parent().toggleClass("table-active");
    })
});

/**
 * form 表单数据校验
 * @param obj 表单对象
 */
function formDataValidator(obj) {
    obj.bootstrapValidator({
        fields: {
            tableName: {
                validators: {
                    notEmpty: {
                        message: 'Cannot be empty!'
                    }
                }
            },
            tableRemark: {
                validators: {
                    notEmpty: {
                        message: 'Cannot be empty!'
                    }
                }
            },
            tableType: {
                validators: {
                    notEmpty: {
                        message: 'Cannot be empty!'
                    }
                }
            },
            clazzName: {
                validators: {
                    notEmpty: {
                        message: 'Cannot be empty!'
                    }
                }
            }
        }
    });
}

/**
 * 添加 数据库字段和页面字段
 * @param start 从哪个位置开始添加
 */
function addDBFieldPageField(start) {
    console.log("add field");
    // 添加一个字段,之前的数据会被刷新，所以需要将之前的数据保存
    var info = beforeTableInfo();
    vm.tableInfo = JSON.parse(JSON.stringify(info.tableInfo));
    vm.dbField = JSON.parse(JSON.stringify(info.fieldArr));
    vm.pageField = JSON.parse(JSON.stringify(info.formTypeArr));
    // 页面字段添加
    console.log(info)
    vm.pageField.splice(start, 0, {
        "pageFieldName": "",
        "pageFieldRemark": "",
        "pageJavaVariable": "",
        "pageJavaType": "String",
        "pageFormValue": "",
        "pageFormType": "text",
        "defaultValue": "",
        "pageFormVerify": JSON.parse(JSON.stringify(PAGE_VERIFY)),
        "pagePlaceholder": ""
    });
    // 数据库字段也需要添加
    vm.dbField.splice(start, 0, {
        "fieldDisplay": "",
        "fieldName": "",
        "fieldRemark": "",
        "fieldType": "varchar(32)",
        "fieldKey": ""
    });
}

/**
 * 封装表单数据
 * @returns {{tableInfo: {}, fieldArr: Array, formTypeArr: Array}}
 */
function beforeTableInfo() {
    var tableDataObj = $("#tableData");
    var tableInfo = {};         // 表信息                  重要（数据库表信息）
    var tableFieldInfo = {};    // 表字段信息               重要（数据库个个字段信息）
    var fieldArr = [];          // 字段数组
    var javaFieldInfo = {};     // Java 成员变量信息
    var javaFieldArr = [];      // Java 成员变量信息 数组    重要（Java 成员）
    var formTypeInfo = {};      // form 表单字段信息
    var formTypeArr = [];       // form 表单字段信息 数组    重要（页面 form 表单字段）
    var formDefaultValueArr = [];//form checkbox 默认值
    var formVerifyArr = [];
    var data = tableDataObj.serializeArray();
    console.log(data);
    // 遍历将数据提取出来
    $.each(data, function (i) {
        var that = this;
        var thatname = that.name.split('-')[0];
        switch (thatname) {
            // 查找关于表的信息
            case "tableId" :
                tableInfo["id"] = that.value;
                break;
            case "tableName" :
                tableInfo["tableName"] = that.value;
                break;
            case "tableRemark" :
                tableInfo["tableRemark"] = that.value;
                break;
            case "tableType" :
                tableInfo["tableType"] = that.value;
                break;
            case "clazzName" :
                tableInfo["clazzName"] = that.value;
                break;
            // 查找关于表的信息
            case "fieldDisplay":
                tableFieldInfo['fieldDisplay'] = that.value;
                break;
            case "fieldName" :
                tableFieldInfo["fieldName"] = that.value;
                break;
            case "fieldRemark" :
                tableFieldInfo["fieldRemark"] = that.value;
                break;
            case "fieldType" :
                tableFieldInfo["fieldType"] = that.value;
                // 判断是否下一个字段,若是则将当前的字段信息插入数组
                if ("fieldKey" !== data[i + 1].name) {
                    tableFieldInfo["fieldKey"] = "false";
                    // 一个字段的信息查找完毕,插入数组
                    fieldArr.push(tableFieldInfo);
                    tableFieldInfo = {};
                }
                break;
            case "fieldKey" :
                tableFieldInfo["fieldKey"] = "true";
                // 一个字段的信息查找完毕,插入数组
                fieldArr.push(tableFieldInfo);
                tableFieldInfo = {};
                break;
            // 查找关于 Java 变量 (暂时不需要)
            /* case "pageJavaVariable" :
                 javaFieldInfo[that.name] = that.value;
                 break;
             case "pageJavaType" :
                 javaFieldInfo[that.name] = that.value;
                 javaFieldArr.push(javaFieldInfo);
                 javaFieldInfo = {};
                 break;*/
            // 查找关于 form 表单字段信息
            case "pageFormValue" :
                formTypeInfo[that.name] = that.value;
                break;
            case "pageFormType" :
                formTypeInfo[that.name] = that.value;
                if ((i + 1) < data.length) {
                    var subStr = data[i + 1].name;
                    console.log(subStr);
                    var lastNum = subStr.lastIndexOf("-");
                    if (lastNum > 0) {
                        subStr = subStr.substring(0, lastNum);
                    }
                    if ("pageFormRadio" === subStr) {
                        formTypeInfo["defaultValue"] = data[i + 1].value;

                    } else if ("pageFormCheckbox" === subStr) {
                        // formDefaultValueArr.push(data[i + 1].value);
                        findCheckboxDefaultValue(i, formDefaultValueArr, data);
                        formTypeInfo["defaultValue"] = formDefaultValueArr;
                        formDefaultValueArr = [];
                    } else if ("pageFormSelect" === subStr) {
                        formTypeInfo["defaultValue"] = data[i + 1].value;
                    } else {
                        formTypeInfo["defaultValue"] = "";
                    }
                } else {
                    formTypeInfo["defaultValue"] = "";
                }
                break;
            case 'pageFormVerify':
                if (YHTUtils.isEmpty(formTypeInfo[thatname])) {
                    findVerify(i,'pageFormVerify', data,formVerifyArr);
                    formTypeInfo[thatname] = formVerifyArr.join(',');
                    formVerifyArr = [];
                }
                break;
            case 'pageFormPlaceholder':
                formTypeInfo[thatname] = that.value;
                formTypeArr.push(formTypeInfo);
                formTypeInfo = {};
                break;
            default :
        }
    });
    console.log(JSON.stringify(tableInfo), JSON.stringify(fieldArr), JSON.stringify(formTypeArr));
    return {"tableInfo": tableInfo, "fieldArr": fieldArr, "formTypeArr": formTypeArr};
}

function findVerify(n,name, data,arr) {
    for (var i=n; i<data.length; i++) {
        if (name === data[i]['name'].split('-')[0]) {
            arr.push(data[i]['value']);
        } else {
            break;
        }
    }
}

/**
 * 提交 form 表单数据
 * @param event 当前对象
 */
function submitOrUpdate(event) {
    YHTUtils.showLoading();
    var submit = this;
    $(submit).attr("disabled", true);
    var tableId = $("[name='tableId']").attr("id");
    tableId = '' === tableId ? 0 : tableId;
    // 表单验证
    var tableDataObj = $("#tableData");
    var bootstrapValid = tableDataObj.data("bootstrapValidator");
    // 验证
    bootstrapValid.validate();
    if (bootstrapValid.isValid()) {
        // 获取 form 表单数据
        var formData = beforeTableInfo();
        console.log(JSON.stringify(formData.tableInfo), JSON.stringify(formData.fieldArr), JSON.stringify(formData.formTypeArr));
        Vue.http.post(MODEL_SAVE_URL + "/" + tableId, {
            "tableInfo": JSON.stringify(formData.tableInfo),
            "fieldArr": JSON.stringify(formData.fieldArr),
            "formArr": JSON.stringify(formData.formTypeArr)
        }).then(function (res) {
            var data = res.data;
            if (0 === data.code) {
                // 提交成功后表单重置
                // vm.tableInfo = TABLE_INFO;
                // vm.dbField = DB_FIELD;
                // vm.pageField = PAGE_FIELD;
                $(submit).removeAttr("disabled");
                $("#addTable").modal("hide");
                vm.getTable();
                YHTUtils.hideLoading();
            } else {
                $(submit).removeAttr("disabled");
                console.log(data.msg);
                YHTUtils.hideLoading();
            }
        }, function (reason) {
            $(submit).removeAttr("disabled");
            console.log("request error...")
            YHTUtils.hideLoading();
        });
    } else {
        $(submit).removeAttr("disabled");
        YHTUtils.hideLoading();
    }
}

/**
 * 递归找出 checkbox 默认值
 * @param index
 * @param formDefaultValueArr
 * @param data
 */
function findCheckboxDefaultValue(index, formDefaultValueArr, data) {
    if (index + 1 > data.length) {
        return formDefaultValueArr;
    }
    var subStr = data[index + 1];
    var lastNum = subStr.name.lastIndexOf("-");
    var sub;
    if (lastNum > 0) {
        sub = subStr.name.substring(0, lastNum);
    }
    if ("pageFormCheckbox" === sub) {
        formDefaultValueArr.push(subStr.value);
        return findCheckboxDefaultValue(index + 1, formDefaultValueArr, data);
    } else {
        return formDefaultValueArr;
    }
}


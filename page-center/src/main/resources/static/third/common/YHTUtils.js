// noinspection JSUnusedLocalSymbols,JSClosureCompilerSyntax
YHTUtils = {
    /* ------------------------------------------------ 变量 --------------------------- */
    'tipEdgeCount': 0
    /**
     * 下载文件
     * */
    , 'download': function (url) {
        // window.open(url, "_blank");
        if (url) {
            var i = url.lastIndexOf("/");
            var n = url.lastIndexOf('.');
            var fileName = url.substring(i + 1,n);
            var a = document.createElement('a');
            a.href = url; //图片地址
            a.download = fileName; //图片名及格式
            console.log(url,fileName);
            document.body.appendChild(a);
            a.click();
        }

    }
    /**
     * 判断是否为空
     * true-空 false-不为空
     * @return {boolean}
     */
    , "isEmpty": function (o) {
        return typeof (o) === "undefined"
            || JSON.stringify(o).trim() === JSON.stringify("")
            || JSON.stringify(o).trim() === JSON.stringify("null")
            || o === JSON.stringify("")
            || o === JSON.stringify("null")
            || o === ''
            || o === 'null'
            || o === null;
    },
    /**
     * 判断是否不为空
     * true-不为空 false-为空
     * @return {boolean}
     */
    "isNotEmpty": function (o) {
        return !(YHTUtils.isEmpty(o));
    },
    /**
     * 获取地址栏参数
     */
    "getUrlParam": function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            // noinspection JSDeprecatedSymbols
            return unescape(r[2]);
        }
        return "null";
    },
    /**
     * 设置cookie
     * @param name name
     * @param value value
     * @param days 日期
     */
    "setCookie": function (name, value, days) {
        var d = new Date();
        d.setTime(d.getTime() + (days * 24 * 60 * 60 * 1000));
        // noinspection JSUnresolvedFunction
        var expires = "expires= " + d.toGMTString();
        document.cookie = name + "=" + value + ";" + expires + ";" + "path=/;";
    },
    /**
     * 获取cookie
     * @param name name
     * @returns {string}
     */
    "getCookie": function (name) {
        var cname = name + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i].trim();
            if (c.indexOf(cname) === 0) {
                return c.substring(cname.length, c.length);
            }
        }
        return "";
    },
    /**
     * 将地图坐标转成 百度地图 point
     * @param arr   坐标数组
     * @returns {Array} point
     */
    "polygonCoord": function (arr) {
        if (Array.isArray(arr)) {
            var tem = [];
            for (var i = 0; i < arr['length']; i++) {
                // noinspection JSUnresolvedVariable
                tem.push(new BMap.Point(arr[i][0], arr[i][1]));
            }
            return tem;
        } else {
            throw "参数类型不符!"
        }

    },
    /**
     * 获取form表单数据,返回的json字符串，后台需要
     * @param id formID，如果没有默认form
     * @returns {string} json字符串
     */
    "formData": function (id) {
        var data;
        var jsonData = {};
        if (YHTUtils.isNotEmpty(id)) {
            data = $("#" + id).serializeArray();
            $.each(data, function () {
                jsonData [this.name] = this.value;
            });
            // noinspection UnnecessaryLocalVariableJS
            var jsonStr = JSON.stringify(jsonData);
            return jsonStr;
        } else {
            throw "参数为空!"
        }

    },
    /**
     * 随机产生 int 数字,
     * 若不传参,0-10的随机数
     * 若只传一个参,默认从 0 开始!
     * @param start 从哪个数字开始(包括自己)
     * @param end 从哪结束(包括自己)
     */
    "randomInt": function (start, end) {
        if (YHTUtils.isNotEmpty(start) && YHTUtils.isNotEmpty(end)) {
            if (end - start > 0) {
                return ((Math.ceil(Math.random().toFixed(1) * (end - start)) + start));
            } else if (start - end > 0) {
                return ((Math.ceil(Math.random().toFixed(1) * (start - end)) + end));
            } else {
                throw "参数相等!"
            }
        } else if (YHTUtils.isEmpty(start) && YHTUtils.isNotEmpty(end)) {
            return ((Math.ceil(Math.random().toFixed(1) * end)));
        } else if (YHTUtils.isEmpty(end) && YHTUtils.isNotEmpty(start)) {
            return ((Math.ceil(Math.random().toFixed(1) * start)));
        } else if (YHTUtils.isEmpty(start) && YHTUtils.isEmpty(end)) {
            return ((Math.ceil(Math.random().toFixed(1) * 10)));
        } else {
            throw "null"
        }
    },
    "formatData": function (num, n) {
        if (YHTUtils.isEmpty(n)) {
            n = 10
        }
        var m = Math.pow(10, n);
        return parseInt(num * m) / m
    },
    /**
     * 获取配置文件的信息
     * @param key key值
     */
    "getProperties": function (key) {
        // noinspection JSUnresolvedVariable
        jQuery.i18n.properties({
            name: 'application-dev',
            path: '../../',
            mode: 'map',
            callback: function () {
                // noinspection JSUnresolvedVariable
                return $.i18n.prop(key);
            }
        })
    },
    /**
     * 获取当前时间
     * @param dateType 要转的格式, 默认:yyyy-MM-dd HH:mm:ss
     * @returns {string} 返回日期
     */
    'getNowDate': function (dateType) {
        var d = new Date();
        var s = '';
        if (YHTUtils.isNotEmpty(dateType)) {
            var lasty = dateType.lastIndexOf('y');
            var indexM = dateType.indexOf('M');
            var lastM = dateType.lastIndexOf('M');
            var indexd = dateType.indexOf('d');
            var lastd = dateType.lastIndexOf('d');
            var indexH = dateType.indexOf('H');
            var lastH = dateType.lastIndexOf('H');
            var indexm = dateType.indexOf('m');
            var lastm = dateType.lastIndexOf('m');
            var indexs = dateType.indexOf('s');

            var suby = dateType.substring(lasty + 1, indexM);
            var subM = dateType.substring(lastM + 1, indexd);
            var subd = dateType.substring(lastd + 1, indexH);
            var subH = dateType.substring(lastH + 1, indexm);
            var subm = dateType.substring(lastm + 1, indexs);

            if (-1 !== indexH && -1 !== indexm && -1 !== indexs) {
                s += d.getFullYear() + suby +
                    (d.getMonth() + 1) + subM + d.getDate() + subd +
                    d.getHours() + subH +
                    d.getMinutes() + subm +
                    d.getSeconds();
            } else {
                s += d.getFullYear() + suby +
                    (d.getMonth() + 1) + subM + d.getDate();
            }


            return s;
        } else {
            s = d.getFullYear() + '-' +
                (d.getMonth() + 1) + '-' +
                d.getDate() + ' ' +
                d.getHours() + ':' +
                d.getMinutes() + ':' +
                d.getSeconds();
            return s;
        }
    },
    /**
     * 日期转字符串
     * @param date 日期
     * @param dateType 可选,格式
     * @returns {string} 默认:yyyy-MM-dd HH:mm:ss
     */
    'dateToString': function (date, dateType) {
        var d = new Date(date);
        var s = '';
        if (YHTUtils.isNotEmpty(dateType)) {
            var lasty = dateType.lastIndexOf('y');
            var indexM = dateType.indexOf('M');
            var lastM = dateType.lastIndexOf('M');
            var indexd = dateType.indexOf('d');
            var lastd = dateType.lastIndexOf('d');
            var indexH = dateType.indexOf('H');
            var lastH = dateType.lastIndexOf('H');
            var indexm = dateType.indexOf('m');
            var lastm = dateType.lastIndexOf('m');
            var indexs = dateType.indexOf('s');

            var suby = dateType.substring(lasty + 1, indexM);
            var subM = dateType.substring(lastM + 1, indexd);
            var subd = dateType.substring(lastd + 1, indexH);
            var subH = dateType.substring(lastH + 1, indexm);
            var subm = dateType.substring(lastm + 1, indexs);

            s = d.getFullYear() + suby +
                (d.getMonth() + 1) + subM +
                d.getDate() + subd +
                d.getHours() + subH +
                d.getMinutes() + subm +
                d.getSeconds();
            return s;
        } else {
            s = d.getFullYear() + '-' +
                (d.getMonth() + 1) + '-' +
                d.getDate() + ' ' +
                d.getHours() + ':' +
                d.getMinutes() + ':' +
                d.getSeconds();
            return s;
        }
    },
    /**
     * 获取日期的前几天或者会几天
     * @param type d-天 M-月 y-年
     * @param num 整数
     * @param date 日期
     * @param dateType 格式
     * @returns {*|string} 默认:yyyy-MM-dd HH:mm:ss
     */
    'otherDay': function (type, num, date, dateType) {
        var d;
        if (YHTUtils.isNotEmpty(date)) {
            d = new Date(date);
            if ('d' === type) {
                return YHTUtils.dateToString(d.getTime() + (num * 24 * 60 * 60 * 1000));
            } else if ('M' === type) {
                return YHTUtils.dateToString(d.getTime() + (num * 30 * 24 * 60 * 1000));
            } else if ('y' === type) {
                return YHTUtils.dateToString(d.getTime() + (num * 365 * 24 * 60 * 60 * 1000));
            }
        } else {
            d = new Date();
            if ('d' === type) {
                return YHTUtils.dateToString(d.getTime() + (num * 24 * 60 * 60 * 1000));
            } else if ('M' === type) {
                return YHTUtils.dateToString(d.getTime() + (num * 30 * 24 * 60 * 1000));
            } else if ('y' === type) {
                return YHTUtils.dateToString(d.getTime() + (num * 365 * 24 * 60 * 60 * 1000));
            }
        }
    },
    /**
     * 弹出提示框，设置时间后隐藏
     * @param id    元素id
     * @param time  毫秒
     */
    "SetTimeShowBox": function (id, time) {
        if (YHTUtils.isNotEmpty(id)) {
            if (YHTUtils.isEmpty(time)) {
                time = 3000;
            }
            setTimeout(function () {
                var obj = $("#" + id);
                obj.popover("update");
                obj.popover("show");
                setTimeout(function () {
                    $("#" + id).popover("hide");
                }, time);
            });
        }
    },
    /*------------------------------------------------------------ 字体图标 ----------------------------*/
    // 图标开始旋转
    'startSpin': function (e) {
        if (YHTUtils.isNotEmpty(e)) {
            $(e.target).addClass('fa-spin');
            $(e).addClass('fa-spin');

        }
    },
    // 图标停止旋转
    'stopSpin': function (e) {
        if (YHTUtils.isNotEmpty(e)) {
            $(e.target).removeClass('fa-spin');
            $(e).removeClass('fa-spin');
        }
    },
    // 添加加载图标
    'showLoading': function () {
        $('body').append('<div id="showLoading" class="show-loading row"' +
            '     style="top: 0px; left: 0px; position: fixed; width: 103%; height: 100%; opacity: 0.5; z-index: 60000; background-color: rgb(0, 0, 0);">' +
            '<div class="col align-self-center"><span id="bgColor" class="row justify-content-center" style="z-index: 60001; color: white;"><i' +
            '            class="fa fa-spinner fa-spin fa-10x"></i></span></div>' +
            '</div>');
    },
    'showElement': function (id) {
        $('#' + id).css({'display': 'block'});
    },
    // 移除加载图标
    'hideLoading': function () {
        $('#showLoading').remove();
    },
    'hideElement': function (id) {
        $('#' + id).css({'display': 'none'});
    },
    /* ------------------------------------------------------ 提示框 ------------------------------------------- */
    /**
     * 自定义侧边栏提示框
     * @param obj {"messages":"自定义侧边栏提示框！",
              "clazz": "success",// primary secondary success danger warning info dark
              "time":3000
             }
     */
    'showTipEdge': function (obj) {
        if (!obj) {
            obj = {}
        }
        YHTUtils.tipEdgeCount += 1;
        var div = '<div id="tipEdge' + YHTUtils.tipEdgeCount + '" class="alert alert-';
        if (YHTUtils.isEmpty(obj['clazz'])) {
            obj['clazz'] = 'success';
        }
        div += obj["clazz"] + ' alert-dismissible fade show" role="alert">' +
            '            <strong>';
        if (YHTUtils.isEmpty(obj['messages'])) {
            obj['messages'] = "Hello,I'm Timmy";
        }
        div += obj['messages'] + '</strong>' +
            '            <button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
            '                <span aria-hidden="true">&times;</span>' +
            '            </button>' +
            '        </div>';
        $('.tip-edge').append(div);
        var tipObj = '#tipEdge' + YHTUtils.tipEdgeCount;
        // 设置默认显示的时间
        if (YHTUtils.isEmpty(obj['time'])) {
            obj['time'] = 3000;
        }
        setTimeout(function () {
            $(tipObj).alert('close');
            YHTUtils.tipEdgeCount -= 1;
        }, obj['time']);
        return obj['messages'];
    },
    /* --------------------------------------------------------- 回车键监听   ----------------------------------------*/
    'enterKey': function (fucName, fucParam) {
        var event = arguments.callee.caller.arguments[0] || window.event;
        if (event.keyCode === 13) {
            eval(fucName + '(' + fucParam + ')');
        }
    },
    /* --------------------------------------------------------- 自制控件 --------------------------------------------*/
    /**
     * 自制 分页 控件
     * 说明，调用之前应该先 判断参数是否为对象，不是对象则转对象，否则报错
     * @param fucName 调用 分页 的方法名
     * @param fucParam 格式：{id: 放在哪，total: 条数, pageIndex: 第几页, pageSize: 一页多少条,pageNum: 显示几个按钮 , other: 其它参数}
     */
    'pagination': function (fucName, fucParam) {
        if (typeof (eval(fucName)) !== 'function') {
            throw '不存在的方法名';
        }

        var showPageNum = fucParam['pageNum'];    // 要显示几页，默认显示5个
        if (YHTUtils.isEmpty(showPageNum)) {
            showPageNum = 5;
        }

        var appendObj;
        var id = fucParam['id'];

        // noinspection JSJQueryEfficiency
        if (typeof $('#YHTPagination').attr('id') === 'undefined') {
            appendObj = $('#' + id);
        } else {
            $('#YHTPagination').remove();
            appendObj = $('#' + id);
        }

        var pageIndex = fucParam['pageIndex'];
        var pageSize = fucParam['pageSize'];
        var total = fucParam['total'];

        if (pageSize <= 0) {
            pageSize = 15;
        }
        if (total < 0) {
            total = 0;
        }
        var totalPage = Math.ceil(total / pageSize);

        if (totalPage < showPageNum) {
            showPageNum = totalPage;
        }

        if (pageIndex < 0) {
            pageIndex = 1;
        } else if (pageIndex > totalPage) {
            pageIndex = 1;
        }

        var paginationHtml = '<div id="YHTPagination" class="container">' +
            '        <div class="row mt-2">' +
            '            <div class="col-2">' +
            '                <button type="button" class="btn btn-dark">共' + totalPage + '页</button>' +
            '            </div>' +
            '            <div class="col-10">' +
            '                <div class="btn-toolbar float-right" role="toolbar" aria-label="Toolbar with button groups">' +
            '                    <div class="btn-group mr-2" role="group" aria-label="First group">' +
            '                        <button type="button" class="btn btn-dark" ';
        // 首页
        if (pageIndex !== 1 && 0 !== totalPage) {
            fucParam['pageIndex'] = 1;
            paginationHtml += 'onclick=' + fucName + '(\'' + JSON.stringify(fucParam) + '\')';
        }

        paginationHtml += '>首页</button>' +
            '                    </div>' +
            '                    <div class="btn-group" role="group" aria-label="Fourth group">' +
            '                        <button type="button" class="btn btn-dark" ';
        // 上一页
        if (pageIndex - 1 >= 1) {
            fucParam['pageIndex'] = pageIndex - 1;
            paginationHtml += 'onclick=' + fucName + '(\'' + JSON.stringify(fucParam) + '\')';
        }
        paginationHtml += '><span aria-hidden="true">&laquo;</span></button>' +
            '                    </div>' +
            '                    <div class="btn-group" role="group" aria-label="Second group">';
        // 中间页数
        // 中间数
        var middle = Math.ceil(showPageNum / 2);
        var addPagePre = 0;
        var addPageAft = 0;

        // 中间页后面的，若不够添加到前面
        for (var i = middle, n = 1; i < showPageNum; i++, n++) {// 3,4
            if (!(pageIndex + n <= totalPage)) {// 3,2
                ++addPageAft;
            }
        }
        // 中间页 前面的，若不够则添加到后面
        var witchPage = pageIndex;
        if (middle === 1) {
            witchPage = 0;
        }

        for (var i = 1; i < middle; i++) {//1,2
            if (pageIndex - i > 0) {// 2,1
                witchPage = (pageIndex - middle) > 0 ? pageIndex - middle - addPageAft + i : i;
                paginationHtml += '<button type="button" class="btn btn-dark" ';
                fucParam['pageIndex'] = witchPage;
                paginationHtml += 'onclick=' + fucName + '(\'' + JSON.stringify(fucParam) + '\')';
                paginationHtml += '>' + witchPage + '</button>';
            } else {
                ++addPagePre;
            }
        }
        if (0 !== addPageAft) {
            for (var i = 1; i <= addPageAft; i++) {
                witchPage += 1;
                paginationHtml += '<button type="button" class="btn btn-dark" ';
                fucParam['pageIndex'] = witchPage;
                paginationHtml += 'onclick=' + fucName + '(\'' + JSON.stringify(fucParam) + '\')';
                paginationHtml += '>' + witchPage + '</button>';
            }
        }

        if (0 === totalPage) {
            paginationHtml += '<button type="button" class="btn btn-dark btn-outline-dark">没有数据</button>';
        } else {
            paginationHtml += '<button type="button" class="btn btn-dark btn-outline-dark">' + pageIndex + '</button>';
        }
        witchPage = pageIndex;
        if (0 !== addPagePre) {
            for (var i = 1; i <= addPagePre; i++) {
                witchPage += 1;
                paginationHtml += '<button type="button" class="btn btn-dark" ';
                fucParam['pageIndex'] = witchPage;
                paginationHtml += 'onclick=' + fucName + '(\'' + JSON.stringify(fucParam) + '\')';
                paginationHtml += '>' + witchPage + '</button>';
            }
        }


        // 中间页后面的
        for (var i = middle, n = 1; i < showPageNum; i++, n++) {// 3,4
            if (pageIndex + n <= totalPage) {// 3,2
                witchPage += 1;
                paginationHtml += '<button type="button" class="btn btn-dark" ';
                fucParam['pageIndex'] = witchPage;
                paginationHtml += 'onclick=' + fucName + '(\'' + JSON.stringify(fucParam) + '\')';
                paginationHtml += '>' + witchPage + '</button>';
            } else {
                ++addPageAft;
            }
        }
        paginationHtml += '</div>' +
            '                    <div class="btn-group mr-2" role="group" aria-label="Fourth group">' +
            '                        <button type="button" class="btn btn-dark" ';
        // 下一页
        if (pageIndex + 1 <= totalPage) {
            fucParam['pageIndex'] = pageIndex + 1;
            paginationHtml += 'onclick=' + fucName + '(\'' + JSON.stringify(fucParam) + '\')';
        }
        paginationHtml += '><span aria-hidden="true">&raquo;</span></button>' +
            '                    </div>' +
            '                    <div class="btn-group" role="group" aria-label="Third group">' +
            '                        <button type="button" class="btn btn-dark" ';

        // 跳转
        var fucParamStr = JSON.stringify(fucParam);
        fucParamStr = fucParamStr.substring(0, fucParamStr.length - 1);
        fucParamStr = fucParamStr.replace(/"/g, '\\"');
        paginationHtml += 'onclick=\'var jumPage = $(\"#jumpPage\").val();' +
            'if (jumPage === \"\"){jumPage=1}' +
            'else if (jumPage > ' + totalPage + '){jumPage=' + totalPage + '}' +
            'else if (jumPage < 0){jumPage=1}' +
            'var fucParamStr=\"' + fucParamStr + '\"+\",\\\"pageIndex\\\":\"+jumPage+\"}\";' +
            'eval(\"' + fucName + '\"+\"(fucParamStr)\");\'';

        paginationHtml += '>跳转</button>' +
            '<input id="jumpPage" type="text" class="yht-w-2" onkeydown=\'var jumPage = $(\"#jumpPage\").val();' +
            'if (jumPage === \"\"){jumPage=1}' +
            'else if (jumPage > ' + totalPage + '){jumPage=' + totalPage + '}' +
            'else if (jumPage < 0){jumPage=1}' +
            'var fucParamStr=\"' + fucParamStr + '\"+\",\\\"pageIndex\\\":\"+jumPage+\"}\";' +
            'YHTUtils.paginationEnterPress(\"' + fucName + '\",fucParamStr);\'' +
            '</div>' +
            '<div class="btn-group" role="group" aria-label="Fourth group">' +
            '<button type="button" class="btn btn-dark" ';
        // 尾页
        if (pageIndex !== totalPage && 0 !== totalPage) {
            fucParam['pageIndex'] = totalPage;
            paginationHtml += 'onclick=' + fucName + '(\'' + JSON.stringify(fucParam) + '\')';
        }

        paginationHtml += '>尾页</button>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>';

        appendObj.append(paginationHtml);
    },
    /**
     * 分页监听事件
     * @param fucname       调用分页的方法名
     * @param fucParamStr   参数
     */
    'paginationEnterPress': function (fucname, fucParamStr) {
        var event = arguments.callee.caller.arguments[0] || window.event;
        if (event.keyCode === 13) {
            eval(fucname + '(\'' + fucParamStr + '\')');
        }
    },
    'copy':function (text) {
        createTextArea(text);
        selectText();
        copyToClipboard();
    },
    /* --------------------------------------------------------- 本 js 的初始化数据 -----------------------------------*/
    'init': function () {
        // 侧边栏提示框容器
        var tipDiv = '<div class="tip-edge" style="z-index: 59999;top: 70px;width: 40%;position: fixed;right: 0px"></div>';
        $('body').append(tipDiv);
        console.log("%c YHTUtils Finished", 'font-size: 36px;font-family: fantasy;color: cornflowerblue;');
    }

};
$(document).ready(function () {
    YHTUtils.init();
});
// 去掉左右空白
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};
// 去掉左边空格
// noinspection JSUnusedGlobalSymbols
String.prototype.ltrim = function () {
    return this.replace(/(^\s*)/g, "");
};
// 去掉右边空格
String.prototype.rtrim = function () {
    return this.replace(/(\s*$)/g, "");
};
// noinspection JSUnusedGlobalSymbols
/**
 * 数组中是否包含指定字符串
 * @param str 目标字符串
 * @returns {boolean}
 */
Array.prototype.contains = function (str) {
    var arr = this;
    for (var i = 0; i < arr.length; i++) {
        if (str === arr[i]) {
            return true;
        }
    }
    return false;
};
// 判断是不是ios端
function isOS() {
    return navigator.userAgent.match(/ipad|iphone/i);
}
//创建文本元素
function createTextArea(text) {
    textArea = document.createElement('textArea');
    textArea.innerHTML = text;
    textArea.value = text;
    document.body.appendChild(textArea);
}
//选择内容
function selectText() {
    var range,
        selection;

    if (isOS()) {
        range = document.createRange();
        range.selectNodeContents(textArea);
        selection = window.getSelection();
        selection.removeAllRanges();
        selection.addRange(range);
        textArea.setSelectionRange(0, 999999);
    } else {
        textArea.select();
    }
}

//复制到剪贴板
function copyToClipboard() {
    try{
        if(document.execCommand("Copy")){
            YHTUtils.showTipEdge({messages:'复制成功', clazz:'success'});
        }else{
            YHTUtils.showTipEdge({messages:'复制失败', clazz:'danger'});
        }
    }catch(err){
        YHTUtils.showTipEdge({messages:'复制失败', clazz:'danger'});
    }
    document.body.removeChild(textArea);
}


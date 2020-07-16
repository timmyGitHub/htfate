<!DOCTYPE html>
<!--suppress ALL -->
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="icon" sizes="any" href="${contextPath}/images/icon/yht_128.ico">
    <title>模板管理</title>
    <link rel="stylesheet" href="${contextPath}/third/bootstrap-4.4.1/bootstrap.min.css">
    <link rel="stylesheet" href="${contextPath}/css/fontawesome/all.min.css">
    <link rel="stylesheet" href="${contextPath}/css/database/bootstrapEX.css">
    <link rel="stylesheet" href="${contextPath}/css/database/mode.css">

    <style>
        #dbTable th, #dbTable td, #pageTable th, #pageTable td, #tableInfo th, #tableInfo td {
            text-align: center;
            vertical-align: middle;
        }
    </style>
</head>
<body>
<script src="${contextPath}/third/jquery/jquery-3.4.1.min.js"></script>
<script src="${contextPath}/third/bootstrap-4.4.1/popper.min.js"></script>
<script src="${contextPath}/third/bootstrap-4.4.1/bootstrap.min.js"></script>
<script src="${contextPath}/third/vue/vue.min.js"></script>
<script src="${contextPath}/third/vue/vue-resource.min.js"></script>
<script src="${contextPath}/third/common/YHTUtils.js"></script>
<script src="${contextPath}/third/common/common.js"></script>
<script src="${contextPath}/js/database/bootstrapValidator.min.js"></script>
<script src="${contextPath}/js/database/bootstrapEX.js"></script>

<div id="vue">

    <!--内容-->
    <div class="container" style="margin-top: 75px">
        <div class="row">
            <div class="col-md-2">
                <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
                    <a class="nav-link active" id="v-pills-home-tab" data-toggle="pill" href="#v-pills-home" role="tab"
                       aria-controls="v-pills-home" aria-selected="true">模版管理</a>

                    <a class="nav-link" id="v-pills-tree-data-tab" data-toggle="pill" href="#v-pills-tree-data"
                       role="tab"
                       aria-controls="v-pills-tree" aria-selected="false" v-on:click="getTreeData">树管理</a>

                    <a class="nav-link" id="v-pills-person-tab" data-toggle="pill" href="#v-pills-person" role="tab"
                       aria-controls="v-pills-person" aria-selected="false" v-on:click="getPersonData">人员管理</a>

                    <a class="nav-link" id="v-pills-role-tab" data-toggle="pill" href="#v-pills-role" role="tab"
                       aria-controls="v-pills-role" aria-selected="false" v-on:click="getRoleData">角色管理</a>

                    <a class="nav-link" id="v-pills-authority-tab" data-toggle="pill" href="#v-pills-authority"
                       role="tab"
                       aria-controls="v-pills-authority" aria-selected="false" v-on:click="getAuthorityData">权限管理</a>
                </div>
            </div>
            <div class="col-md-10">
                <div class="tab-content" id="v-pills-tabContent">
                    <!--模版管理-->
                    <div class="tab-pane fade show active" id="v-pills-home" role="tabpanel"
                         aria-labelledby="v-pills-home-tab">
                        <!--按钮组-->
                        <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                            <div class="btn-group mr-2" role="group" aria-label="First group">
                                <button type="button" class="btn btn-success" data-toggle="modal" v-on:click="addTable">
                                    <i class="fa fa-plus"></i> 增加
                                </button>
                                <button type="button" class="btn btn-danger ht-del-table"><i
                                            class="fa fa-trash-alt"></i> 删除
                                </button>
                                <button type="button" class="btn btn-warning"><i class="fa fa-edit"></i> 修改</button>
                            </div>
                            <div class="btn-group mr-2" role="group" aria-label="Second group">
                                <button type="button" class="btn btn-secondary ht-create-code"><i
                                            class="fa fa-folder"></i> 生成代码
                                </button>
                            </div>
                            <div class="btn-group" role="group" aria-label="Third group">
                                <button type="button" class="btn btn-secondary"
                                        onclick="window.location.href = 'about:blank';"><span style="color: red"><i
                                                class="fa fa-times"></i></span> 不要点击
                                </button>
                            </div>
                        </div>
                        <!-- 表信息 -->
                        <table id="tableInfo" class="table table-striped table-hover mt-4">
                            <thead>
                            <tr class="">
                                <th scope="col" style="width: 10%">
                                    <div class="form-group form-check mt-3">
                                        <input type="checkbox" class="form-check-input" id="ht-all-select">
                                        <label class="form-check-label" for="ht-all-select">全选</label>
                                    </div>
                                </th>
                                <th scope="col">表名</th>
                                <th scope="col">表类型</th>
                                <th scope="col">表说明</th>
                                <th scope="col">数据库状态</th>
                                <th scope="col">生成代码状态</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-if="table.length === 0">
                                <td><a href="javascript:;"><span class="fa fa-sync"
                                                                 v-on:click="getTable($event)"></span></a></td>
                                <td>没</td>
                                <td>有</td>
                                <td>数</td>
                                <td>据！</td>
                                <td></td>
                            </tr>
                            <tr v-for="site in table" :id="site.id" class="ht-tr">
                                <th scope="row">
                                    <div class="form-check">
                                        <input type="checkbox" name="ht-del" class="form-check-input ht-checkbox"
                                               onclick="event.cancelBubble=true">
                                        <label class="form-check-label"></label>
                                    </div>
                                </th>
                                <td><a href="javascript:void(0);" class="" v-on:click="getTableId(site.id)">{{site.tableName}}</a>
                                </td>
                                <td>{{site.tableType}}</td>
                                <td>{{site.tableRemark}}</td>
                                <td v-if="site.dbFlag === 0">
                                    <button class="btn btn-dark btn-sm" v-on:click="createDB($event,0,site.id)">未创建
                                    </button>
                                </td>
                                <td v-else-if="site.dbFlag === 1">
                                    <button class="btn btn-danger btn-sm" v-on:click="createDB($event,1,site.id)">未同步
                                    </button>
                                </td>
                                <td v-else-if="site.dbFlag === 2">
                                    <button class="btn btn-success btn-sm" onclick="event.cancelBubble=true">已同步
                                    </button>
                                </td>
                                <td v-else>
                                    <button class="btn btn-warning btn-sm" onclick="event.stopPropagation()">未知状态
                                    </button>
                                </td>
                                <td v-if="site.codeFlag === 1">
                                    已生成代码
                                </td>
                                <td v-else-if="site.codeFlag === 0">
                                    未生成代码
                                </td>
                                <td v-else-if="site.codeFlag === 2">
                                    待生成代码
                                </td>
                                <td v-else>
                                    出错
                                </td>

                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <!--树管理-->
                    <div class="tab-pane fade" id="v-pills-tree-data" role="tabpanel"
                         aria-labelledby="v-pills-tree-data-tab">
                        <div id="treeDataEcharts" class="container-fluid">
                        </div>
                    </div>
                    <!-- 人员管理-->
                    <div class="tab-pane fade" id="v-pills-person" role="tabpanel"
                         aria-labelledby="v-pills-person-tab">
                        <!--按钮组-->
                        <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                            <div class="btn-group mr-2" role="group" aria-label="First group">
                                <button type="button" class="btn btn-success" data-toggle="modal" v-on:click="addTable">
                                    <i class="fa fa-plus"></i> 增加
                                </button>
                                <button type="button" class="btn btn-danger ht-del-table"><i
                                            class="fa fa-trash-alt"></i> 删除
                                </button>
                                <button type="button" class="btn btn-warning"><i class="fa fa-edit"></i> 修改</button>
                            </div>
                            <div class="btn-group mr-2" role="group" aria-label="Second group">
                                <button type="button" class="btn btn-secondary ht-create-code"><i
                                            class="fa fa-folder"></i> 生成代码
                                </button>
                            </div>
                            <div class="btn-group" role="group" aria-label="Third group">
                                <button type="button" class="btn btn-secondary"
                                        onclick="window.location.href = 'about:blank';"><span style="color: red"><i
                                                class="fa fa-times"></i></span> 不要点击
                                </button>
                            </div>
                        </div>
                    </div>
                    <!-- 角色管理 -->
                    <div class="tab-pane fade" id="v-pills-role" role="tabpanel"
                         aria-labelledby="v-pills-role-tab">
                        <!--按钮组-->
                        <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                            <div class="btn-group mr-2" role="group" aria-label="First group">
                                <button type="button" class="btn btn-success" data-toggle="modal" v-on:click="addTable">
                                    <i class="fa fa-plus"></i> 增加
                                </button>
                                <button type="button" class="btn btn-danger ht-del-table"><i
                                            class="fa fa-trash-alt"></i> 删除
                                </button>
                                <button type="button" class="btn btn-warning"><i class="fa fa-edit"></i> 修改</button>
                            </div>
                            <div class="btn-group mr-2" role="group" aria-label="Second group">
                                <button type="button" class="btn btn-secondary ht-create-code"><i
                                            class="fa fa-folder"></i> 生成代码
                                </button>
                            </div>
                            <div class="btn-group" role="group" aria-label="Third group">
                                <button type="button" class="btn btn-secondary"
                                        onclick="window.location.href = 'about:blank';"><span style="color: red"><i
                                                class="fa fa-times"></i></span> 不要点击
                                </button>
                            </div>
                        </div>
                    </div>
                    <!-- 权限管理 -->
                    <div class="tab-pane fade" id="v-pills-authority" role="tabpanel"
                         aria-labelledby="v-pills-authority-tab">
                        <!--按钮组-->
                        <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                            <div class="btn-group mr-2" role="group" aria-label="First group">
                                <button type="button" class="btn btn-success" data-toggle="modal" v-on:click="addTable">
                                    <i class="fa fa-plus"></i> 增加
                                </button>
                                <button type="button" class="btn btn-danger ht-del-table"><i
                                            class="fa fa-trash-alt"></i> 删除
                                </button>
                                <button type="button" class="btn btn-warning"><i class="fa fa-edit"></i> 修改</button>
                            </div>
                            <div class="btn-group mr-2" role="group" aria-label="Second group">
                                <button type="button" class="btn btn-secondary ht-create-code"><i
                                            class="fa fa-folder"></i> 生成代码
                                </button>
                            </div>
                            <div class="btn-group" role="group" aria-label="Third group">
                                <button type="button" class="btn btn-secondary"
                                        onclick="window.location.href = 'about:blank';"><span style="color: red"><i
                                                class="fa fa-times"></i></span> 不要点击
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--添加表单-->
    <div>
        <!-- Modal -->
        <div class="modal fade" id="addTable" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle"
             aria-hidden="true"
             data-backdrop="static">
            <div class="modal-dialog" role="document" style="max-width: 100rem">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLongTitle">表单</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="">
                            <form id="tableData">
                                <!--表信息-->
                                <div>
                                    <div class="form-row">
                                        <div class="form-group col-md-1"><input type="text" name="tableId"
                                                                                :id="tableInfo.id" :value="tableInfo.id"
                                                                                style="display: none"></input></div>
                                        <div class="form-group col-md-5">
                                            <label for="tableName">表名：</label>
                                            <input type="text" class="form-control" id="tableName" name="tableName"
                                                   placeholder="请输入表名，例如：table_table" :value="tableInfo.tableName">
                                        </div>
                                        <div class="form-group col-md-5">
                                            <label for="tableName">表说明：</label>
                                            <input type="text" class="form-control" id="tableRemark" name="tableRemark"
                                                   placeholder="请输入表说明" :value="tableInfo.tableRemark">
                                        </div>
                                        <div class="form-group col-md-1"></div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group col-md-1"></div>
                                        <div class="form-group col-md-5">
                                            <label for="tableName">表类型：</label>
                                            <input type="text" class="form-control" id="tableType" name="tableType"
                                                   placeholder="请输入表类型" :value="tableInfo.tableType">
                                        </div>
                                        <div class="form-group col-md-5">
                                            <label for="tableName">类名：</label>
                                            <input type="text" class="form-control" id="clazzName" name="clazzName"
                                                   placeholder="请输入类名, 例如：TableTable" :value="tableInfo.clazzName">
                                        </div>
                                        <div class="form-group col-md-1"></div>
                                    </div>
                                </div>
                                <hr style="height:1px;border:none;border-top:1px solid #185598;"/>
                                <!--字段信息-->
                                <div class="">
                                    <nav>
                                        <div class="nav nav-tabs" id="nav-tab" role="tablist">
                                            <!--数据库字段信息-->
                                            <a class="nav-item nav-link active" id="dbFieldSetTab" data-toggle="tab"
                                               href="#dbFieldSet" role="tab" aria-controls="dbFieldSet"
                                               aria-selected="true">数据库字段设置</a>
                                            <!--页面字段信息-->
                                            <a class="nav-item nav-link" id="pageFieldSetTab" data-toggle="tab"
                                               href="#pageFieldSet" role="tab" aria-controls="pageFieldSet"
                                               aria-selected="false">页面字段设置</a>
                                            <a class="nav-item nav-link" id="nav-contact-tab" data-toggle="tab"
                                               href="#nav-contact" role="tab" aria-controls="nav-contact"
                                               aria-selected="false">Contact</a>
                                        </div>
                                    </nav>
                                    <div class="tab-content" id="nav-tabContent">
                                        <!--数据库字段信息-->
                                        <div class="tab-pane fade show active" id="dbFieldSet" role="tabpanel"
                                             aria-labelledby="dbFieldSetTab">
                                            <table id="dbTable" class="table table-bordered table-hover">
                                                <thead>
                                                <tr>
                                                    <th scope="col" class="" style="width: 10%;">
                                                        <button id="addDBField" type="button"
                                                                class="btn btn-light btn-sm">增加
                                                        </button>
                                                        <button id="delDBField" type="button"
                                                                class="btn btn-light btn-sm">删除
                                                        </button>
                                                    </th>
                                                    <th scope="col" class="" style="width: 25%;">
                                                        <span>字段名</span><br><span
                                                                style="font-size: 10px;color: #dc3545">(数据库不区分大小写)</span>
                                                    </th>
                                                    <th scope="col">字段说明</th>
                                                    <th scope="col" style="width: 25%;">字段类型</th>
                                                    <th scope="col" style="width: 8%;">是否主键</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <!--<div th:replace="~{view/thymeleafTemplates :: dbTable}"></div>-->
                                                <tr v-for="(db,index) in dbField" :id="'db-table-'+(index+1)"
                                                    class="ht-tr">
                                                    <td>
                                                        <div class="form-check">
                                                            <input type="checkbox" name="ht-del"
                                                                   class="form-check-input ht-checkbox"
                                                                   onclick="event.cancelBubble=true">
                                                            <label class="form-check-label"></label>
                                                            <input :id="'field-display-'+(index+1)" type="text"
                                                                   name="fieldDisplay"
                                                                   :value="db.fieldDisplay === '' ? dbFieldLength : db.fieldDisplay"
                                                                   onclick="event.cancelBubble=true"
                                                                   style="display: none">
                                                            {{db.fieldDisplay === '' ? dbFieldLength : db.fieldDisplay}}
                                                        </div>
                                                    </td>
                                                    <td><input :id="'field-name-'+(index+1)" type="text"
                                                               name="fieldName" class="form-control"
                                                               :value="db.fieldName" onclick="event.cancelBubble=true">
                                                    </td>
                                                    <td><input :id="'field-remark-'+(index+1)" type="text"
                                                               name="fieldRemark" class="form-control"
                                                               :value="db.fieldRemark"
                                                               onclick="event.cancelBubble=true"></td>
                                                    <td>
                                                        <div class="">
                                                            <input :id="'field-type-'+(index+1)" type="text"
                                                                   name="fieldType"
                                                                   class="form-control form-control-sm ht-input-select-1"
                                                                   :value="db.fieldType"
                                                                   onclick="event.cancelBubble=true">
                                                            <select class="form-control form-control-sm ht-input-select-db"
                                                                    onclick="event.cancelBubble=true">
                                                                <option v-if="'varchar' === (db.fieldType.indexOf('(') === -1 ? db.fieldType:db.fieldType.substring(0,db.fieldType.indexOf('(')))"
                                                                        value="varchar(32)" selected="selected">
                                                                    varchar(32)
                                                                </option>
                                                                <option v-else value="varchar(32)">varchar(32)</option>
                                                                <option v-if="'int' === db.fieldType" value="int(10)"
                                                                        selected="selected">int(10)
                                                                </option>
                                                                <option v-else value="int">int</option>
                                                                <option v-if="'bigint' === db.fieldType"
                                                                        value="bigint" selected="selected">bigint
                                                                </option>
                                                                <option v-else value="bigint">bigint</option>
                                                                <option v-if="'double' === (db.fieldType.indexOf('(') === -1 ? db.fieldType:db.fieldType.substring(0,db.fieldType.indexOf('(')))"
                                                                        value="double(11,3)" selected="selected">
                                                                    double(11,3)
                                                                </option>
                                                                <option v-else value="double(11,3)">double(11,3)
                                                                </option>
                                                                <option v-if="'datetime' === db.fieldType"
                                                                        value="datetime" selected="selected">datetime
                                                                </option>
                                                                <option v-else value="datetime">datetime</option>
                                                                <option v-if="'text'===db.fieldType" value="text"
                                                                        selected="selected">text
                                                                </option>
                                                                <option v-else value="text">text</option>
                                                            </select>
                                                        </div>
                                                    </td>
                                                    <td class="ht-td">
                                                        <div class="form-check">
                                                            <input v-if="db.fieldKey === 'true'" type="checkbox"
                                                                   name="fieldKey" class="form-check-input" checked
                                                                   onclick="event.cancelBubble=true">
                                                            <input v-else type="checkbox" name="fieldKey"
                                                                   class="form-check-input"
                                                                   onclick="event.cancelBubble=true">
                                                            <label class="form-check-label"></label>
                                                        </div>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <!--页面字段信息-->
                                        <div class="tab-pane fade" id="pageFieldSet" role="tabpanel"
                                             aria-labelledby="pageFieldSetTab">
                                            <table id="pageTable" class="table table-bordered table-hover">
                                                <thead>
                                                <tr>
                                                    <th scope="col" class="" style="width: 71px;">
                                                        <button id="addDBField" type="button"
                                                                class="btn btn-light btn-sm">增加
                                                        </button>
                                                        <button id="delDBField" type="button"
                                                                class="btn btn-light btn-sm">删除
                                                        </button>
                                                    </th>
                                                    <th scope="col" class="" style="width: 25%;">
                                                        <span>字段名</span><br><span
                                                                style="font-size: 10px;color: #dc3545">（确保与数据库对应）</span>
                                                    </th>
                                                    <th scope="col" style="width: 13%"><span>字段说明</span></th>
                                                    <!--<th scope="col" style="width: 15%;">Java 变量</th>-->
                                                    <!--<th scope="col" style="width: 15%;">Java 类型</th>-->
                                                    <th scope="col" style="width: 25%;">表单类型</th>
                                                    <th scope="col" style="">校验</th>
                                                    <th scope="col" style="width: 110px;">提示</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <!--<div th:replace="~{view/thymeleafTemplates :: pageTable}"></div>-->
                                                <tr v-for="(page,index) in pageField" :id="'page-table-'+(index+1)"
                                                    class="ht-tr">
                                                    <td>
                                                        <div class="form-check">
                                                            <input type="checkbox" name="ht-del"
                                                                   class="form-check-input ht-checkbox"
                                                                   onclick="event.cancelBubble=true">
                                                            <label class="form-check-label"></label>
                                                        </div>
                                                    </td>
                                                    <td><input :id="'page-field-name-'+(index+1)" type="text"
                                                               name="pageFieldName" class="form-control"
                                                               :value="dbField[index].fieldName"
                                                               onclick="event.cancelBubble=true"></td>
                                                    <td><input :id="'page-field-remark-'+(index+1)" type="text"
                                                               name="pageFieldRemark" class="form-control"
                                                               :value="dbField[index].fieldRemark"
                                                               onclick="event.cancelBubble=true"></td>
                                                    <!--<td><input id="'page-java-variable-'+(index+1)" type="text" name="pageJavaVariable" class="form-control" :value="page.pageJavaVariable"></td>-->
                                                    <!--<td>
                                                        <div class="">
                                                            <select id="'page-java-type-'+(index+1)" name="pageJavaType" class="form-control form-control-sm">
                                                                <option v-if="'String' === page.pageJavaType" value="String" selected="selected">String</option>
                                                                <option v-else value="String">String</option>
                                                                <option v-if="'Integer' === page.pageJavaType" value="Integer" selected="selected">Integer</option>
                                                                <option v-else value="Integer">Integer</option>
                                                                <option v-if="'Double' === page.pageJavaType" value="Double" selected="selected">Double</option>
                                                                <option v-else value="Double">Double</option>
                                                                <option v-if="'Long' === page.pageJavaType" value="Long" selected="selected">Long</option>
                                                                <option v-else value="Long">Long</option>
                                                                <option v-if="'Timestamp' === page.pageJavaType" value="Timestamp" selected="selected">Timestamp</option>
                                                                <option v-else value="Timestamp">Timestamp</option>
                                                            </select>
                                                        </div>
                                                    </td>-->
                                                    <td class="">
                                                        <div class="">
                                                            <input type="text" name="pageFormValue"
                                                                   class="form-control form-control-sm ht-input-select-1"
                                                                   :placeholder="page.pageFormValue"
                                                                   :value="page.pageFormValue"
                                                                   onclick="event.cancelBubble=true">
                                                            <select :id="'page-form-type-select-'+(index+1)"
                                                                    name="pageFormType"
                                                                    class="form-control form-control-sm ht-input-select-form"
                                                                    onclick="event.cancelBubble=true">
                                                                <option v-if="'text' === page.pageFormType" value="text"
                                                                        selected="selected">文本
                                                                </option>
                                                                <option v-else value="text">文本</option>
                                                                <option v-if="'radio' === page.pageFormType"
                                                                        value="radio" selected="selected">单选
                                                                </option>
                                                                <option v-else value="radio">单选</option>
                                                                <option v-if="'checkbox' === page.pageFormType"
                                                                        value="checkbox" selected="selected">多选
                                                                </option>
                                                                <option v-else value="checkbox">多选</option>
                                                                <option v-if="'select' === page.pageFormType"
                                                                        value="select" selected="selected">下拉
                                                                </option>
                                                                <option v-else value="select">下拉</option>
                                                                <option v-if="'date' === page.pageFormType" value="date"
                                                                        selected="selected">日期
                                                                </option>
                                                                <option v-else value="date">日期</option>
                                                                <option v-if="'datetime' === page.pageFormType"
                                                                        value="datetime"
                                                                        selected="selected">时间
                                                                </option>
                                                                <option v-else value="datetime">时间</option>
                                                                <option v-if="'wang' === page.pageFormType"
                                                                        value="wang" selected="selected">编辑
                                                                </option>
                                                                <option v-else value="wang">编辑</option>

                                                                <option v-if="'img' === page.pageFormType"
                                                                        value="img" selected="selected">图片
                                                                </option>
                                                                <option v-else value="img">图片</option>

                                                                <option v-if="'file' === page.pageFormType"
                                                                        value="file" selected="selected">文件
                                                                </option>
                                                                <option v-else value="file">文件</option>
                                                            </select>
                                                            <div :id="'page-form-type-plus-'+(index+1)"
                                                                 class="ht-page-form-type" style="text-align: initial;">
                                                                <div v-if="'checkbox' === page.pageFormType">
                                                                    <div class="form-check"
                                                                         v-if="YHTUtils.isNotEmpty(page.pageFormValue)"
                                                                         v-for="(ft,index2) in page.pageFormValue.toString().split(',')">
                                                                        <input v-if="page.defaultValue === ft.toString().split(':')[0]"
                                                                               class="form-check-input"
                                                                               type="checkbox"
                                                                               name="pageFormCheckbox-1"
                                                                               :value="ft.toString().split(':')[0]"
                                                                               :id="'page-form-checkbox-'+(index2+1)+'-'+(index+1)"
                                                                               checked>
                                                                        <input v-else class="form-check-input"
                                                                               type="checkbox"
                                                                               name="pageFormCheckbox-1"
                                                                               :value="ft.toString().split(':')[0]"
                                                                               :id="'page-form-checkbox-'+(index2+1)+'-'+(index+1)">
                                                                        <label class="form-check-label"
                                                                               :for="'page-form-checkbox-'+(index2+1)+'-'+(index+1)">{{ft.toString().split(':')[1]}}</label>
                                                                    </div>
                                                                </div>
                                                                <div v-else-if="'radio' === page.pageFormType">
                                                                    <div class="form-check"
                                                                         v-if="YHTUtils.isNotEmpty(page.pageFormValue)"
                                                                         v-for="(ft,index2) in page.pageFormValue.toString().split(',')">
                                                                        <input v-if="page.defaultValue === ft.toString().split(':')[0]"
                                                                               class="form-check-input"
                                                                               type="radio"
                                                                               name="pageFormRadio-1"
                                                                               :value="ft.toString().split(':')[0]"
                                                                               :id="'page-form-checkbox-'+(index2+1)+'-'+(index+1)"
                                                                               checked>
                                                                        <input v-else class="form-check-input"
                                                                               type="radio"
                                                                               name="pageFormCheckbox-1"
                                                                               :value="ft.toString().split(':')[0]"
                                                                               :id="'page-form-checkbox-'+(index2+1)+'-'+(index+1)">
                                                                        <label class="form-check-label"
                                                                               :for="'page-form-checkbox-'+(index2+1)+'-'+(index+1)">{{ft.toString().split(':')[1]}}</label>
                                                                    </div>
                                                                </div>
                                                                <div v-else-if="'select' === page.pageFormType"
                                                                     style="width: 90%;">
                                                                    <select v-if="YHTUtils.isNotEmpty(page.pageFormValue)"
                                                                            class="form-control form-control-sm"
                                                                            name="pageFormSelect-1">
                                                                        <option value=""></option>
                                                                        <option v-for="selectValue in page.pageFormValue.toString().split(',')"
                                                                                v-if="selectValue.toString().split(':')[0] === page.defaultValue"
                                                                                selected
                                                                                :value="selectValue.toString().split(':')[0]">
                                                                            {{selectValue.toString().split(':')[1]}}
                                                                        </option>
                                                                        <option v-else
                                                                                :value="selectValue.toString().split(':')[0]">
                                                                            {{selectValue.toString().split(':')[1]}}
                                                                        </option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <div style="text-align: left">
                                                            <div v-for="v in page.pageFormVerify"
                                                                 class="form-check form-check-inline">
                                                                <input v-if="v.is"
                                                                       :name="'pageFormVerify-'+index+'-'+v.key"
                                                                       :id="'pageFormVerify-'+index+'-'+v.key"
                                                                       class="form-check-input" type="checkbox"
                                                                       v-on:click.stop=""
                                                                       :value="v.key" checked>
                                                                <input v-else :name="'pageFormVerify-'+index+'-'+v.key"
                                                                       :id="'pageFormVerify-'+index+'-'+v.key"
                                                                       class="form-check-input" type="checkbox"
                                                                       :value="v.key">
                                                                <label class="form-check-label"
                                                                       :for="'pageFormVerify-'+index+'-'+v.key">{{v.value}}</label>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <div>
                                                            <input type="text" name="pageFormPlaceholder"
                                                                   class="form-control form-control-sm"
                                                                   :placeholder="page.pageFormPlaceholder"
                                                                   :value="page.pageFormPlaceholder"
                                                                   onclick="event.cancelBubble=true">
                                                        </div>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="tab-pane fade" id="nav-contact" role="tabpanel"
                                             aria-labelledby="nav-contact-tab">...
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">

                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--数据库创建提示框-->
    <div id="DBCreate" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" style="color: #856404">警告！</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="container row justify-content-center">
                        <div class="">
                            <div class="row align-self-start">
                                <span style="margin-left: 10px;color: #dc3545">你可想好了，</span>
                            </div>
                            <div class="row align-self-center">
                                <span style="margin-left: 95px;color: #dc3545">此创建不可逆，</span>
                            </div>
                            <div class="row align-self-end">
                                <span style="margin-left: 195px;color: #dc3545">造成损失概不负责！</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">没想好</button>
                    <button type="button" class="btn btn-primary" v-on:click="connectionDB">后果自负</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 生成代码提示框 -->
    <div id="createCodeModal" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">生成代码信息</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="container">
                        <div v-if="createCode.length === 0" class="text-center">
                            <h4>请选择一张表单好吗？</h4>
                        </div>
                        <div v-else>
                            <div class="text-center mb-4">
                                <h6 class="" style="color:#dc3545">若是第二次生成代码，将会覆盖第一次，请做好备份！</h6>
                            </div>
                            <div v-for="(site,index) in createCode">
                                <table :id="'createCodeTableInfo-'+index" name="createCodeTableInfo"
                                       class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th class="text-center" style="width: 25%">{{site.tableName}} 表<input
                                                    :value="site.tableId" name="tableId" style="display: none;"></th>
                                        <th class="text-center">
                                            数据库状态：
                                            <span v-if="site.dbFlag === 0" style="color:#6c757d">未创建</span>
                                            <span v-else-if="site.dbFlag === 1" style="color: #dc3545">未同步</span>
                                            <span v-else-if="site.dbFlag === 2" style="color: #28a745">已同步</span>
                                            <span v-else>未知状态</span>
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="text-right">尊姓大名：</td>
                                        <td><input class="form-control" type="text" name="tableAuthor"
                                                   :value="site.tableAuthor"></input></td>
                                    </tr>
                                    <tr>
                                        <td class="text-right">表名：</td>
                                        <td><input type="text" class="form-control" name="tableName"
                                                   :value="site.tableName"></input></td>
                                    </tr>
                                    <tr>
                                        <td class="text-right">表说明：</td>
                                        <td><input class="form-control" type="text" name="tableRemark"
                                                   :value="site.tableRemark"></input></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <div class="row justify-content-center">
                                                <div class="text-danger">第一次生成需要全选！</div>
                                                <div class="form-check form-check-inline"
                                                     v-on:click="changeCheckBoxValue($event)">
                                                    <input class="form-check-input" type="checkbox" id="controller"
                                                           name="controller" value="true" checked>
                                                    <label class="form-check-label text-info" for="controller"
                                                           onclick="event.cancelBubble=true">是否创建 Controller 层</label>
                                                </div>
                                                <div class="form-check form-check-inline"
                                                     v-on:click="changeCheckBoxValue($event)">
                                                    <input class="form-check-input" type="checkbox" id="entity"
                                                           name="entity" value="true" checked>
                                                    <label class="form-check-label text-info" for="entity"
                                                           onclick="event.cancelBubble=true">是否创建 entity 层</label>
                                                </div>
                                                <div class="form-check form-check-inline"
                                                     v-on:click="changeCheckBoxValue($event)">
                                                    <input class="form-check-input" type="checkbox" id="service"
                                                           name="service" value="true" checked>
                                                    <label class="form-check-label text-info" for="service"
                                                           onclick="event.cancelBubble=true">是否创建 service 层</label>
                                                </div>
                                                <div class="form-check form-check-inline"
                                                     v-on:click="changeCheckBoxValue($event)">
                                                    <input class="form-check-input" type="checkbox" id="dao" name="dao"
                                                           value="true" checked>
                                                    <label class="form-check-label text-info" for="dao"
                                                           onclick="event.cancelBubble=true">是否创建 dao 层</label>
                                                </div>
                                            </div>

                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div v-if="index+1 != createCode.length">
                                    <hr style="height:1px;border:none;border-top:1px solid #dc3545;"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div v-if="createCode.length === 0">
                        <button type="button" class="btn btn-secondary" onclick="window.location.href = 'about:blank';">
                            不好
                        </button>
                        <button type="button" class="btn btn-primary" data-dismiss="modal">好的</button>
                    </div>
                    <div v-else>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary" v-on:click="createCodeFun($event)">生成</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--自定义提示框-->
    <div id="tips" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" v-html="tipMessage.title"></h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="row justify-content-center">
                        <div v-html="tipMessage.messages"></div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button v-if="tipMessage.showCancel" type="button"
                            :class='tipMessage.showConfirm ? "btn btn-secondary":"btn btn-primary"' data-dismiss="modal"
                            v-on:click="tipFun(tipMessage.cancelFunction,tipMessage.param)">{{tipMessage.cancel}}
                    </button>
                    <button v-if="tipMessage.showConfirm" type="button" class="btn btn-primary"
                            v-on:click="tipFun(tipMessage.confirmFunction,tipMessage.param)">{{tipMessage.confirm}}
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script src="${contextPath}/js/database/model.js"></script>
<script>
    var vm = new Vue({
        el: "#vue",
        data: {
            table: [],
            DBCreate: {},                                          // table 是否有数据
            createCode: [],                                        // 代码生成
            tableInfo: JSON.parse(JSON.stringify(TABLE_INFO)),     // 表信息
            dbField: JSON.parse(JSON.stringify(DB_FIELD)),                            // 表字段信息
            dbFieldLength: 0,                                     // size
            pageField: JSON.parse(JSON.stringify(PAGE_FIELD)),                        // 页面字段信息
            pageVerify: JSON.parse(JSON.stringify(PAGE_VERIFY)),                        // 页面字段校验信息
            tableInfoTemp: {},                                     // 表信息 temp
            dbFieldTemp: [],                                       // 表字段信息 temp
            pageFieldTemp: [],                                     // 页面字段信息 temp
            tipMessage: {},                                        // 自定义提示框的信息
            tipEdge: {'clazz': 'danger', 'messages': '自定义侧边栏提示框！', 'time': 3000} // 自定义侧边栏提示框
        },
        methods: {
            // 添加表单
            addTable: function () {
                var that = this;
                that.tableInfo = JSON.parse(JSON.stringify(TABLE_INFO));
                that.dbField = DB_FIELD.slice(0);
                that.pageField = PAGE_FIELD.slice(0);
                $("#addTable").find(".modal-footer").html(
                    "<button type=\"button\" class=\"btn btn-danger ht-table-reset\">重置</button>" +
                    "<button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">取消</button>" +
                    "<button type=\"button\" class=\"btn btn-primary ht-submit\" name='ht-submit'>保存</button>");
                $("#addTable").modal('show');
                console.log("click addTable")
            },
            // 删除表单,model.js 调用
            delTable: function (tableIds) {
                YHTUtils.showLoading();
                var that = this;
                that.$http.delete(MODEL_DELETE_URL + "/" + tableIds).then(function (res) {
                    var data = res.data;
                    if (data.success && 0 === data.code) {
                        that.getTable();
                        console.log(data.msg)
                        $("#tips").modal("hide");
                        YHTUtils.hideLoading();
                    } else {
                        console.log(data.msg)
                        $("#tips").modal("hide");
                        YHTUtils.hideLoading();
                    }
                })
            },
            // 获取所有表单
            getTable: function (e) {
                YHTUtils.startSpin(e);
                var that = this;
                that.$http.post(MODEL_GET_TABLE_URL, {pageIndex:0,pageSize: 20}).then(function (res) {
                    var data = res.data;
                    if (0 === data.code) {
                        that.table = data.data;
                        YHTUtils.stopSpin(e);
                    } else {
                        console.log(data.msg);
                        YHTUtils.stopSpin(e);
                    }
                })
            },
            // 根据 table id 查找
            getTableId: function (id) {
                event.stopPropagation();//阻止触发父元素的点击事件
                var that = this;
                that.$http.get(MODEL_GET_TABLE_ID_URL + "/" + id).then(function (res) {
                    var data = res.data;
                    console.log(data.data);
                    if (0 === data.code) {
                        // 渲染
                        that.tableInfo = data.data.tableInfo;
                        that.dbField = data.data.dbField;
                        that.pageField = data.data.pageField;

                        for (var o = 0; o < that.pageField.length; o++) {
                            var pageVerify = that.pageField[o]['pageFormVerify'];
                            that.pageVerify = JSON.parse(JSON.stringify(PAGE_VERIFY));
                            console.log(pageVerify, that.pageVerify);
                            if (YHTUtils.isNotEmpty(pageVerify)) {
                                var pageVerifyArr = pageVerify.split(',');
                                for (var i = 0; i < that.pageVerify.length; i++) {
                                    for (var j = 0; j < pageVerifyArr.length; j++) {
                                        if (that.pageVerify[i]['key'] === pageVerifyArr[j]) {
                                            that.pageVerify[i]['is'] = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            that.pageField[o]['pageFormVerify'] = that.pageVerify;
                        }
                        // 复制一份，用于重置
                        that.tableInfoTemp = JSON.parse(JSON.stringify(data.data.tableInfo));
                        that.dbFieldTemp = data.data.dbField.slice(0);
                        that.pageFieldTemp = data.data.pageField.slice(0);
                        $("#addTable").find(".modal-footer").html(
                            "<button type=\"button\" class=\"btn btn-danger ht-table-reset\" v-on:click=\"resetTable\">重置</button>" +
                            "<button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">取消</button>" +
                            "<button type=\"button\" class=\"btn btn-primary ht-update\">保存</button>");
                        $("#addTable").modal('show');
                    } else {
                        console.log(data.msg)
                    }
                });
            },
            // 表单更新
            tableUpdate: function () {
                console.log("update...")
            },
            // 创建/同步 数据库 0-创建1-同步
            connectionDB: function () {
                YHTUtils.showLoading();
                event.stopPropagation();
                var that = this;
                that.DBCreate.obj.prop("disabled", true);
                $("#DBCreate").modal("hide")
                that.$http.get(MODEL_CREATE_URL + "/" + that.DBCreate.flag + "/" + that.DBCreate.id).then(function (res) {
                    var data = res.data;
                    if (0 === data.code) {
                        that.getTable();
                        that.DBCreate.obj.prop("disabled", false);
                        YHTUtils.hideLoading();
                    } else {
                        that.DBCreate.obj.prop("disabled", false);
                        console.log(data.msg);
                        YHTUtils.hideLoading();
                    }
                })
                console.log(that.DBCreate.flag, that.DBCreate.id);
            },
            // 给上面做铺垫，先触发提示框，确认之后在提交
            createDB: function (e, i, id) {
                console.log(i, id)
                event.stopPropagation();
                var that = this;
                that.DBCreate['obj'] = $(e.currentTarget);
                that.DBCreate['flag'] = i;
                that.DBCreate['id'] = id;
                $("#DBCreate").modal("show");
            },
            // 生成代码
            createCodeFun: function (e) {
                YHTUtils.showLoading();
                var that = this;
                var createCodeTableObjs = $(e.currentTarget).parents(".modal-footer").siblings(".modal-body").find("[name='createCodeTableInfo']");
                var obj = {};
                var objArr = [];
                $.each(createCodeTableObjs, function (i) {
                    var ress = $(this).find("input");
                    $.each(ress, function (i) {
                        obj[$(this).attr("name")] = $(this).val();
                    });
                    objArr.push(obj);
                    obj = {};
                })
                console.log(JSON.stringify(objArr))
                that.$http.post(GENERATE_CODE_URL, {"generateInfo": JSON.stringify(objArr)}).then(function (res) {
                    var data = res.data;
                    if (0 === data.code) {
                        $("#createCodeModal").modal('hide');
                        YHTUtils.hideLoading();
                        var tipE = that.initTipEdge();
                        tipE.messages = "生成代码成功！";
                        $('#tipEdge').alert();
                        setInterval(function () {
                            $('#tipEdge').alert('close');
                        }, tipE.time);
                    } else {
                        console.log(data['msg'])
                        YHTUtils.hideLoading();
                    }
                }, function (reason) {
                    var data = reason.json();
                    console.log(data);
                    YHTUtils.hideLoading();
                })

            },
            // 生成代码-复选框改变事件
            changeCheckBoxres: function (e) {
                var that = this;
                var obj = $(e.currentTarget).children('input');
                if (obj.prop('checked')) {
                    obj.val("true");
                } else {
                    obj.val("false");
                }
            },
            // 树管理-获取数据
            getTreeData: function () {
                var that = this;
                var myChart = echarts.init(document.getElementById('treeDataEcharts'));
                myChart.showLoading();
                that.$http.get(TREE_GET_URL).then(function (res) {
                    var data = res.data;
                    myChart.hideLoading();
                })
            },
            // 获取用户
            getPersonData: function () {
                var that = this;
                YHTUtils.showLoading();
                that.$http.get(GET_PERSON_LIST).then(function (res) {
                    console.log(res)
                    YHTUtils.hideLoading()
                }, function (reason) {
                    YHTUtils.hideLoading();
                    console.log(reason);
                });
            },
            // 获取角色
            getRoleData: function () {

            },
            // 获取权限
            getAuthorityData: function () {

            },
            // 自定义提示框执行方法
            tipFun: function (funName, param) {
                var that = this;
                if ("" !== funName) {
                    that[funName](param);
                }
            },
            // 提示框初始化
            initTip: function () {
                var that = this;
                that.tipMessage = {
                    "param": "",
                    "title": "提示",
                    "messages": "自定义提示框",
                    "cancel": "取消",
                    "confirm": "确定",
                    "showCancel": true,
                    "showConfirm": true,
                    "cancelFunction": "",
                    "confirmFunction": ""
                };
                return that.tipMessage;
            },
            // 侧边栏提示框初始化
            initTipEdge: function () {
                var that = this;
                that.tipEdge = {
                    "messages": "自定义侧边栏提示框！",
                    "clazz": "success",// primary secondary success danger warning info dark
                    "time": 3000
                }
                return that.tipEdge;
            },
            // 侧边栏提示框-显示
            showTipEdge: function () {
                var that = this;
                $('#tipEdge').addClass('show');
                setTimeout(function () {
                    $('#tipEdge').removeClass('show');
                }, that.tipEdge.time);
            }
        },
        mounted: function () {
            var that = this;
            console.log(that.pageVerify);
            // 获取表单
            that.getTable()
        },
        components: {
            'table-update': {
                methods: {
                    tableUpdate: function () {
                        vm.tableUpdate();
                    }
                },
                template: "<div><button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">取消</button>" +
                    "<button type=\"button\" class=\"btn btn-primary\" v-on:click=\"tableUpdate\">保存</button></div>"
            }
        }
    })
</script>
</html>
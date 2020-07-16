var ACTIVE_MODEL = 'gateway';

var ORIGIN = location.origin;
var HTTP = location.protocol + '//' + location.hostname;
var BASE_URL = '';
var DEFULT_USER_ID = '61381c2a80d2496094d369b9a94b8b98';

/*剪贴板*/
var SAVEN_PASTE_CONTEXT_URL;// 保存内容
var GET_PASTE_CONTEXT_URL;// 获取内容
var UPLOAD_PASTE_FILE_URL;// 上传文件
var DOWNLOAD_PASTE_FILE_URL;// 下载文件

/*主页跳转路径*/
var REDIRECT_SAY_URL = ORIGIN + '/editor';// 留言板
var REDIRECT_PASTE_URL = ORIGIN + '/paste';// 剪贴板


/*登录成功后跳转页面*/
var USER_PAGE_URL = ORIGIN + "/page/page/xxx";
/*认证路径*/
var OAUTH_LOGIN_URL = ORIGIN + '/login';
var OAUTH_ACCESS_TOKEN_URL = 'http://purerland:purerland@localhost:52005/oauth/token';

/* 操作用户*/
var USER_ORIGIN = "http://localhost:52005";
var USER_ADD_URL = USER_ORIGIN + '/user/add';// 注册
var USER_LOGIN_URL = USER_ORIGIN + '/user/login';// 登录

/*模版中心*/

var MODEL_CREATE_URL = HTTP + ":52011/modelManage/create";                             // 创建数据库
var MODEL_SAVE_URL = HTTP + ":52011/modelManage/save";                                 // 保存表单
var MODEL_DELETE_URL = HTTP + ":52011/modelManage/delete";                             // 删除表单
var MODEL_GET_TABLE_URL = HTTP + ":52011/modelManage/search";                          // 获取表单
var MODEL_GET_TABLE_ID_URL = HTTP + ":52011/modelManage/form";                         // 根据 id 获取 form 表单信息
// 生成代码地址
var GENERATE_CODE_URL = HTTP + ":52011/generateCode/generate";                         // 生成代码
// 树管理
var TREE_GET_URL = HTTP + ":52011/treeData/all";                                       // 树管理-获取所有数据
var TREE_SAVE_URL = ORIGIN + 'treeData/save';                                     // 树管理-保存数据
var TREE_DELETED_URL = ORIGIN + 'treeData/deleted';                               // 树管理-删除数据

// 人员管理
var GET_PERSON_LIST = ORIGIN + 'personInfo/list';
var DEL_PERSON = ORIGIN + 'personInfo/del';
var SAVE_PERSON = ORIGIN + 'personInfo/save';
var GET_PERSON_ID = ORIGIN + 'personInfo/getById';
switch (ACTIVE_MODEL) {
    case "gateway":
        BASE_URL = '/page';
        SAVEN_PASTE_CONTEXT_URL = ORIGIN + '/wang-editor/wang/savePaste';
        GET_PASTE_CONTEXT_URL = ORIGIN + '/wang-editor/wang/listPaste';
        USER_ADD_URL = ORIGIN + '/oauth2-center/user/add';
        USER_LOGIN_URL = ORIGIN + '/oauth2-center/user/login';
        UPLOAD_PASTE_FILE_URL = ORIGIN + '/upload/upload/paste';
        DOWNLOAD_PASTE_FILE_URL = ORIGIN + '/upload/upload/download';
        // 模版中心
        MODEL_CREATE_URL = ORIGIN + "/database/modelManage/create";                             // 创建数据库
        MODEL_SAVE_URL = ORIGIN + "/database/modelManage/save";                                 // 保存表单
        MODEL_DELETE_URL = ORIGIN + "/database/modelManage/delete";                             // 删除表单
        MODEL_GET_TABLE_URL = ORIGIN + "/database/modelManage/search";                          // 获取表单
        MODEL_GET_TABLE_ID_URL = ORIGIN + "/database/modelManage/form";
        break;
}

// 初始表单信息
var TABLE_INFO = {"id": "", "tableName": "", "tableRemark": "", "tableType": "", "clazzName": ""};
// 校验
var PAGE_VERIFY = [
    {key:'required',value:'必填', is:false},
    {key: 'phone', value: '手机号',is:false},
    {key: 'email', value: '邮箱',is:false},
    {key: 'url', value: '地址',is:false},
    {key: 'number', value: '数字',is:false},
    {key: 'date', value: '日期',is:false},
    {key: 'identity', value: '身份证',is:false}
];
// 数据库字段
var DB_FIELD = [
    {
        "fieldDisplay": 1,
        "fieldName": "id",
        "fieldRemark": "主键",
        "fieldType": "bigint",
        "fieldKey": "true"
    },
    {
        "fieldDisplay": 2,
        "fieldName": "uuid",
        "fieldRemark": "唯一键",
        "fieldType": "varchar(32)",
        "fieldKey": "false"
    },
    {
        "fieldDisplay": 4,
        "fieldName": "display",
        "fieldRemark": "排序",
        "fieldType": "int(10)",
        "fieldKey": "false"
    },
    {
        "fieldDisplay": 4,
        "fieldName": "create_person",
        "fieldRemark": "创建人",
        "fieldType": "varchar(32)",
        "fieldKey": "false"
    },
    {
        "fieldDisplay": 5,
        "fieldName": "create_date",
        "fieldRemark": "创建时间",
        "fieldType": "datetime",
        "fieldKey": "false"
    },
    {
        "fieldDisplay": 6,
        "fieldName": "update_person",
        "fieldRemark": "更新人",
        "fieldType": "varchar(32)",
        "fieldKey": "false"
    },
    {
        "fieldDisplay": 7,
        "fieldName": "update_date",
        "fieldRemark": "更新时间",
        "fieldType": "datetime",
        "fieldKey": "false"
    },
    {
        "fieldDisplay": 8,
        "fieldName": "delflag",
        "fieldRemark": "状态：0-正常1-删除",
        "fieldType": "int(4)",
        "fieldKey": "false"
    }
];
// 页面字段
var PAGE_FIELD = [
    {
        "pageFieldName": "id",
        "pageFieldRemark": "主键",
        "pageJavaVariable": "id",
        "pageJavaType": "Long",
        "pageFormValue": "",
        "pageFormType": "text",
        "defaultValue": "", "pageFormVerify": JSON.parse(JSON.stringify(PAGE_VERIFY)), "pagePlaceholder": ""
    },
    {
        "pageFieldName": "uuid",
        "pageFieldRemark": "唯一键",
        "pageJavaVariable": "id",
        "pageJavaType": "String",
        "pageFormValue": "",
        "pageFormType": "text",
        "defaultValue": "", "pageFormVerify": JSON.parse(JSON.stringify(PAGE_VERIFY)), "pagePlaceholder": ""
    },
    {
        "pageFieldName": "display",
        "pageFieldRemark": "排序",
        "pageJavaVariable": "display",
        "pageJavaType": "Integer",
        "pageFormValue": "",
        "pageFormType": "text",
        "defaultValue": "", "pageFormVerify": JSON.parse(JSON.stringify(PAGE_VERIFY)), "pagePlaceholder": ""
    },
    {
        "pageFieldName": "create_person",
        "pageFieldRemark": "创建人",
        "pageJavaVariable": "createPerson",
        "pageJavaType": "String",
        "pageFormValue": "",
        "pageFormType": "text",
        "defaultValue": "", "pageFormVerify": JSON.parse(JSON.stringify(PAGE_VERIFY)), "pagePlaceholder": ""
    },
    {
        "pageFieldName": "create_date",
        "pageFieldRemark": "创建时间",
        "pageJavaVariable": "createDate",
        "pageJavaType": "LocalDateTime",
        "pageFormValue": "",
        "pageFormType": "datetime",
        "defaultValue": "", "pageFormVerify": JSON.parse(JSON.stringify(PAGE_VERIFY)), "pagePlaceholder": "yyyy-MM-dd HH:mm:ss"
    },
    {
        "pageFieldName": "update_person",
        "pageFieldRemark": "更新人",
        "pageJavaVariable": "updatePerson",
        "pageJavaType": "String",
        "pageFormValue": "",
        "pageFormTypeSelect": "text",
        "defaultValue": "", "pageFormVerify": JSON.parse(JSON.stringify(PAGE_VERIFY)), "pagePlaceholder": ""
    },
    {
        "pageFieldName": "update_date",
        "pageFieldRemark": "更新时间",
        "pageJavaVariable": "updateDate",
        "pageJavaType": "LocalDateTime",
        "pageFormValue": "",
        "pageFormType": "datetime",
        "defaultValue": "", "pageFormVerify": JSON.parse(JSON.stringify(PAGE_VERIFY)), "pagePlaceholder": "yyyy-MM-dd HH:mm:ss"
    },
    {
        "pageFieldName": "delflag",
        "pageFieldRemark": "状态：0-正常1-删除",
        "pageJavaVariable": "delflag",
        "pageJavaType": "String",
        "pageFormValue": "",
        "pageFormTypeSelect": "text",
        "defaultValue": "", "pageFormVerify": JSON.parse(JSON.stringify(PAGE_VERIFY)), "pagePlaceholder": ""
    }
];




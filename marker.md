# 端口使用：
* 52001：路由中心
* 52002：编辑中心
* 52003：页面中心 page
* 52004：rabbitmq
* 52005: oauth2
* 52006: 上传中心 upload
* 52007: 模版中心 template
* 52008: 文本中心 text
* 52009: app 
* 52010: 模版中心
* 52011: 数据库基本操作中心
#返回代码说明:
* 0:   操作成功
* 101: 系统错误
* 3: 无权限
* 4: oath2 错误
* 5: token 无效
* 9: 缺少 access_token
* 6: feign 请求错误
* 7: oauth 错误
* 8: 超时

# template-center 模版使用：
1. 关闭 IDEA ，不要用 IDEA 复制粘贴
2. 在文件夹中复制粘贴 template-center 模版，然后删除文件夹中的 .iml 文件
3. 更改 pom.xml 文件的 <artifactId>template-center</artifactId> 与 <name>template-center</name> 的名称
4. 然后用 IDEA 打开，将模块添加到父 pom 中
5. 更改模块名称和项目名称，更改启动类名称，更改端口号

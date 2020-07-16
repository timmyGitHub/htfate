<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>授权</title>
</head>
<body>
<div class="container">
    <h1>${session}</h1>
    <h3>请求授权，该应用将获取你的以下信息</h3>
    <p>昵称，头像和性别</p>
    授权后表明你已同意 <a  href="#boot" style="color: #E9686B">OAUTH-BOOT 服务协议</a>
    <form method="post" action="/oauth/authorize">
        <input type="hidden" name="user_oauth_approval" value="true">
<#--        <input type="hidden" name="_csrf" value="${_csrf.getToken()}"/>-->
        <button class="btn" type="submit"> 同意/授权</button>
    </form>
</div>
</body>
</html>
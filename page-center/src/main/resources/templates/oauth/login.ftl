<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="icon" sizes="any" href="${contextPath}/images/icon/yht_128.ico">
    <title>login</title>
    <link rel="stylesheet" href="${contextPath}/third/bootstrap-4.4.1/bootstrap.min.css">
</head>
<body>
<script src="${contextPath}/third/jquery/jquery-3.4.1.min.js"></script>
<script src="${contextPath}/third/bootstrap-4.4.1/popper.min.js"></script>
<script src="${contextPath}/third/bootstrap-4.4.1/bootstrap.min.js"></script>
<script src="${contextPath}/third/vue/vue.min.js"></script>
<script src="${contextPath}/third/vue/vue-resource.min.js"></script>
<script src="${contextPath}/third/common/common.js"></script>
<script src="${contextPath}/third/common/YHTUtils.js"></script>

<div id="app">
    <div class="container" style="margin-top: 2rem">
        <form class="form-signin" method="post" action="javascript:;">
            <h2 class="form-signin-heading">Please sign in</h2>
            <p>
                <label for="username" class="sr-only">Username</label>
                <input type="text" id="username" v-model="username" name="username" class="form-control" placeholder="Username" required="" autofocus="">
            </p>
            <p>
                <label for="password" class="sr-only">Password</label>
                <input type="password" id="password" v-model="password" name="password" class="form-control" placeholder="Password" required="">
            </p>
            <#--            <input name="_csrf" type="hidden" value="f9ce3c84-77bf-40f1-9c79-2e39b4510e5b">-->
            <button class="btn btn-lg btn-primary btn-block" v-on:click="login">Sign in</button>
        </form>
    </div>

</div>
<script>
    var app = new Vue({
        el: '#app'
        , data: {
            cancelOrLogin: '50'
            , cancelOrLoginTip: ''
            , bLogin: false
            , bCancel: false
            , username: ''
            , password: ''
        }
        , methods: {
            login: function(){
                var that = this;
                if (!that.checkUser())
                    return;
                YHTUtils.showLoading();

                var postData = new FormData();
                postData.append("username",that.username);
                postData.append("password",that.password);

                that.$http.post(OAUTH_LOGIN_URL,postData
                ).then(function (res) {
                    YHTUtils.hideLoading();
                    res = res['data'];
                    if (0 === res['code']) {
                        YHTUtils.showTipEdge({"messages":"登录成功"});
                    } else if (401 === res['code']) {
                        YHTUtils.showTipEdge({"messages":res['msg'],"clazz":"warning"});
                    } else {
                        YHTUtils.showTipEdge({"messages":res['msg'],"clazz":"danger"});
                    }
                })
            },
            // 登录按钮监听，判断是登录还是取消
            change: function () {
                var that = this;
                // 登录
                if (that.bLogin) {
                    if (that.checkUser()) {
                        that.$http.post(OAUTH_LOGIN_URL, {
                            username: that.username,
                            password: that.password
                        }).then(function (res) {
                            console.log(res);
                            var data = res['body'];
                            if (data['success']) {

                            } else {
                                console.log(data['msg']);
                            }
                        });
                    }
                }
                // 取消登录
                else if (that.bCancel) {
                    that.setCancelOrLogin();
                    document.getElementById('login').reset();
                    $('#loginModal').modal('hide');
                }
                console.log(this.cancelOrLogin)
            }
            // 校验用户名和密码
            , checkUser: function () {
                var that = this;
                if (!that.username) {
                    that.setCancelOrLogin();
                    YHTUtils.showTipEdge({'messages':'用户名不能为空','clazz':'danger'});
                    return false;
                }
                if (!that.password) {
                    that.setCancelOrLogin();
                    YHTUtils.showTipEdge({'messages':'密码不能为空','clazz':'danger'});
                    return false;
                }
                return true;
            }
            // 重置登录按钮
            , setCancelOrLogin: function () {
                var that = this;
                that.cancelOrLogin = "50";
            }
        }
        , watch: {
            cancelOrLogin: function (val) {
                var that = this;
                if (val >= 90) {
                    that.cancelOrLoginTip = '登录？';
                    that.bLogin = true;
                    that.bCancel = false;
                } else if (val <= 10) {
                    that.cancelOrLoginTip = '取消？';
                    that.bLogin = false;
                    that.bCancel = true;
                } else {
                    that.cancelOrLoginTip = '';
                    that.bLogin = false;
                    that.bCancel = false;
                }
            }
        }
    });
</script>
<script>
    $(function () {
        $('#loginModal').modal('show');
    })
</script>
</body>
</html>
<!DOCTYPE html>
<html lang="zh_cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>Demo1</title>
    <link rel="stylesheet" href="https://static.heleeos.com/lib/layui/css/layui.css" media="all">
</head>
<body>
    <div id="demo" class="layui-container">
        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
            <blockquote class="layui-elem-quote">演示: cookie 不能跨域</blockquote>
        </fieldset>

        <div class="layui-row">
            <div class="layui-col-xs4">
                <fieldset class="layui-elem-field">
                    <legend>heleeos.com</legend>
                    <div class="layui-field-box">
                        <table class="layui-table">
                            <thead>
                            <tr>
                                <th>key</th>
                                <th>value</th>
                                <th>domain</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="cookie in cookies1">
                                <td>{{ cookie.name }}</td>
                                <td>{{ cookie.value }}</td>
                                <td>{{ cookie.domain }}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </fieldset>
            </div>
            <div class="layui-col-xs4">
                <fieldset class="layui-elem-field">
                    <legend>zpbig.com</legend>
                    <div class="layui-field-box">
                        <table class="layui-table">
                            <thead>
                            <tr>
                                <th>key</th>
                                <th>value</th>
                                <th>domain</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="cookie in cookies2">
                                <td>{{ cookie.name }}</td>
                                <td>{{ cookie.value }}</td>
                                <td>{{ cookie.domain }}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </fieldset>
            </div>
            <div class="layui-col-xs4">
                <fieldset class="layui-elem-field">
                    <legend>127.0.0.1</legend>
                    <div class="layui-field-box">
                        <table class="layui-table">
                            <thead>
                            <tr>
                                <th>key</th>
                                <th>value</th>
                                <th>domain</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="cookie in cookies3">
                                <td>{{ cookie.name }}</td>
                                <td>{{ cookie.value }}</td>
                                <td>{{ cookie.domain }}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </fieldset>
            </div>
        </div>
        <div class="layui-row">
            <fieldset class="layui-elem-field">
                <legend>设置Cookie</legend>
                <div class="layui-field-box">

                    <input type="radio" value="http://127.0.0.1:8080/setCookie" v-model="url" checked> 127.0.0.1
                    <input type="radio" value="https://heleeos.com/sso/sso-demo1/setCookie" v-model="url"> heleeos.com
                    <input type="radio" value="https://zpbig.com/sso/sso-demo1/setCookie" v-model="url"> zpbig.com

                    <div class="layui-form">
                        <div class="layui-form-item">
                            <label class="layui-form-label">key</label>
                            <div class="layui-input-block">
                                <input id="key" type="text" class="layui-input" v-model="key">
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">value</label>
                            <div class="layui-input-block">
                                <input id="value" type="text" class="layui-input" v-model="value">
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">domain</label>
                            <div class="layui-input-block">
                                <input id="domain" type="text" class="layui-input" v-model="domain">
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <div class="layui-input-block">
                                <button class="layui-btn" v-on:click="setCookie">设置Cookie</button>
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
    </div>

    <script src="https://static.heleeos.com/lib/jquery.js" charset="utf-8"></script>
    <script src="https://static.heleeos.com/lib/vue.js" charset="utf-8"></script>
    <script>
        var vm = new Vue({
            el : "#demo",
            data: {
                key : '',
                value : '',
                domain: '',
                url: '',
                cookies1 : [],
                cookies2 : [],
                cookies3 : []
            },
            methods: {
                loadCookie: function(url, index) {
                    $.get(url).done(function(res){
                        console.log(res);
                        if(res.code === 200) {
                            if(index === 1) vm.cookies1 = res.data;
                            if(index === 2) vm.cookies2 = res.data;
                            if(index === 3) vm.cookies3 = res.data;
                        }
                    }).fail(function(err){
                        console.log(err);
                    });
                },
                reload: function() {
                    vm.loadCookie("https://heleeos.com/sso/sso-demo1/getCookie.json", 1);
                    vm.loadCookie("https://zpbig.com/sso/sso-demo1/getCookie.json", 2);
                    vm.loadCookie("http://127.0.0.1:8080/getCookie.json", 3);
                },
                setCookie: function() {
                    $.get(vm.url, {"key" : vm.key, "value" : vm.value, "domain" : vm.domain}).done(function(res){
                        console.log(res);
                        vm.reload();
                    }).fail(function(err){
                        console.log(err);
                    });
                }
            }
        });
        vm.reload();
    </script>
</body>
</html>

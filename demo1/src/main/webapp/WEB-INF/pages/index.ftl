<!DOCTYPE html>
<html lang="zh_cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>Demo1</title>
    <link rel="stylesheet" href="/static/layui/css/layui.css" media="all">
</head>
<body>
    <div class="layui-container">
        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
            <blockquote class="layui-elem-quote">演示: cookie 不能跨域</blockquote>
        </fieldset>

        <div id="demo">
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
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>贤心</td>
                                    <td>汉族</td>
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
                                <colgroup>
                                    <col>
                                    <col>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>key</th>
                                    <th>value</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>贤心</td>
                                    <td>汉族</td>
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
        </div>

        <div class="layui-row">
            <fieldset class="layui-elem-field">
                <legend>设置Cookie</legend>
                <div class="layui-field-box">
                    <div class="layui-form">
                        <div class="layui-form-item">
                            <label class="layui-form-label">key</label>
                            <div class="layui-input-block">
                                <input type="text" autocomplete="off" class="layui-input">
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">value</label>
                            <div class="layui-input-block">
                                <input type="text" autocomplete="off" class="layui-input">
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">domain</label>
                            <div class="layui-input-block">
                                <input type="text" autocomplete="off" class="layui-input">
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">选择接口</label>
                            <div class="layui-input-inline">
                                <select name="modules" lay-verify="required" lay-search="">
                                    <option value="">heleeos.com</option>
                                    <option value="19">zpbig.com</option>
                                    <option value="20">127.0.0.1</option>
                                </select>
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <div class="layui-input-block">
                                <button class="layui-btn">设置Cookie</button>
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
    </div>

    <script src="/static/layui/layui.all.js" charset="utf-8"></script>
    <script src="/static/jquery.js" charset="utf-8"></script>
    <script src="/static/vue.js" charset="utf-8"></script>
    <script>
        layui.use('form', function(){
            var form = layui.form;

            var vm = new Vue({
                el : "#demo",
                data: {
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
                    init: function() {
                        vm.loadCookie("http://127.0.0.1:8080/getCookie.json", 3);
                    }
                }
            });
            vm.init();
        });


    </script>
</body>
</html>

package com.heleeos.sso.controller;

import com.heleeos.sso.bean.Result;
import com.heleeos.sso.util.ResultBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 *
 * Created by liyu on 2018/5/23.
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class IndexController {

    @RequestMapping({"/", "index.html"})
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("getCookie.json")
    public Result<List<Cookie>> getCookie(HttpServletRequest request) {
        return ResultBuilder.buildSuccess(Arrays.asList(Optional.ofNullable(request.getCookies()).orElse(new Cookie[]{})));
    }

    @GetMapping("setCookie")
    public Result<Cookie> setCookie(HttpServletRequest request, HttpServletResponse response) {
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        String domain = request.getParameter("domain");

        if(StringUtils.isBlank(key)) {
            return ResultBuilder.buildError("key 不能为空");
        }

        if(StringUtils.isBlank(value)) {
            return ResultBuilder.buildError("value 不能为空");
        }

        Cookie cookie = new Cookie(key, value);
        cookie.setDomain(domain);
        response.addCookie(cookie);
        return ResultBuilder.buildSuccess(cookie);
    }
}

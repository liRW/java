package com.gude.controller;

import com.gude.model.UserInfo;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
@Api(value = "示例-九炎核心系统-操作公司（必填）", description = "示例-操作公司接口描述（必填）")
@RestController
@RequestMapping("/api1/demo1")
public class Demo1RestController {
    @ApiOperation("示例-用户登录（必填）")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "示例-用户名（必填）", required = true, dataType = "string"),
            @ApiImplicitParam(name = "password", value = "示例-密码（必填）", required = true, dataType = "string")})
    @PostMapping("/login")
    @ResponseBody
    public UserInfo login(String username, String password) {
        UserInfo info = new UserInfo();
        info.setCreateTime(new Date());
        info.setId(111);
        return info;
    }
    @ApiOperation("示例-json返回测试（必填）")
    @GetMapping("/json")
    @ResponseBody
    public void testJson(@RequestBody @ApiParam(name="用户对象（必填）", value="传入json格式（必填）",required=true)UserInfo userInfo) {
    }
    @ApiIgnore  //隐藏
    @GetMapping("/hello")
    public ModelAndView helloWorld(UserInfo info) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("hell");
        return mv;
    }
}

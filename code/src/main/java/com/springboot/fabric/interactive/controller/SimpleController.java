package com.springboot.fabric.interactive.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.springboot.fabric.interactive.service.SimpleService;

@Controller
@RequestMapping("/simple")
public class SimpleController {

    @Autowired
    private SimpleService simpleService;

    @RequestMapping(value = "/chaincode", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String chaincode(@RequestBody Map<String, Object> map) {
        return simpleService.chainCode(new JSONObject(map));
    }

    @RequestMapping(value = "/trace", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String trace(@RequestBody Map<String, Object> map) {
        return simpleService.trace(new JSONObject(map));
    }
}

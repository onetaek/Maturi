package com.maturi.controller;

import com.maturi.exception.common.Sample404Exception;
import com.maturi.exception.common.Sample500Exception;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiTestController {

    @GetMapping("/api/test/error/400")
    @ResponseBody
    public void error404() {
        throw new Sample404Exception();
    }

    @GetMapping("/api/test/error/500")
    @ResponseBody
    public void error500() {
        throw new Sample500Exception();
    }

}

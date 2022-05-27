package com.rihao.property.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gaoy
 * 2020/2/26/026
 */
@Controller
@RequestMapping("/api/docs")
public class ApiDocController {

    @GetMapping
    public String swaggerDoc() {
        return "redirect:/swagger-ui.html";
    }
}

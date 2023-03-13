package com.aimer.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author :覃玉锦
 * @create :2023-03-13 21:01:00
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "xxxx");
        return "hello";
    }
}

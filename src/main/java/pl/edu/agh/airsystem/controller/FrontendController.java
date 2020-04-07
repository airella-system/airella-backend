package pl.edu.agh.airsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FrontendController {

    @GetMapping(value = {"/", ""})
    public String getIndex(HttpServletRequest request) {
        return "index.html";
    }

}
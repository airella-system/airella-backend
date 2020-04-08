package pl.edu.agh.airsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApiDocsController {

    @RequestMapping("/api/docs")
    public String apiDocs() {
        return "redirect:/swagger-ui.html";
    }

}
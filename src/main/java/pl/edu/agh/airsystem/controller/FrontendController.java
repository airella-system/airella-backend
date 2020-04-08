package pl.edu.agh.airsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller

public class FrontendController {

    // /{path:[^\.]+} matches any /path where path doesn't have any dot (isn't a file)
    // warning - it doesn't matches subdirectories

    // /{path:^(?!api).*}/**/{path:[^\.]*} matches any /path1/**/path2
    // where path1 isn't 'api' and last component of path (path2) doesn't
    // have any dot (isn't a file)

    @RequestMapping(value = {"/{path:[^\\.]+}", "/{path:^(?!api).*}/**/{path:[^\\.]*}"})
    public String getIndex(HttpServletRequest request) {
        return "/index.html";
    }

}
package at.ac.tuwien.cashtechthon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class IndexController extends AbstractController {

    @Override
    protected String getViewDir() {
        return "index";
    }

    @RequestMapping(value = "")
    public String index() {
        return "redirect:/app/index.html";
    }
}

package at.ac.tuwien.cashtechthon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

    @Override
    protected String getViewDir() {
        return "user";
    }
}

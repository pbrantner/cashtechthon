package at.ac.tuwien.cashtechthon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/login")
public class LoginController extends AbstractController {

    @Override
    protected String getViewDir() {
        return "login";
    }

    /**
     * Renders the login form.
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showLogin() {
        return createViewPath("login");
    }

    /**
     * Renders the login form with an additional error message.
     *
     * @return
     */
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String showLoginError(Model model) {
        model.addAttribute("loginError", true);
        return createViewPath("login");
    }
}

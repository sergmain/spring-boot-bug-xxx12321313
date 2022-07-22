package bug;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Sergio Lissner
 * Date: 7/21/2022
 * Time: 8:52 PM
 */
@Controller
public class RootController {

    @GetMapping("/index")
    public String index2() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

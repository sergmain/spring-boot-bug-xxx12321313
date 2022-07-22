package bug;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Sergio Lissner
 * Date: 7/21/2022
 * Time: 7:53 PM
 */
@Controller
public class SimpleController {

    @GetMapping(value = "/content/public/info")
    public String info() {
        return "content/public/info";
    }

    @GetMapping("/content/secured/info")
    public String securedInfo() {
        return "content/secured/info";
    }

}

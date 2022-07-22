package bug;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sergio Lissner
 * Date: 7/21/2022
 * Time: 7:53 PM
 */
@RestController
@RequestMapping("/rest/v1")
public class SimpleRestController {

    @GetMapping("/public/info")
    public String info() {
        return "Access to public info.";
    }

    @GetMapping("/secured/info")
    public String securedInfo() {
        return "Access to secured info.";
    }

}

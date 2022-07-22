package bug;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Sergio Lissner
 * Date: 7/21/2022
 * Time: 7:53 PM
 */
@SpringBootApplication
@Slf4j
public class SimpleApp {
    public static void main(String[] args) {
        SpringApplication.run(SimpleApp.class, args);
    }
}

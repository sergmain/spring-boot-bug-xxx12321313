package bug;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Sergio Lissner
 * Date: 7/21/2022
 * Time: 7:59 PM
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Configs {

    public static final String REST_REALM = "REST realm";

    @Configuration
    @Order(1)
    @Profile("profile26")
    public static class ConfigRest26 extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            initRestSecurity(http);
        }
    }

    @Configuration
    @Profile("profile26")
    public static class Config26 extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            initSecurity(http);
        }
    }

    @Bean
    @Order(0)
    @Profile("profile27")
    public SecurityFilterChain restFilterChain(HttpSecurity http) throws Exception {
        initRestSecurity(http);
        return http.build();
    }

    @Bean
    @Order(1)
    @Profile("profile27")
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        initSecurity(http);
        return http.build();
    }

    private static void initRestSecurity(HttpSecurity http) throws Exception {
        http
                .antMatcher("/rest/**/**").sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/rest/login", "/rest/v1/public/**/**").permitAll()
                .antMatchers("/rest/**/**").authenticated()
                .and()
                .antMatcher("/rest/**/**").httpBasic().realmName(REST_REALM)
                .and()
                .antMatcher("/rest/**/**").csrf().disable().headers().cacheControl();
    }

    private static void initSecurity(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .authorizeRequests()
                .antMatchers("/manager/html").denyAll()
                .antMatchers("/static/**/**", "/css/**", "/js/**", "/webjars/**").permitAll()
                .antMatchers("/favicon.ico", "/", "/index", "/content/public/**/**", "/login", "/jssc", "/error/**").permitAll()
                .antMatchers("/login").anonymous()
                .antMatchers("/logout", "/content/secured/**/**").authenticated()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER")
                .antMatchers("/**/**").denyAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .loginProcessingUrl("/jssc")
                .defaultSuccessUrl("/index")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/index");
    }
}

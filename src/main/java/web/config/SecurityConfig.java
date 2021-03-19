package web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import web.config.handler.CustomUrlLogoutSuccessHandler;
import web.config.handler.CustomAuthenticationFailureHandler;
import web.config.handler.CustomAuthenticationSuccessHandler;
import web.service.AppService;

@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // сервис, с помощью которого тащим пользователя
    private final AppService appService;

    // класс, в котором описана логика перенаправления пользователей по ролям
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    private final CustomUrlLogoutSuccessHandler urlLogoutSuccessHandler;

    public SecurityConfig(AppService appService,
                          CustomAuthenticationSuccessHandler authenticationSuccessHandler,
                          CustomAuthenticationFailureHandler authenticationFailureHandler,
                          CustomUrlLogoutSuccessHandler urlLogoutSuccessHandler) {
        this.appService = appService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.urlLogoutSuccessHandler = urlLogoutSuccessHandler;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appService).passwordEncoder(passwordEncoder()); // конфигурация для прохождения аутентификации
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests() // делаем страницу регистрации недоступной для авторизированных пользователей
                .antMatchers("/", "/css/*", "/js/*").permitAll()
                //.antMatchers("/login").anonymous() //страницы аутентификаци доступна всем
                .antMatchers("/admin/**  ").hasRole("ADMIN") // TODO проверить
                .antMatchers("/user/**  ").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated(); // защищенные URL
                //.antMatchers("/hello").access("hasAnyRole('ADMIN')").anyRequest().authenticated(); // защищенные URL
                //.and()
                // 403 error https://www.codeflow.site/ru/article/spring-security-custom-access-denied-page
                //.exceptionHandling().accessDeniedPage("/accessdenied"); // todo

        http.formLogin()
                .loginPage("/") // указываем страницу с формой логина
                .permitAll()  // даем доступ к форме логина всем
                .successHandler(authenticationSuccessHandler) //указываем логику обработки при удачном логине
                .failureHandler(authenticationFailureHandler)
                .usernameParameter("email") // Указываем параметры логина и пароля с формы логина
                .passwordParameter("password");
                //.and().csrf().configure(http); // todo

        http.logout()
                //.permitAll() // разрешаем делать логаут всем
                .logoutUrl("/logout")
                .logoutSuccessHandler(urlLogoutSuccessHandler) // todo
                //.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // https://docs.spring.io/spring-security/site/docs/4.2.12.RELEASE/apidocs/org/springframework/security/config/annotation/web/configurers/LogoutConfigurer.html
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/?logout"); // указываем URL при удачном логауте
                //.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // указываем URL логаута
                //.invalidateHttpSession(true); // сделать невалидной текущую сессию
                //.and().csrf().disable(); //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // TODO
/*    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }*/
}

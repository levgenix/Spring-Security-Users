package web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import web.config.handler.LoginSuccessHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("ADMIN").password("ADMIN").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login") // указываем страницу с формой логина
                .successHandler(new LoginSuccessHandler()) //указываем логику обработки при логине
                .loginProcessingUrl("/login") // указываем action с формы логина
                .usernameParameter("j_username") // Указываем параметры логина и пароля с формы логина
                .passwordParameter("j_password")
                .permitAll(); // даем доступ к форме логина всем

        http.logout()
                .permitAll() // разрешаем делать логаут всем
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // указываем URL логаута
                .logoutSuccessUrl("/login?logout") // указываем URL при удачном логауте
                .invalidateHttpSession(true) // сделать невалидной текущую сессию
                .and().csrf().disable(); //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)

        http.csrf().configure(http);

        http
                .authorizeRequests() // делаем страницу регистрации недоступной для авторизированных пользователей
                .antMatchers("/login").anonymous() //страницы аутентификаци доступна всем
                .antMatchers("/hello").access("hasAnyRole('ADMIN')").anyRequest().authenticated(); // защищенные URL
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}

package at.ac.tuwien.cashtechthon.config;

import at.ac.tuwien.cashtechthon.config.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/api/**");

        return registrationBean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/resources/**").permitAll()
                    .antMatchers("/lib/**").permitAll()
                    .antMatchers("/app/assets/**").permitAll()
                    .antMatchers("/api/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/app/index.html", true)
                    .failureUrl("/login/error")
                    .and()
                .logout().permitAll()
                .and().csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("thomas").password("test").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("michael").password("test").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("patrick").password("test").roles("USER");
        auth.inMemoryAuthentication().withUser("johannes").password("test").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("bernd").password("test").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("chris").password("test").roles("ADMIN");
    }
}

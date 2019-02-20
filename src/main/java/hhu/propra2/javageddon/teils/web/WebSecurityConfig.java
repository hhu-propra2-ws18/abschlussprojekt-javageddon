package hhu.propra2.javageddon.teils.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(jdbcTemplate.getDataSource())
                .usersByUsernameQuery(
                        "select name,password, true from benutzer where name=?")
                .authoritiesByUsernameQuery(
                        "select name, select rolle from benutzer where name=?");
        //TODO NutzerRolle ordentlich implementieren
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/registrieren","/fotos/*").permitAll()
                .antMatchers("/details").hasRole("ADMIN")
                //.anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/benutzer_anmelden")
                .defaultSuccessUrl("/",true)
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Bean
    @Override
    public JdbcUserDetailsManager userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
        manager.setJdbcTemplate(jdbcTemplate);
        return manager;
    }
//Passwörter werden PLAIN in der DB abgelegt, nicht schön aber simpel.
    //TODO bei ganz viel Langeweile ordentlich implementieren
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}
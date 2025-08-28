package org.esfe.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity {

    @Bean
    public UserDetailsManager customUsers(DataSource dataSource){
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        //users.setUsersByUsernameQuery("select nombre_usuario, clave, status from usuarios where nombre_usuario = ?");
        users.setUsersByUsernameQuery("""
        select 
            nombre_usuario as username,
            clave as password,
            status as enabled
        from usuarios
        where nombre_usuario = ?
        """);

        users.setAuthoritiesByUsernameQuery("select u.nombre_usuario, r.nombre from usuarios u " +
                "inner join roles r on r.id = u.id_rol " +
                "where u.nombre_usuario = ?");

        return users;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")                 // Página de login (GET)
                        .loginProcessingUrl("/login")        // Procesamiento (POST)
                        .defaultSuccessUrl("/", true)        // Dónde ir al loguearse
                        .failureUrl("/login?error=true")     // Dónde ir si falla
                        .usernameParameter("username")       // Debe coincidir con el name del input
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        return http.build();
    }



//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/assets/**", "/css/**", "/js/**").permitAll()
//                        .requestMatchers("/", "/privacy", "/terms").permitAll()
////                .requestMatchers("/assets/**", "/css/**", "/js/**","/images/**","/fonts/**","/error", "/", "/privacy", "/terms").permitAll()
//                .requestMatchers("/usuarios/**").hasAuthority("admin")
//                .anyRequest().authenticated()
//        );
//
//        http.formLogin(form -> form
//                        .loginPage("/login").permitAll());
//
////        http.formLogin(form -> form
////                .loginPage("/login").permitAll()
////                .defaultSuccessUrl("/", true)
////        );
//        return http.build();
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

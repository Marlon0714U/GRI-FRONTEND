package co.edu.uniquindio.gri.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * Clase WebSecurityConfig.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

/** Servicio encargado de obtener los detalles del usuario */
	@Autowired
	UserDetailsServiceImpl userDetailsService;

    //private final UserDetailsService userDetailsService;

    /**
     * Creates a BCrypt password encoder bean.
     *
     * @return BCryptPasswordEncoder instance.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates an in-memory user details service with predefined users.
     *
     * @param passwordEncoder The password encoder.
     * @return UserDetailsService instance.
     */
    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build());
        manager.createUser(User.withUsername("inge")
                .password(passwordEncoder.encode("password"))
                .roles("INGE")
                .build());
        return manager;
    }

   /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http HttpSecurity instance.
     * @return SecurityFilterChain instance.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authManagerRequestMatcherRegistry -> authManagerRequestMatcherRegistry
                            .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**", "/lib/**", "/favicon.ico").permitAll()
                            .requestMatchers("/inventario", "/reporteinventario", "/pertenencia", "/reportepertenencia").hasRole("ADMIN")
                            .requestMatchers("/general?id=6&type=f").hasRole("USER")
                            .anyRequest().authenticated())
                    .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error")
                        .usernameParameter("username")
                        .passwordParameter("password")
                    )
                    .logout(logout -> logout
                        .permitAll()
                        .logoutSuccessUrl("/login?logout")
                    );

            return http.build();
        }

         /*
    	 * (non-Javadoc)
    	 *
    	 * @see org.springframework.security.config.annotation.web.configuration.
    	 * WebSecurityConfigurerAdapter#configure
    	 * (org.springframework.security.config.annotation.web.builders.HttpSecurity) Se
    	 * permite el acceso a librerías y complementos. Se restringe el acceso al
    	 * inventario a los administradores. Se restringe el uso del resto de páginas
    	 * web a clientes autenticados.
    	 */
}

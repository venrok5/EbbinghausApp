package ru.alex.project.EbbinghausApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import ru.alex.project.EbbinghausApp.services.PersonDetailsService;

@Configuration
@EnableWebSecurity// @EnableMethodSecurity // prePostEnabled, @PreAuthorize/@PostAuthorize and @PreFilter/@PostFilter is by default set to true
public class SecurityConfig {
	
	private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    
    @Bean // security config
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
    	http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/auth/registration", "/error",
                		"/images/**", "/css/**", "/js/**").permitAll() // urls avaiabled for unauthorized users
                .anyRequest().authenticated() // all requests only for authorised users
               );
        
    	// login form
    	http.formLogin(formLogin -> formLogin
               .loginPage("/auth/login")
               .usernameParameter("email") // authorisation by email instead of username (default) 
               .loginProcessingUrl("/process_login") // action that starts login chain
               .defaultSuccessUrl("/user", true)
               .failureUrl("/auth/login?error")
               .permitAll()
       );
    	
      // logout
      http.logout(logout -> logout
    		   .logoutUrl("/logout").permitAll()
    		   .logoutSuccessUrl("/auth/login")
       );
              
      http.httpBasic(Customizer.withDefaults()); // for login via HTTP methods (not via form)
      
      http.headers(headers -> headers
  			.frameOptions(frameOptions -> frameOptions
  					.sameOrigin() // for working picture in picture for pdf
  				)
    		  );
      
         return http.build();
    }

    
    @Bean // Authentication config 
    public AuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    	
    	provider.setPasswordEncoder(getPasswordEncoder()); // decrypt passwords
    	provider.setUserDetailsService(personDetailsService); // BD relation
    	
    	return provider;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
  
}

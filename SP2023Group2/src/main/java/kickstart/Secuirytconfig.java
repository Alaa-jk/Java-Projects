package kickstart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import kickstart.personalverwaltung.PersonalRepository;
import kickstart.personalverwaltung.PersonalServiceImpl;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class Secuirytconfig  {

	
	private  PasswordEncoder passwordEncoder = null;
	

    private  PersonalRepository personalRepository = null;
 	


	@Bean
	public SecurityFilterChain  securityfilterchain(HttpSecurity http) throws Exception {
		
		return http.csrf().disable() 
		.authorizeHttpRequests()
		.antMatchers(
				"/login", 
				"/resources/**", 
				"/css/**", 
				"/fonts/**", 
				"/img/**").permitAll()
		.anyRequest().authenticated().and()
		.formLogin()
		.loginPage("/login").permitAll()
		.defaultSuccessUrl("/home").and().exceptionHandling()
		.accessDeniedPage("/accessDenied").and()
		.logout().invalidateHttpSession(true)
		.clearAuthentication(true)
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/login").permitAll().and().build();
		
		
	}
	
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new PersonalServiceImpl();
	}
	   @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	
	@Bean
	public AuthenticationProvider authenticationProvider() {
	     DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
	        authenticationProvider.setUserDetailsService(userDetailsService());
	        authenticationProvider.setPasswordEncoder(passwordEncoder());
	        return authenticationProvider;
	}

}

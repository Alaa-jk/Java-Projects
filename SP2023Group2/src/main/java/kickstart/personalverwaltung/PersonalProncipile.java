package kickstart.personalverwaltung;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class PersonalProncipile implements UserDetails {
	
	private Optional<Personal> personal;
	private List<GrantedAuthority> authorities;
	

	
	public PersonalProncipile (Optional<Personal> personal2) {
		
		this.personal=personal2;
		System.out.println(personal2.get().getRoles());
		authorities=Arrays.stream(personal2.get().getRoles().split(","))
				.map(SimpleGrantedAuthority::new ).collect(Collectors.toList());
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return authorities;
	}

	@Override
	public String getPassword() {
		
		return personal.get().getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return personal.get().getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}

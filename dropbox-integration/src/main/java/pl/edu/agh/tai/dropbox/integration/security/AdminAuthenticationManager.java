package pl.edu.agh.tai.dropbox.integration.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.model.User;

@Component
@Scope("session")
public class AdminAuthenticationManager extends AbstractAuthenticationManager {

	@Override
	public String getPassword(User user) {
		return user.getAdminPassword();
	}

	@Override
	public List<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(Role.USER));
		authorities.add(new SimpleGrantedAuthority(Role.ADMIN));
		return authorities;
	}

}

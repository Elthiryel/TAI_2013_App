package pl.edu.agh.tai.dropbox.integration.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SecurityHelper {

	public static boolean hasRole(final String role) {
		if (SecurityContextHolder.getContext().getAuthentication() == null)
			return false;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user.getAuthorities().contains(new SimpleGrantedAuthority(role));
	}

	public static User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null)
			return null;
		return (User) authentication.getPrincipal();
	}
}

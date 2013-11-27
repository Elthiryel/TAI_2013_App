package pl.edu.agh.tai.dropbox.integration.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * Security helper.
 * @author konrad
 *
 */
public class SecurityHelper {
	
	/**
	 * Checks if logged user has given role
	 * @param role
	 * @return true if hase, false in another case
	 */
	public static boolean hasRole(final String role) {
		if (SecurityContextHolder.getContext().getAuthentication() == null)
			return false;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user.getAuthorities().contains(new SimpleGrantedAuthority(role));
	}
	
	/**
	 * Gets authenticated user
	 * @return User or null if not authenticated
	 */
	public static User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null)
			return null;
		return (User) authentication.getPrincipal();
	}
}

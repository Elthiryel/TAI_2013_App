package pl.edu.agh.tai.dropbox.integration.security;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pl.edu.agh.tai.dropbox.integration.dao.UserDao;

/**
 * Abstract class provides authentication mechanism depends on role.
 * {@link AuthenticationManager}
 * @author konrad
 *
 */
public abstract class AbstractAuthenticationManager implements
		AuthenticationManager, Serializable {

	@Autowired
	private UserDao userDao;
	
	/**
	 * Authenticates and setr roles.
	 * 
	 * {@link AuthenticationManager#authenticate(Authentication)}
	 */
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String login = authentication.getName();
		String password = authentication.getCredentials().toString();
		pl.edu.agh.tai.dropbox.integration.model.User user = userDao
				.findByLogin(login);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (user != null && encoder.matches(password, getPassword(user))) {
			List<GrantedAuthority> authorities = getAuthorities();
			return new UsernamePasswordAuthenticationToken(new User(login,
					password, true, true, true, true, authorities), password,
					authorities);
		}
		throw new BadCredentialsException("Bad credentials");
	}
	
	/**
	 * Gets password
	 * @param user
	 * @return String password
	 */
	public abstract String getPassword(pl.edu.agh.tai.dropbox.integration.model.User user);
	
	/**
	 * Gets authorities
	 * @return List<GrantedAuthority> list of authorities
	 */
	public abstract List<GrantedAuthority> getAuthorities();
}

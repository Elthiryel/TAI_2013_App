package pl.edu.agh.tai.dropbox.integration.bean;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.model.User;

import com.dropbox.core.DbxWebAuth;

/**
 * Session bean for storing important data.
 * @author konrad
 *
 */
@Component("sessionData")
@Scope("session")
public class SessionData implements Serializable {
	
	/**
	 * Logged user
	 */
	private User user;
	
	/**
	 * User's redirected dropobox token
	 */
	private DbxWebAuth auth;
	
	/**
	 * Gets logged user
	 * @return User logged user
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Set user
	 * @param user logged user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Gets user's redirected dropobox token
	 * @return
	 */
	public DbxWebAuth getAuth() {
		return auth;
	}
	
	/**
	 * Set user's redirected dropobox token
	 * @param auth dropbox token
	 */
	public void setAuth(DbxWebAuth auth) {
		this.auth = auth;
	}
	
	/**
	 * Cleans data
	 */
	public void clearData(){
		user = null;
		auth = null;
	}
	
	
}

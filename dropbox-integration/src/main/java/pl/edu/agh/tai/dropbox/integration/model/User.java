package pl.edu.agh.tai.dropbox.integration.model;

import java.io.Serializable;

import javax.jdo.annotations.Unique;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.Min;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Entity class. Represents user data.
 * @author konrad
 *
 */
@Entity
public class User implements Serializable{
	
	@Unique(members ={"login"})
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String login = "";
	
	private String userPassword = "";
	
	private String adminPassword = "";
	
	private String dropboxToken;
	
	/**
	 * Gets user id
	 * @return Long user id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets user id
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Gets user login
	 * @return String user login
	 */
	public String getLogin() {
		return login;
	}
	
	/**
	 * Sets user login
	 * @param login
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * Gets encoded user password
	 * @return String user password
	 */
	public String getUserPassword() {
		return userPassword;
	}
	
	/**
	 * Sets use password
	 * @param userPassword
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	/**
	 * Gets encoded admin password
	 * @return String admin password
	 */
	public String getAdminPassword() {
		return adminPassword;
	}
	
	/**
	 * Sets admin password
	 * @param adminPassword
	 */
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	
	/**
	 * Gets dropbox token
	 * @return String dropbox token
	 */
	public String getDropboxToken() {
		return dropboxToken;
	}
	
	/**
	 * Sets doropbox token
	 * @param dropboxToken
	 */
	public void setDropboxToken(String dropboxToken) {
		this.dropboxToken = dropboxToken;
	}
	
	/**
	 * Encodes passwords before persisting.
	 */
	@PrePersist
	public void prePersist(){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		userPassword=encoder.encode(userPassword);
		adminPassword=encoder.encode(adminPassword);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

	
}

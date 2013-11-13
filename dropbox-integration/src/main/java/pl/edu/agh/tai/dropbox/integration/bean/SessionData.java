package pl.edu.agh.tai.dropbox.integration.bean;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.edu.agh.tai.dropbox.integration.model.User;

@Component
@Scope("session")
public class SessionData implements Serializable {

	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void clearData(){
		user = null;
	}
	
	
}

package pl.edu.agh.tai.dropbox.integration.test;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.agh.tai.dropbox.integration.dao.UserDao;
import pl.edu.agh.tai.dropbox.integration.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "/testContext.xml")
public class UserDaoTest  extends AbstractJUnit4SpringContextTests  {

	@Autowired
	private UserDao userDao;
	
	private User newTestUser(int random) {
		User user = new User();
		user.setLogin("test_user_login" + random);
		user.setUserPassword("test_user_password");
		user.setAdminPassword("test_asmin_password");
		user.setDropboxToken("");
		return user;
	}
	
	@Test
	public void createAndFindTest() {
		Random random = new Random();
		int randInt = random.nextInt();
		User user = newTestUser(randInt);
		userDao.create(user);
		Assert.assertNotNull(userDao.findByLogin("test_user_login" + randInt));
	}
	
	@Test
	public void uniqueTest() {
		Random random = new Random();
		int randInt = random.nextInt();
		User user = newTestUser(randInt);
		userDao.create(user);
		Assert.assertEquals(false, userDao.isLoginUnique("test_user_login" + randInt));
	}
}

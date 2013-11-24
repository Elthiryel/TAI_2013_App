package pl.edu.agh.tai.dropbox.integration.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.agh.tai.dropbox.integration.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "/testContext.xml")
public class UserDaoTest  extends AbstractJUnit4SpringContextTests  {

	@Autowired
	private UserDao userDao;
	
	@Test
	public void dummyTest(){
		Assert.assertNotNull(userDao.findByLogin("konrad2"));
	}
}

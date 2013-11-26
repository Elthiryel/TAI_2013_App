package pl.edu.agh.tai.dropbox.integration.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.agh.tai.dropbox.integration.model.User;

/**
 * DAO class for User entity.
 * @author konrad
 *
 */
@Repository
public class UserDao implements Serializable {

	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Creates and saves in database new user entity
	 * Requires transaction
	 * @param user
	 */
	@Transactional
	public void create(User user){
		em.persist(user);
		em.flush();
	}
	
	/**
	 * Finds user by login
	 * @param login - user login
	 * @return founded user or null if does not exists
	 */
	public User findByLogin(String login){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> criteria = cb.createQuery(User.class);
		Root<User> root = criteria.from(User.class);
		criteria.select(root);
		criteria.where(cb.equal(root.get("login"), login));
		try{
			return em.createQuery(criteria).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	/**
	 * Checks if login is unique
	 * @param login 
	 * @return true if login is unique, false in another case
	 */
	public boolean isLoginUnique(String login){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<User> root = criteria.from(User.class);
		criteria.select(cb.count(root));
		criteria.where(cb.equal(root.get("login"), login));
		Long result = em.createQuery(criteria).getSingleResult();
		if (result.equals(0L))
			return true;
		return false;
	}
}

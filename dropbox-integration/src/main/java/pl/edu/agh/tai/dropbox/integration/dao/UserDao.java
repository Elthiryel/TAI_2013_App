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

@Repository
public class UserDao implements Serializable {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void create(User user){
		em.persist(user);
		em.flush();
	}
	
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

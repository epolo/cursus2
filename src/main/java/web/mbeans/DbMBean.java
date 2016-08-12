package web.mbeans;

import db.entity.Courses;
import db.entity.Disciplines;
import db.entity.Users;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@ManagedBean(name = "db")
@ApplicationScoped
public class DbMBean {

	private EntityManagerFactory emf;
	
	public DbMBean() {
	}
	
	@PostConstruct
	private void init() {
		emf = Persistence.createEntityManagerFactory("cursus2PU");					
	}
	
	EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
/*	
	public List select(String sql, Object... params) {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createQuery(sql);
			int i = 1;
			for (Object p : params) {
				q.setParameter(i++, p);
			}
			return q.getResultList();
		} finally {
			em.close();
		}
	}
*/
	public <T> List<T> select(Class<T> cl, String sql, Object... params) {
		EntityManager em = getEntityManager();
		try {
			TypedQuery<T> q = em.createQuery(sql, cl);
			int i = 1;
			for (Object p : params) {
				q.setParameter(i++, p);
			}
			return q.getResultList();
		} finally {
			em.close();
		}
	}
	
	
	public List select(String sql, List params) {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createQuery(sql);
			int i = 1;
			for (Object p : params) {
				q.setParameter(i++, p);
			}
			return q.getResultList();
		} finally {
			em.close();
		}
	}
	
	public Object selectSingle(String sql, Object... params) {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createQuery(sql);
			int i = 1;
			for (Object p : params) {
				q.setParameter(i++, p);
			}
			return q.getSingleResult();
		} catch (NoResultException nre) {
			/** ignored **/
		} finally {
			em.close();
		}
		return null;
	}

	<T> T find(Class<T> cl, Object pk) {
		EntityManager em = getEntityManager();
		try {
			return em.find(cl, pk);
		} finally {
			em.close();
		}
	}
	
	public List<Courses> getCoursesList(boolean showDeleted, boolean showPast) {
		String sql = "SELECT o FROM Courses o";
		String where = null;
		if (!showDeleted)		where = " WHERE o.isDelete = false";
		if (!showPast)
			if (where == null)	where = " WHERE o.endsAt > CURRENT_DATE";
			else				where += " AND o.endsAt > CURRENT_DATE";
		if (where != null)		sql += where;
		return select(Courses.class, sql);
	}
	
	
	public List<Users> getUsers() {
		return select(Users.class, "SELECT o FROM Users o");
	}

	<T> T merge(T o) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			o = em.merge(o);
			em.getTransaction().commit();
			return o;
		} finally {
			em.close();
		}
	}

	<T> T persist(T o) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(o);
			em.merge(o);
			em.getTransaction().commit();
			return o;
		} finally {
			em.close();
		}
	}
	
	public List<Disciplines> getDiscWCoursesList() {
		return select(Disciplines.class, "select distinct c.disciplineId from Courses c where c.isDelete=false");
	}

	public List<Disciplines> getDiscList() {
		return select(Disciplines.class, "select o from Disciplines o");
	}

	<T> T findByUuid(Class<T> cl, String uuid) {
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> q = cb.createQuery(cl);
			Root<T> r = q.from(cl);
			q.where(cb.equal(r.get("uuid"), uuid));
			return em.createQuery(q).getSingleResult();
		} catch (NoResultException nre) {
			/** ignored **/
		} finally {
			em.close();
		}
		return null;
	}

	public void clearCache() {
		emf.getCache().evictAll();
	}

}

package org.academiadecodigo.hexaltistas.persistence.dao;


import org.academiadecodigo.hexaltistas.model.Model;
import org.academiadecodigo.hexaltistas.persistence.TransactionException;
import org.academiadecodigo.hexaltistas.persistence.jpa.JpaSessionManager;
import org.hibernate.HibernateException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class GenericJpaDao<T extends Model> implements Dao<T> {

    protected JpaSessionManager sm;
    protected Class<T> modelType;

    /**
     * Initializes a new {@code JPA DAO} instance given a model type
     *
     * @param modelType the model type
     */
    public GenericJpaDao(Class<T> modelType) {
        this.modelType = modelType;
    }

    /**
     * Sets the session manager
     *
     * @param sm the session manager to set
     */
    public void setSm(JpaSessionManager sm) {
        this.sm = sm;
    }

    /**
     * @see Dao#findAll()
     */
    @Override
    public List<T> findAll() {

        try {

            EntityManager em = sm.getCurrentSession();

            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(modelType);
            Root<T> root = criteriaQuery.from(modelType);
            return em.createQuery(criteriaQuery).getResultList();

            // Using JPQL
            // return em.createQuery( "from " + modelType.getSimpleName(), modelType).getResultList();


        } catch (HibernateException ex) {
            throw new TransactionException(ex);
        }
    }

    /**
     * @see Dao#findById(Integer)
     */
    @Override
    public T findById(Integer id) {

        try {

            EntityManager em = sm.getCurrentSession();
            return em.find(modelType, id);

        } catch (HibernateException ex) {
            throw new TransactionException(ex);
        }
    }

    /**
     * @see Dao#saveOrUpdate(Model)
     */
    @Override
    public T saveOrUpdate(T modelObject) {

        try {

            EntityManager em = sm.getCurrentSession();
            return em.merge(modelObject);

        } catch (HibernateException ex) {
            throw new TransactionException(ex);
        }
    }

    /**
     * @see Dao#delete(Integer)
     */
    @Override
    public void delete(Integer id) {

        try {

            EntityManager em = sm.getCurrentSession();
            em.remove(em.find(modelType, id));

        } catch (HibernateException ex) {
            throw new TransactionException(ex);
        }
    }
}
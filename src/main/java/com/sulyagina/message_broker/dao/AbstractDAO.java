package com.sulyagina.message_broker.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * Created by anastasia on 27.05.16.
 */
@Repository
@Transactional
public abstract class AbstractDAO<T extends Serializable>{
    private Class<T> clazz;

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public void setClazz(final Class<T> clazzToSet){
        clazz = clazzToSet;
    }

    public T findById(final long id) {
        return (T) getCurrentSession().get(clazz, id);
    }

    public List<T> findAll() {
        return getCurrentSession().createQuery("from " + clazz.getName()).list();
    }

    public void persist(final T entity) {
        getCurrentSession().persist(entity);
    }

    public void save(final T entity) {
        getCurrentSession().saveOrUpdate(entity);
    }
    public T update(final T entity) {
        return (T) getCurrentSession().merge(entity);
    }
    public void delete(final T entity) {
        getCurrentSession().delete(entity);
    }

    public void deleteById(final long id) {
        final T entity = findById(id);
        delete(entity);
    }

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}

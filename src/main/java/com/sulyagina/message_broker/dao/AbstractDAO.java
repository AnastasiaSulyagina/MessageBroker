package com.sulyagina.message_broker.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * Created by anastasia on 27.05.16.
 */
public abstract class AbstractDAO<T extends Serializable>{
    private Class<T> clazz;

    @Autowired
    private SessionFactory sessionFactory;

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

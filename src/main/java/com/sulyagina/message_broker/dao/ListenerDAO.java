package com.sulyagina.message_broker.dao;

import com.sulyagina.message_broker.components.Listener;
import com.sulyagina.message_broker.components.ListenerWrapper;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;

/**
 * Created by anastasia on 30.05.16.
 */
public class ListenerDAO <T> extends AbstractDAO {
    public ListenerWrapper getByListener(Listener listener) {
        Criteria cr = getCurrentSession().createCriteria(ListenerWrapper.class);
        cr.add(Restrictions.eq("listener", listener));
        return (ListenerWrapper) cr.uniqueResult();
    }
}

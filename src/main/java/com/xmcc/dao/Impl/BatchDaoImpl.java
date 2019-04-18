package com.xmcc.dao.Impl;

import com.xmcc.dao.BatchDao;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class BatchDaoImpl<T> implements BatchDao<T> {

    @PersistenceContext
    protected EntityManager em;

    @Override
    @Transactional
    public void batchInsert(List<T> list) {
        int size = list.size();
        //循环存入缓存区
        for (int i = 0; i < size; i++) {
            em.persist(list.get(i));
            if (i%100 == 0 || i==size-1){
                em.flush();
                em.clear();
            }
        }
    }
}

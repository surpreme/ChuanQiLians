package com.mbcq.commonlibrary.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.mbcq.commonlibrary.db.WebAreaDbInfo;

import com.mbcq.commonlibrary.greendao.WebAreaDbInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig webAreaDbInfoDaoConfig;

    private final WebAreaDbInfoDao webAreaDbInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        webAreaDbInfoDaoConfig = daoConfigMap.get(WebAreaDbInfoDao.class).clone();
        webAreaDbInfoDaoConfig.initIdentityScope(type);

        webAreaDbInfoDao = new WebAreaDbInfoDao(webAreaDbInfoDaoConfig, this);

        registerDao(WebAreaDbInfo.class, webAreaDbInfoDao);
    }
    
    public void clear() {
        webAreaDbInfoDaoConfig.clearIdentityScope();
    }

    public WebAreaDbInfoDao getWebAreaDbInfoDao() {
        return webAreaDbInfoDao;
    }

}

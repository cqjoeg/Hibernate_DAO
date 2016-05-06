package DAO.base;

import vo.PageBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2016/5/5.
 */
public  interface IHibernateDAO <T extends Serializable, ID extends Serializable>{
    // 项目中对数据库的通用型操作
    public abstract T create(final T entity);

    public abstract T findById(final ID id);

    public abstract void delete(final ID id);

    public abstract void update(final T entity);

    public abstract List<T> findAll();

    public abstract List<T> findByHQL(final String strHQL, final Object[] params);

    // 分页
    public abstract PageBean findByPage(final String strHQL,
                                        final int currentPage, final int pageSize, final Object[] params);

}

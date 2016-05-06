package DAO.base;

import hibernate.HibernateSessionFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import vo.PageBean;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by admin on 2016/5/5.
 */
public class CommonHibernateDAO <T extends Serializable, ID extends Serializable>
        implements IHibernateDAO<T, ID>{

    private Class<T> persistentClass;


    @SuppressWarnings("unchecked")
    public CommonHibernateDAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T create(T entity) {
        Session session = HibernateSessionFactory.getSession();
        Transaction trans = null;
        try{
            trans = session.beginTransaction();
            session.save(entity);
            trans.commit();
            return entity;
        } catch (RuntimeException ex){
            ex.printStackTrace();
            trans.rollback();
            return null;
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T findById(ID id) {
        Session session = HibernateSessionFactory.getSession();
        try{
            return (T)session.get(this.persistentClass, id);
        } catch (RuntimeException ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(ID id) {
        Session session = HibernateSessionFactory.getSession();
        Transaction trans = null;
        try{
            trans = session.beginTransaction();
            session.delete(findById(id));
            trans.commit();
        } catch (RuntimeException ex){
            ex.printStackTrace();
            trans.rollback();
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    @Override
    public void update(T entity) {
        Session session = HibernateSessionFactory.getSession();
        Transaction trans = null;
        try{
            trans = session.beginTransaction();
            session.update(entity);
            trans.commit();
        } catch (RuntimeException ex){
            ex.printStackTrace();
            trans.rollback();
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    @Override
    public List<T> findAll() {
        return findByCriteria();
    }

    @Override
    public List<T> findByHQL(String strHQL, Object[] params) {
        Session session = HibernateSessionFactory.getSession();
        try{
            Query query = session.createQuery(strHQL);
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
            return query.list();
        } catch (RuntimeException ex){
            ex.printStackTrace();
            return null;
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    @Override
    public PageBean findByPage(String strHQL, int currentPage, int pageSize, Object[] params) {
        // 步骤1：创建一个PageBean对象
        PageBean pageBean = new PageBean();
        // 步骤2：获取一个数据库链接session
        Session session = HibernateSessionFactory.getSession();
        try{
            // 步骤3：执行HQL语句完成查询动获取本页内的固定条数的数据
            Query query = session.createQuery(strHQL);
            // 步骤4：设置查询条件-参数条件
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
            // 步骤5：设置查询条件-每页的启始记录下标 (当前也是-1)*每页个数
            query.setFirstResult((currentPage-1)*pageSize);
            // 步骤6：设置查询条件-控制查询记录的个数
            query.setMaxResults(pageSize);
            // 步骤7：获取数据集合并且赋值给pageBean对象的data属性
            pageBean.setData(query.list());

            // 步骤8：将输入的HQL语句动态查分成符合返回记录个数的HQL语句
            strHQL = "select count(*) "+ strHQL.substring(strHQL.toLowerCase().indexOf("from"));
            // 步骤9：执行HQL语句完成查询动获取本页内的固定条数的数据
            query = session.createQuery(strHQL);
            // 步骤10：设置查询条件-参数条件
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
            // 步骤11：获取查询结果并且赋值给pageBean对象的totalRows
            pageBean.setTotalRows(Integer.parseInt(query.uniqueResult().toString()));
        } catch (RuntimeException ex){
            ex.printStackTrace();
            return null;
        } finally {
            // 关闭数据库连接
            HibernateSessionFactory.closeSession();
        }
        // 步骤12：为剩余的pageBean属性赋值
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);

        return pageBean;
    }


    // 内部方法Criteria接口查询
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(){
        Session session = HibernateSessionFactory.getSession();
        try{
            Criteria criteria = session.createCriteria(this.persistentClass);
            return criteria.list();
        } catch (RuntimeException ex){
            ex.printStackTrace();
            return null;
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}

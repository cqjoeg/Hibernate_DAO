package DAO.impl;

import DAO.ICustomers;
import DAO.base.CommonHibernateDAO;
import DAO.base.IHibernateDAO;
import bean.CustomersEntity;


/**
 * Created by admin on 2016/5/6.
 */

public class CustomersDAO_impl extends CommonHibernateDAO<CustomersEntity,Integer> implements
        ICustomers {


}

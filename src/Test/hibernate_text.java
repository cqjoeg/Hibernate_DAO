package Test;

import DAO.ICustomers;
import DAO.impl.CustomersDAO_impl;
import bean.CustomersEntity;
import vo.PageBean;

import java.util.Iterator;
import java.util.List;

/**
 * Created by admin on 2016/5/6.
 */
public class hibernate_text
{
    public static final void main(String[] args){
        ICustomers cdao = new CustomersDAO_impl();

        PageBean pb = cdao.findByPage("from CustomersEntity",4 ,10, new Object[]{});
        System.out.println("查询查询返回的所有记录个数："+pb.getTotalRows()+" 条");
        System.out.println("当前：第："+pb.getCurrentPage()+" / " +pb.getTotalPages()+" 页");
        System.out.println("本页内的数据如下：");

        List<CustomersEntity> data = pb.getData();
        Iterator<CustomersEntity> iter = data.iterator();
        while(iter.hasNext()){
            CustomersEntity dept = iter.next();
            System.out.println(dept.getCustomerId()+"-"+dept.getCompanyName()+"-"+dept.getContactName());
        }
    }
}

package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.po.Admin;
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface AdminRepository extends JpaRepository<Admin,String>,JpaSpecificationExecutor<Admin>{

    /**
     * 根据登录名查询管理员对象
     * @param loginname
     * @return
     */
    Admin findByLoginname(String loginname);
}

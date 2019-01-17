package com.tensquare.recruit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.recruit.po.Enterprise;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface EnterpriseRepository extends JpaRepository<Enterprise,String>,JpaSpecificationExecutor<Enterprise>{
	//属性表达式

    /**
     * 根据是否热门查询企业列表
     * @param ishot
     * @return
     */
    List<Enterprise> findByIshot(String ishot);
}

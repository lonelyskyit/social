package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.po.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemRepository extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    /**
     * 根据标签id查询问题分页列表，并且按照回答时间倒序排序
     * @param labelid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select problemid from Pl where labelid =?1) order by p.replytime desc")//JPQL,面向对象的，类似于sql
    Page<Problem> findByLabelidOrderByReplytimeDesc(String labelid, Pageable pageable);


}

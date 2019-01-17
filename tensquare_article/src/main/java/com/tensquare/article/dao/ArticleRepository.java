package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.po.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleRepository extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{

    /**
     * 根据id修改状态
     * @param id
     * @param state
     */
    @Query("update Article set state = ?2 where id = ?1")//readonle=true，自带事务
    @Modifying//一旦查询中使用增删改，必须加上该注解，表明是一个修改的查询。需要手动事务！
    void updateStateById(String id, String state);
}

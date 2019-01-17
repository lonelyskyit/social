package com.tensquare.qa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.po.Reply;
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ReplyRepository extends JpaRepository<Reply,String>,JpaSpecificationExecutor<Reply>{
	
}

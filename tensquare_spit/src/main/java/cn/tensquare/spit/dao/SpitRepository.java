package cn.tensquare.spit.dao;

import cn.tensquare.spit.po.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 吐槽的持久层
 */
public interface SpitRepository extends MongoRepository<Spit,String> {

    /**
     * 根据父id查询分页列表
     * @param parentid
     * @param pageable
     * @return
     */
    Page<Spit> findByParentid(String parentid, Pageable pageable);
}

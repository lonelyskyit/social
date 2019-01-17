package cn.tensquare.spit.service;

import cn.tensquare.spit.dao.SpitRepository;
import cn.tensquare.spit.po.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import utils.IdWorker;

import java.util.Date;
import java.util.List;

@Service
public class SpitService {
    @Autowired
    private SpitRepository spitRepository;

    @Autowired
    private IdWorker idWorker;

    /**
     * 增加
     *
     * @param spit
     */
    public void saveSpit(Spit spit) {
        //主键
        spit.setId(idWorker.nextId() + "");
        //默认值
        spit.setPublishtime(new Date());//发布时间为当前时间
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        //保存新的吐槽
        spitRepository.save(spit);
        //处理回复数-如果是子吐槽的话，要对父吐槽的回复数+1
        if(!StringUtils.isEmpty(spit.getParentid())){
            //是子吐槽,要对父吐槽的回复数+1
            mongoTemplate.updateFirst(
                    Query.query(Criteria.where("_id").is(spit.getParentid()))
                    ,new Update().inc("comment",1)
                    ,"spit"
            );
        }
    }

    /**
     * 修改
     *
     * @param spit
     */
    public void updateSpit(Spit spit) {
        spitRepository.save(spit);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteSpitById(String id) {
        spitRepository.deleteById(id);
    }

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<Spit> findSpitList() {
        return spitRepository.findAll();
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public Spit findSpitById(String id) {
        return spitRepository.findById(id).get();
    }


    /**
     * 根据上级ID查询吐槽列表
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findSpitListPageByParentid(String parentid, int page, int size){
        return spitRepository.findByParentid(parentid, PageRequest.of(page-1,size));
    }

    //注入MongoTemplate
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 使用底层的MongoTemplate工具类来直接操作
     * 根据吐槽的id，增加点赞的数量
     * @param id
     */
    public void updateSpitThumbupToIncrementing(String id){
        //匹配的条件
        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        //更新的数据对象
        Update update=new Update();
        //参数2：步进、递增的幅度
        update.inc("thumbup",1);
        //根据条件更新数据
        //参数1：匹配的条件
        //参数2：更新的数据对象
        //参数3:操作的集合名字或类名
        mongoTemplate.updateFirst(query,update,"spit");
    }

}

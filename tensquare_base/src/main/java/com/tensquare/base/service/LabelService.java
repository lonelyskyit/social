package com.tensquare.base.service;

import com.tensquare.base.dao.LabelRepository;
import com.tensquare.base.po.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import utils.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 标签管理的业务层
 */
@Service
public class LabelService {

    //注入dao
    @Autowired
    private LabelRepository labelRepository;

    //注入id生成器
    @Autowired
    private IdWorker idWorker;

    /**
     * 保存标签
     * @param label
     */
    public void saveLabel(Label label){
        //设置主键
        label.setId(idWorker.nextId()+"");
        labelRepository.save(label);
    }

    /**
     * 更新一个标签
     * @param label
     */
    public void updateLabel(Label label){
        labelRepository.save(label);
    }

    /**
     * 删除一个标签
     * @param id
     */
    public void deleteLabelById(String id){
        labelRepository.deleteById(id);
    }

    /**
     * 查询全部标签
     *
     * @return
     */
    public List<Label> findLabelList() {
        return labelRepository.findAll();
    }

    /**
     * 根据ID查询标签
     *
     * @return
     */
    public Label findLabelById(String id) {
        return labelRepository.findById(id).get();
    }


    /**
     * 复杂业务条件查询
     * @param searchMap
     * @return
     */
    public List<Label> findLabelList(Map<String,Object> searchMap){
        //业务条件对象
        Specification<Label> spec=getLabelSpecification(searchMap);

        //调用dao
        return labelRepository.findAll(spec);
    }

    /**
     * 组合条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public Page<Label> findLabelListPage(Map<String,Object> searchMap,int page,int size){
        //业务条件对象
        Specification<Label> spec = getLabelSpecification(searchMap);
        //请求的分页bean
        //注意：参数1：页码的索引，从0开始 zero-based page index.
        //参数2：每页最大记录数
        PageRequest pageRequest=PageRequest.of(page-1,size);
        //dao调用
        return labelRepository.findAll(spec,pageRequest);
    }

    /**
     * 根据map条件获取到spec条件对象
     * @param searchMap
     * @return
     */
    private Specification<Label> getLabelSpecification(Map<String, Object> searchMap) {
        return (root, query, cb) -> {
            //目标：条件的拼接

            //定义集合
            List<Predicate> andPredicateList=new ArrayList<>();

            //标签名称
            if(!StringUtils.isEmpty(searchMap.get("labelname"))){
                andPredicateList.add(
                        cb.like(root.get("labelname").as(String.class),"%"+searchMap.get("labelname")+"%"));
            }
            //状态
            if(!StringUtils.isEmpty(searchMap.get("state"))){
                andPredicateList.add(
                        cb.equal(root.get("state").as(String.class),searchMap.get("state")));
            }

            //是否推荐
            if(!StringUtils.isEmpty(searchMap.get("recommend"))){
                andPredicateList.add(
                        cb.equal(root.get("recommend").as(String.class),searchMap.get("recommend")));
            }

            //组合条件
            return cb.and(andPredicateList.toArray(new Predicate[andPredicateList.size()]));

        };
//        return spec;
    }

}

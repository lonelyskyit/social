package com.tensquare.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import javax.persistence.criteria.Predicate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import utils.IdWorker;

import com.tensquare.user.dao.AdminRepository;
import com.tensquare.user.po.Admin;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private IdWorker idWorker;

	//注入密码加密工具
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * 增加
	 * @param admin
	 */
	public void saveAdmin(Admin admin) {
		admin.setId( idWorker.nextId()+"" );
		admin.setState("1");
		//密码加密
		admin.setPassword(passwordEncoder.encode(admin.getPassword()));

		//保存
		adminRepository.save(admin);
	}

	/**
	 * 修改
	 * @param admin
	 */
	public void updateAdmin(Admin admin) {
		adminRepository.save(admin);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteAdminById(String id) {
		adminRepository.deleteById(id);
	}

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Admin> findAdminList() {
		return adminRepository.findAll();
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Admin findAdminById(String id) {
		return adminRepository.findById(id).get();
	}

	/**
	 * 根据条件查询列表
	 * @param whereMap
	 * @return
	 */
	public List<Admin> findAdminList(Map whereMap) {
		//构建Spec查询条件
        Specification<Admin> specification = getAdminSpecification(whereMap);
		//Specification条件查询
		return adminRepository.findAll(specification);
	}
	
	/**
	 * 组合条件分页查询
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Admin> findAdminListPage(Map whereMap, int page, int size) {
		//构建Spec查询条件
        Specification<Admin> specification = getAdminSpecification(whereMap);
		//构建请求的分页对象
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return adminRepository.findAll(specification, pageRequest);
	}

	/**
	 * 根据参数Map获取Spec条件对象
	 * @param searchMap
	 * @return
	 */
	private Specification<Admin> getAdminSpecification(Map searchMap) {

		return (Specification<Admin>) (root, query, cb) ->{
				//临时存放条件结果的集合
				List<Predicate> predicateList = new ArrayList<Predicate>();
				//属性条件
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 登陆名称
                if (searchMap.get("loginname")!=null && !"".equals(searchMap.get("loginname"))) {
                	predicateList.add(cb.like(root.get("loginname").as(String.class), "%"+(String)searchMap.get("loginname")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 状态
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                	predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
		
				//最后组合为and关系并返回
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));
		};

	}


	/**
	 * 根据登录名和密码来查询管理员对象
	 * @param loginname
	 * @param password
	 * @return
	 */
	public Admin findAdminByLoginnameAndPassword(String loginname,String password){

		//调用dao，根据用户名查询用户
		Admin admin = adminRepository.findByLoginname(loginname);
		//判断用户是否为空
		if(null == admin){
			//用户名不存在，则返回空
			return null;
		}
		//用户存在，判断密码是否一致
		if(passwordEncoder.matches(password,admin.getPassword())){
			//返回管理员对象
			return admin;
		}else{
			//密码不一致，也可以返回个性化的数据对象，或者抛异常
			return null;
		}
	}


}

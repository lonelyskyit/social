package com.tensquare.user.web.controller;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.user.po.Admin;
import com.tensquare.user.service.AdminService;

import dto.PageResultDTO;
import dto.ResultDTO;
import constants.StatusCode;
import utils.JwtUtil;

/**
 * 控制器层
 * @author BoBoLaoShi
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	
	/**
	 * 增加
	 * @param admin
	 */
	@PostMapping
	public ResultDTO add(@RequestBody Admin admin  ){
		adminService.saveAdmin(admin);
		return new ResultDTO(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param admin
	 */
	@PutMapping("/{id}")
	public ResultDTO edit(@RequestBody Admin admin, @PathVariable String id ){
		admin.setId(id);
		adminService.updateAdmin(admin);		
		return new ResultDTO(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping("/{id}")
	public ResultDTO remove(@PathVariable String id ){
		adminService.deleteAdminById(id);
		return new ResultDTO(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public ResultDTO list(){
		return new ResultDTO(true,StatusCode.OK,"查询成功",adminService.findAdminList());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping("/{id}")
	public ResultDTO listById(@PathVariable String id){
		return new ResultDTO(true,StatusCode.OK,"查询成功",adminService.findAdminById(id));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list( @RequestBody Map searchMap){
        return new ResultDTO(true,StatusCode.OK,"查询成功",adminService.findAdminList(searchMap));
    }

	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@PostMapping("/search/{page}/{size}")
	public ResultDTO listPage(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Admin> pageResponse = adminService.findAdminListPage(searchMap, page, size);
		return  new ResultDTO(true,StatusCode.OK,"查询成功",  new PageResultDTO<Admin>(pageResponse.getTotalElements(), pageResponse.getContent()) );
	}

	//注入jwt工具类
	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * 管理员登录
	 * @param loginMap
	 * @return
	 */
	@PostMapping("/login")
	public ResultDTO login(@RequestBody Map<String,String> loginMap){
		//调用业务层查询
		Admin admin =adminService.findAdminByLoginnameAndPassword(loginMap.get("loginname"),loginMap.get("password"));
		//判断
		if(null!=admin){
			//登录成功
			//签发jwt的token
			String token = jwtUtil.createJWT(admin.getId(), admin.getLoginname(), "admin");
			//建议使用map再封装一下
			Map<String,Object> map=new HashMap<>();
			map.put("token",token);
			//用来给前端直接显示用户名用的，如果前端需要其他的东东，则也可以继续添加
			map.put("name",admin.getLoginname());

			return new ResultDTO(true,StatusCode.OK,"登录成功",map);
		}else{
			//登录失败
			return new ResultDTO(false,StatusCode.LOGINERROR,"用户名或密码错误");
		}
	}
}

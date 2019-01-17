package com.tensquare.user.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.user.po.User;
import com.tensquare.user.service.UserService;

import dto.PageResultDTO;
import dto.ResultDTO;
import constants.StatusCode;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author BoBoLaoShi
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	/**
	 * 增加
	 * @param user
	 */
	@PostMapping
	public ResultDTO add(@RequestBody User user  ){
		userService.saveUser(user);
		return new ResultDTO(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@PutMapping("/{id}")
	public ResultDTO edit(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.updateUser(user);		
		return new ResultDTO(true,StatusCode.OK,"修改成功");
	}

	//注入HttpServletRequest
	@Autowired
	private HttpServletRequest request;
	//注入jwt工具类
	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping("/{id}")
	public ResultDTO remove(@PathVariable String id ){
		//限制：必须是管理员的身份才能删除用户--鉴权
		/*//1)获取头信息
		String authorizationHeader = request.getHeader("JwtAuthorization");
		//判断，是否为空
		if(null==authorizationHeader){
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足1");
		}

		//2）头信息规则：必须以“Bearer ”开头
		if(!authorizationHeader.startsWith("Bearer ")){
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足2");
		}

		//3)截取字符串，获取token，解析token
		String token = authorizationHeader.substring(7);
		Claims claims = null;
		try {
			claims = jwtUtil.parseJWT(token);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足3");
		}
		if(null==claims){
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足4");
		}
		//4)判断是否是管理员的身份
		if(!"admin".equals(claims.get("roles"))){
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足5");
		}*/
		//基于拦截器编程
        Claims claims = (Claims) request.getAttribute("admin_claims");
        //判断，token的载荷是否存在
        if(null ==claims){
            return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足");
        }

        //2.删除用户
		userService.deleteUserById(id);
		return new ResultDTO(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public ResultDTO list(){
		return new ResultDTO(true,StatusCode.OK,"查询成功",userService.findUserList());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping("/{id}")
	public ResultDTO listById(@PathVariable String id){
		return new ResultDTO(true,StatusCode.OK,"查询成功",userService.findUserById(id));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list( @RequestBody Map searchMap){
        return new ResultDTO(true,StatusCode.OK,"查询成功",userService.findUserList(searchMap));
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
		Page<User> pageResponse = userService.findUserListPage(searchMap, page, size);
		return  new ResultDTO(true,StatusCode.OK,"查询成功",  new PageResultDTO<User>(pageResponse.getTotalElements(), pageResponse.getContent()) );
	}

	/**
	 * 发送短信验证码
	 * @param mobile
	 * @return
	 */
	@PostMapping("/sendsms/{mobile}")
	public ResultDTO sendSmsCheckcode(@PathVariable String mobile){
		userService.saveSmsCheckcode(mobile);
		return  new ResultDTO(true,StatusCode.OK,"发送成功");
	}
	/**
	 * 用户注册
	 * @param user
	 */
	@PostMapping("/register/{checkcode}")
	public ResultDTO register(@RequestBody User user ,@PathVariable String checkcode ){
		userService.saveUser(user,checkcode);
		return new ResultDTO(true,StatusCode.OK,"用户注册成功");
	}

	/**
	 * 用户登录
	 * @param loginMap
	 * @return
	 */
	@PostMapping("/login")
	public ResultDTO login(@RequestBody Map<String,String> loginMap){
		//调用业务层查询
		User user=userService.findUserByMobileAndPassword(loginMap.get("mobile"),loginMap.get("password"));
		//判断
		if(null!=user){
			//登录成功
            //签发token
            String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
            //构建Map，用来封装token（最终转json）
            Map map=new HashMap();
            map.put("token",token);
            //用户昵称
            map.put("name",user.getNickname());
            //用户头像
            map.put("avatar",user.getAvatar());
            return new ResultDTO(true,StatusCode.OK,"登录成功",map);
		}else{
			//登录失败
			return new ResultDTO(false,StatusCode.LOGINERROR,"用户名或密码错误");
		}
	}

}

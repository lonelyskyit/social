package com.tensquare.recruit.web.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.recruit.po.Enterprise;
import com.tensquare.recruit.service.EnterpriseService;

import dto.PageResultDTO;
import dto.ResultDTO;
import constants.StatusCode;
/**
 * 控制器层
 * @author BoBoLaoShi
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/enterprise")
public class EnterpriseController {

	@Autowired
	private EnterpriseService enterpriseService;
	
	
	/**
	 * 增加
	 * @param enterprise
	 */
	@PostMapping
	public ResultDTO add(@RequestBody Enterprise enterprise  ){
		enterpriseService.saveEnterprise(enterprise);
		return new ResultDTO(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param enterprise
	 */
	@PutMapping("/{id}")
	public ResultDTO edit(@RequestBody Enterprise enterprise, @PathVariable String id ){
		enterprise.setId(id);
		enterpriseService.updateEnterprise(enterprise);		
		return new ResultDTO(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping("/{id}")
	public ResultDTO remove(@PathVariable String id ){
		enterpriseService.deleteEnterpriseById(id);
		return new ResultDTO(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public ResultDTO list(){
		return new ResultDTO(true,StatusCode.OK,"查询成功",enterpriseService.findEnterpriseList());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping("/{id}")
	public ResultDTO listById(@PathVariable String id){
		return new ResultDTO(true,StatusCode.OK,"查询成功",enterpriseService.findEnterpriseById(id));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list( @RequestBody Map searchMap){
        return new ResultDTO(true,StatusCode.OK,"查询成功",enterpriseService.findEnterpriseList(searchMap));
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
		Page<Enterprise> pageResponse = enterpriseService.findEnterpriseListPage(searchMap, page, size);
		return  new ResultDTO(true,StatusCode.OK,"查询成功",  new PageResultDTO<Enterprise>(pageResponse.getTotalElements(), pageResponse.getContent()) );
	}

	/**
	 * 查询热门企业
	 * @return
	 */
	@GetMapping("/search/hotlist")
	public ResultDTO listIshot(){
		return new ResultDTO(true, StatusCode.OK, "查询成功",enterpriseService.findEnterpriseListIshot());
	}
	
}

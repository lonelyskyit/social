package com.tensquare.recruit.web.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.recruit.po.Recruit;
import com.tensquare.recruit.service.RecruitService;

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
@RequestMapping("/recruit")
public class RecruitController {

	@Autowired
	private RecruitService recruitService;
	
	
	/**
	 * 增加
	 * @param recruit
	 */
	@PostMapping
	public ResultDTO add(@RequestBody Recruit recruit  ){
		recruitService.saveRecruit(recruit);
		return new ResultDTO(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param recruit
	 */
	@PutMapping("/{id}")
	public ResultDTO edit(@RequestBody Recruit recruit, @PathVariable String id ){
		recruit.setId(id);
		recruitService.updateRecruit(recruit);		
		return new ResultDTO(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping("/{id}")
	public ResultDTO remove(@PathVariable String id ){
		recruitService.deleteRecruitById(id);
		return new ResultDTO(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public ResultDTO list(){
		return new ResultDTO(true,StatusCode.OK,"查询成功",recruitService.findRecruitList());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping("/{id}")
	public ResultDTO listById(@PathVariable String id){
		return new ResultDTO(true,StatusCode.OK,"查询成功",recruitService.findRecruitById(id));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list( @RequestBody Map searchMap){
        return new ResultDTO(true,StatusCode.OK,"查询成功",recruitService.findRecruitList(searchMap));
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
		Page<Recruit> pageResponse = recruitService.findRecruitListPage(searchMap, page, size);
		return  new ResultDTO(true,StatusCode.OK,"查询成功",  new PageResultDTO<Recruit>(pageResponse.getTotalElements(), pageResponse.getContent()) );
	}


	/**
	 * 查询推荐的、最新职位列表的前4条记录
	 * @return
	 */
	@GetMapping("/search/recommend")
	public ResultDTO listRecommend(){
		return new ResultDTO(true, StatusCode.OK, "查询成功",recruitService.findRecruitListTop4Recommend());
	}
}

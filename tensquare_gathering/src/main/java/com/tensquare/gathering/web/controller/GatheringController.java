package com.tensquare.gathering.web.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.gathering.po.Gathering;
import com.tensquare.gathering.service.GatheringService;

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
@RequestMapping("/gathering")
public class GatheringController {

	@Autowired
	private GatheringService gatheringService;
	
	
	/**
	 * 增加
	 * @param gathering
	 */
	@PostMapping
	public ResultDTO add(@RequestBody Gathering gathering  ){
		gatheringService.saveGathering(gathering);
		return new ResultDTO(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param gathering
	 */
	@PutMapping("/{id}")
	public ResultDTO edit(@RequestBody Gathering gathering, @PathVariable String id ){
		gathering.setId(id);
		gatheringService.updateGathering(gathering);		
		return new ResultDTO(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping("/{id}")
	public ResultDTO remove(@PathVariable String id ){
		gatheringService.deleteGatheringById(id);
		return new ResultDTO(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public ResultDTO list(){
		return new ResultDTO(true,StatusCode.OK,"查询成功",gatheringService.findGatheringList());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping("/{id}")
	public ResultDTO listById(@PathVariable String id){
		return new ResultDTO(true,StatusCode.OK,"查询成功",gatheringService.findGatheringById(id));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list( @RequestBody Map searchMap){
        return new ResultDTO(true,StatusCode.OK,"查询成功",gatheringService.findGatheringList(searchMap));
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
		Page<Gathering> pageResponse = gatheringService.findGatheringListPage(searchMap, page, size);
		return  new ResultDTO(true,StatusCode.OK,"查询成功",  new PageResultDTO<Gathering>(pageResponse.getTotalElements(), pageResponse.getContent()) );
	}
	
}

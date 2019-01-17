package com.tensquare.article.web.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.article.po.Column;
import com.tensquare.article.service.ColumnService;

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
@RequestMapping("/column")
public class ColumnController {

	@Autowired
	private ColumnService columnService;
	
	
	/**
	 * 增加
	 * @param column
	 */
	@PostMapping
	public ResultDTO add(@RequestBody Column column  ){
		columnService.saveColumn(column);
		return new ResultDTO(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param column
	 */
	@PutMapping("/{id}")
	public ResultDTO edit(@RequestBody Column column, @PathVariable String id ){
		column.setId(id);
		columnService.updateColumn(column);		
		return new ResultDTO(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping("/{id}")
	public ResultDTO remove(@PathVariable String id ){
		columnService.deleteColumnById(id);
		return new ResultDTO(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public ResultDTO list(){
		return new ResultDTO(true,StatusCode.OK,"查询成功",columnService.findColumnList());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping("/{id}")
	public ResultDTO listById(@PathVariable String id){
		return new ResultDTO(true,StatusCode.OK,"查询成功",columnService.findColumnById(id));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list( @RequestBody Map searchMap){
        return new ResultDTO(true,StatusCode.OK,"查询成功",columnService.findColumnList(searchMap));
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
		Page<Column> pageResponse = columnService.findColumnListPage(searchMap, page, size);
		return  new ResultDTO(true,StatusCode.OK,"查询成功",  new PageResultDTO<Column>(pageResponse.getTotalElements(), pageResponse.getContent()) );
	}
	
}

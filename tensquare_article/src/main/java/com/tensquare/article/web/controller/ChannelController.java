package com.tensquare.article.web.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.article.po.Channel;
import com.tensquare.article.service.ChannelService;

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
@RequestMapping("/channel")
public class ChannelController {

	@Autowired
	private ChannelService channelService;
	
	
	/**
	 * 增加
	 * @param channel
	 */
	@PostMapping
	public ResultDTO add(@RequestBody Channel channel  ){
		channelService.saveChannel(channel);
		return new ResultDTO(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param channel
	 */
	@PutMapping("/{id}")
	public ResultDTO edit(@RequestBody Channel channel, @PathVariable String id ){
		channel.setId(id);
		channelService.updateChannel(channel);		
		return new ResultDTO(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping("/{id}")
	public ResultDTO remove(@PathVariable String id ){
		channelService.deleteChannelById(id);
		return new ResultDTO(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public ResultDTO list(){
		return new ResultDTO(true,StatusCode.OK,"查询成功",channelService.findChannelList());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping("/{id}")
	public ResultDTO listById(@PathVariable String id){
		return new ResultDTO(true,StatusCode.OK,"查询成功",channelService.findChannelById(id));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list( @RequestBody Map searchMap){
        return new ResultDTO(true,StatusCode.OK,"查询成功",channelService.findChannelList(searchMap));
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
		Page<Channel> pageResponse = channelService.findChannelListPage(searchMap, page, size);
		return  new ResultDTO(true,StatusCode.OK,"查询成功",  new PageResultDTO<Channel>(pageResponse.getTotalElements(), pageResponse.getContent()) );
	}
	
}

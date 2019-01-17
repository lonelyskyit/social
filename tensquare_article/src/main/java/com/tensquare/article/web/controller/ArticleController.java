package com.tensquare.article.web.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.article.po.Article;
import com.tensquare.article.service.ArticleService;

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
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	
	
	/**
	 * 增加
	 * @param article
	 */
	@PostMapping
	public ResultDTO add(@RequestBody Article article  ){
		articleService.saveArticle(article);
		return new ResultDTO(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param article
	 */
	@PutMapping("/{id}")
	public ResultDTO edit(@RequestBody Article article, @PathVariable String id ){
		article.setId(id);
		articleService.updateArticle(article);		
		return new ResultDTO(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping("/{id}")
	public ResultDTO remove(@PathVariable String id ){
		articleService.deleteArticleById(id);
		return new ResultDTO(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public ResultDTO list(){
		return new ResultDTO(true,StatusCode.OK,"查询成功",articleService.findArticleList());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping("/{id}")
	public ResultDTO listById(@PathVariable String id){
		return new ResultDTO(true,StatusCode.OK,"查询成功",articleService.findArticleById(id));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list( @RequestBody Map searchMap){
        return new ResultDTO(true,StatusCode.OK,"查询成功",articleService.findArticleList(searchMap));
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
		Page<Article> pageResponse = articleService.findArticleListPage(searchMap, page, size);
		return  new ResultDTO(true,StatusCode.OK,"查询成功",  new PageResultDTO<Article>(pageResponse.getTotalElements(), pageResponse.getContent()) );
	}
	/**
	 *根据文章id审核文章
	 * @param id
	 * @return
	 */
	@PutMapping("/examine/{id}")
	public ResultDTO examine(@PathVariable String id){
		articleService.updateArticleStateToExamine(id);
		return new ResultDTO(true,StatusCode.OK,"审核成功");
	}
}

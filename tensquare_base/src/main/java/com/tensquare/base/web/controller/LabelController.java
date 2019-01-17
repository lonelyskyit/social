package com.tensquare.base.web.controller;

import com.tensquare.base.po.Label;
import com.tensquare.base.service.LabelService;
import constants.StatusCode;
import dto.PageResultDTO;
import dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 标签管理的表现层
 */
//@Controller
//@ResponseBody
@RestController
@RequestMapping("/label")
@CrossOrigin//允许跨域请求
public class LabelController {
    //注入service
    @Autowired
    private LabelService labelService;

    /**
     * 添加标签
     * @param label
     * @return
     */
//    @PostMapping("/label")
    @PostMapping
//    @ResponseBody
    public ResultDTO add(@RequestBody Label label){
        //调用业务层
        labelService.saveLabel(label);
        //返回结果对象
        return new ResultDTO(true, StatusCode.OK,"添加成功");
    }
    /**
     * 修改编辑
     * @param label
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResultDTO edit(@RequestBody Label label,@PathVariable String id){
        label.setId(id);
        labelService.updateLabel(label);
        return new ResultDTO(true, StatusCode.OK,"修改成功");
    }

    /**
     * 根据id删除一个
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultDTO remove(@PathVariable String id){
        labelService.deleteLabelById(id);
        return new ResultDTO(true, StatusCode.OK,"删除成功");
    }


    /**
     * 查询所有
     * @return
     */
    @GetMapping
    public ResultDTO list(){
        //制造异常
//        int d=1/0;
        List<Label> list = labelService.findLabelList();
        return new ResultDTO(true, StatusCode.OK,"查询成功",list);
    }

    /**
     * 根据id查询一个
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultDTO listById(@PathVariable String id, HttpServletRequest request){
        //获取被调用时的服务端口
        System.out.println("当前服务端口："+request.getServerPort());
        Label label = labelService.findLabelById(id);
        return new ResultDTO(true, StatusCode.OK,"查询成功",label);
    }

    /**
     * 复杂条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list(@RequestBody Map<String,Object> searchMap){
        List<Label> list = labelService.findLabelList(searchMap);
        return new ResultDTO(true, StatusCode.OK,"查询成功",list);
    }

    /**
     * 组合条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public ResultDTO listPage(@RequestBody Map<String,Object> searchMap,@PathVariable int page,@PathVariable int size){

        Page<Label> pageResponse = labelService.findLabelListPage(searchMap, page, size);
        PageResultDTO pageResultDTO=new PageResultDTO(
                //总记录数
                pageResponse.getTotalElements()
                //当前页的列表
                ,pageResponse.getContent()
        );

        return new ResultDTO(true, StatusCode.OK,"查询成功",pageResultDTO);
    }

}

package cn.tensquare.spit.web.controller;

        import cn.tensquare.spit.po.Spit;
        import cn.tensquare.spit.service.SpitService;
        import constants.StatusCode;
        import dto.PageResultDTO;
        import dto.ResultDTO;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.Page;
        import org.springframework.data.redis.core.RedisTemplate;
        import org.springframework.data.redis.core.StringRedisTemplate;
        import org.springframework.web.bind.annotation.*;

        import java.util.concurrent.TimeUnit;

/**
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;
    /**
     * 增加
     * @param spit
     */
    @PostMapping
    public ResultDTO add(@RequestBody Spit spit){
        spitService.saveSpit(spit);
        return new ResultDTO(true, StatusCode.OK,"增加成功");
    }

    /**
     * 修改
     * @param spit
     */
    @PutMapping("/{id}")
    public ResultDTO edit(@RequestBody Spit spit, @PathVariable String id ){
        spit.setId(id);
        spitService.updateSpit(spit);
        return new ResultDTO(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     * @param id
     */
    @DeleteMapping("/{id}")
    public ResultDTO remove(@PathVariable String id ){
        spitService.deleteSpitById(id);
        return new ResultDTO(true,StatusCode.OK,"删除成功");
    }

    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public ResultDTO list(){
        return new ResultDTO(true,StatusCode.OK,"查询成功",spitService.findSpitList());
    }

    /**
     * 根据ID查询
     * @param id ID
     * @return
     */
    @GetMapping("/{id}")
    public ResultDTO listById(@PathVariable String id){
        return new ResultDTO(true,StatusCode.OK,"查询成功",spitService.findSpitById(id));
    }

    /**
     * 根据上级ID查询吐槽分页数据
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/comment/{parentid}/{page}/{size}")
    public ResultDTO listPageByParentid(@PathVariable String parentid , @PathVariable int page, @PathVariable int size){
        Page<Spit> pageResponse = spitService.findSpitListPageByParentid(parentid, page, size);
        return  new ResultDTO(true,StatusCode.OK,"查询成功",  new PageResultDTO<Spit>(pageResponse.getTotalElements(), pageResponse.getContent()) );
    }

    //注入RedisTemplate
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
//    @Autowired
//    private StringRedisTemplate redisTemplate;

    /**
     *  根据吐槽id，增加点赞的数量
     * @param id
     * @return
     */
    @PutMapping("/thumbup/{id}")
    public ResultDTO incrementThumbup(@PathVariable String id){
        //需求：控制同一个用户24小时内不能重复点赞
        //1)获取当前登录的用户，后边我们会修改为真正的当前登陆的用户（先写死）
        String userid="2023";
        //2)定义已经点赞的用户在Redis中存放的key
        String redisKey="thumbup_"+userid+"_"+ id;
        //判断是否存在
        if(null!=redisTemplate.opsForValue().get(redisKey)){
            //已经赞过了，不能重复点赞
            return new ResultDTO(false, StatusCode.REPERROR,"您已经点过赞了");
        }

        //还没有点赞
        spitService.updateSpitThumbupToIncrementing(id);
        //点赞成功，要在redis中放入一个标识
        //24小时有效
//        redisTemplate.opsForValue().set(redisKey,"1",1, TimeUnit.DAYS);
        //15秒有效
        redisTemplate.opsForValue().set(redisKey,"1",15, TimeUnit.SECONDS);
        return new ResultDTO(true,StatusCode.OK,"点赞成功");
    }
}

package com.ajie.controller;

import com.ajie.constant.MessageConstant;
import com.ajie.entity.CheckItem;
import com.ajie.result.CheckItemDeleteException;
import com.ajie.result.PageResult;
import com.ajie.result.QueryPageBean;
import com.ajie.result.Result;
import com.ajie.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*  检查项管理
 *  @Author 阿杰
 *  @create 2021年01月02日 17:29
 */
@RestController //包含controller并且返回json对象
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference //通多dubbo查找服务
    private CheckItemService checkItemService;

    @RequestMapping("/add")  //@RequestBody将前台传递的json参数解析成实体对象
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return checkItemService.findPage(queryPageBean);
    }

    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            checkItemService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            if (e instanceof CheckItemDeleteException) {
                return new Result(false, "已经有检查组关联到了该检查项，请优先删除检查组后再删除！");
            }
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/edit")  //@RequestBody将前台传递的json参数解析成实体对象
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    @RequestMapping("findAll")
    public Result findAll(){
        try {
            List<CheckItem> list = checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

}

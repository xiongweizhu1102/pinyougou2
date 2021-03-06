package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    private BrandService brandService;

    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }

    @RequestMapping("/findPage")

    public PageResult findPage(@RequestParam(name = "page") int pageNum, @RequestParam(name = "rows") int pageSize) {
        return brandService.findPage(pageNum, pageSize);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand tbBrand) {
        try {
            brandService.add(tbBrand);
            return new Result(true, "新增成功");
        } catch (Exception e) {
            return new Result(false, "新增失败");
        }
    }

    @RequestMapping("/findOne")
    public TbBrand findOne(Long id) {

        return brandService.findOne(id);
    }

    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand tbBrand) {
        try {
            brandService.update(tbBrand);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            return new Result(false, "修改失败");
        }
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            brandService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody TbBrand tbBrand, @RequestParam(name = "page") int pageNum, @RequestParam(name = "rows") int pageSize) {
        return brandService.findPage(tbBrand, pageNum, pageSize);
    }

    @RequestMapping("/selectOptionList")
    public  List<Map> selectOptionList(){
        return  brandService.selectOptionList();
    }
}

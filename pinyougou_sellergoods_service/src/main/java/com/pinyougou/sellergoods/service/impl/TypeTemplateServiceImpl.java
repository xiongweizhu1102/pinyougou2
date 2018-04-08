package com.pinyougou.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pinyougou.entity.PageResult;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.mapper.TbTypeTemplateMapper;
import com.pinyougou.pojo.*;
import com.pinyougou.pojo.TbTypeTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbTypeTemplateExample.Criteria;
import com.pinyougou.sellergoods.service.TypeTemplateService;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

	@Autowired
	private TbTypeTemplateMapper typeTemplateMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbTypeTemplate> findAll() {
		return typeTemplateMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbTypeTemplate> page=   (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);

			return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 缓存品牌和规格
	 */
	@Autowired
	private  RedisTemplate redisTemplate;
	private void saveToRedis(){
		//获取模板列表
		List<TbTypeTemplate> typeTemplateList = findAll();
		//循环模板得到品牌和规格需要进行json转换
		for (TbTypeTemplate tbTypeTemplate : typeTemplateList) {
			//品牌列表存储到缓存
			List<Map> brandList = JSON.parseArray(tbTypeTemplate.getBrandIds(),Map.class);
			redisTemplate.boundHashOps("brandList").put(tbTypeTemplate.getId(),brandList);
			//规格和规格选项列表缓存到redis中
			List<Map> specList = findSpecList(tbTypeTemplate.getId());
			redisTemplate.boundHashOps("specList").put(tbTypeTemplate.getId(),specList);
		}
		System.out.println("缓存了");
	}


	/**
	 * 增加
	 */
	@Override
	public void add(TbTypeTemplate typeTemplate) {
		typeTemplateMapper.insert(typeTemplate);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbTypeTemplate typeTemplate){
		typeTemplateMapper.updateByPrimaryKey(typeTemplate);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbTypeTemplate findOne(Long id){
		return typeTemplateMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			typeTemplateMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbTypeTemplateExample example=new TbTypeTemplateExample();
		Criteria criteria = example.createCriteria();
		
		if(typeTemplate!=null){			
						if(typeTemplate.getName()!=null && typeTemplate.getName().length()>0){
				criteria.andNameLike("%"+typeTemplate.getName()+"%");
			}
			if(typeTemplate.getSpecIds()!=null && typeTemplate.getSpecIds().length()>0){
				criteria.andSpecIdsLike("%"+typeTemplate.getSpecIds()+"%");
			}
			if(typeTemplate.getBrandIds()!=null && typeTemplate.getBrandIds().length()>0){
				criteria.andBrandIdsLike("%"+typeTemplate.getBrandIds()+"%");
			}
			if(typeTemplate.getCustomAttributeItems()!=null && typeTemplate.getCustomAttributeItems().length()>0){
				criteria.andCustomAttributeItemsLike("%"+typeTemplate.getCustomAttributeItems()+"%");
			}
	
		}
		
		Page<TbTypeTemplate> page= (Page<TbTypeTemplate>)typeTemplateMapper.selectByExample(example);
			//做完分页再缓存
			saveToRedis();
			//System.out.println("品牌和规格缓存了");
		return new PageResult(page.getTotal(), page.getResult());
	}
    @Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
    @Override
    public List<Map> findSpecList(Long id) {
        //根据id查询出模板
		TbTypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
		//将规格列表字符串转换为MapList
		List<Map> list = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);
		//遍历map去添加规格选项
		for (Map map : list) {
			//从map中拿到规格id,根据规格id去规格选项表中找对应的规格选项
			//创造查询条件
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			//需要将字符串的id转换为整形,Long型
			criteria.andSpecIdEqualTo(new Long((Integer)map.get("id")));
			List<TbSpecificationOption> options = specificationOptionMapper.selectByExample(example);
			map.put("options",options);
		}
		return  list;

	}

}

package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.entity.PageResult;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbBrandMapper brandMapper;
	@Autowired
	private TbSellerMapper sellerMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		//对商品状态进行设置
		goods.getGoods().setAuditStatus("0");
		//保存商品spu
		goodsMapper.insert(goods.getGoods());
		//事務控制
		//int x=1/0;
		//商品详细设置商品id
		goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
		//保存商品详细/扩展数据
		goodsDescMapper.insert(goods.getGoodsDesc());
		//如果启用规格才用规格保存
		if("1".equals(goods.getGoods().getIsEnableSpec())){
			//保存条目数据,先遍历条目集合
			for(TbItem item:goods.getItemList()){
				//标题
				String title=goods.getGoods().getGoodsName();
				//将item中的规格和规格列表字符串转换为Map
				Map<String,Object> specMap = JSON.parseObject(item.getSpec());
				//实际上title是加上规格对应的值
				for(String key:specMap.keySet()){
					title+=" "+ specMap.get(key);
				}
				item.setTitle(title);

				//设置其他通用属性
				setItemValues(goods,item);
				//插入数据
				itemMapper.insert(item);
			}

		}else{
			//不启用规格
			//创建条目对象
			TbItem item = new TbItem();
			//设置页面需要传递的属性
			//设置标题
			item.setTitle(goods.getGoods().getGoodsName());
			item.setPrice(goods.getGoods().getPrice());
			//设置状态
			item.setStatus("1");
			//设置默认显示
			item.setIsDefault("1");
			item.setNum(9999);
			item.setSpec("{}");
			setItemValues(goods,item);
			//插入数据
			itemMapper.insert(item);
		}
	}

	private void setItemValues(Goods goods,TbItem item){
		//这些都是页面通用的,除了规格
		item.setGoodsId(goods.getGoods().getId());//商品SPU编号
		item.setSellerId(goods.getGoods().getSellerId());//商家编号
		item.setCategoryid(goods.getGoods().getCategory3Id());//商品分类编号（3级）
		item.setCreateTime(new Date());//创建日期
		item.setUpdateTime(item.getCreateTime());//修改日期
		//设置品牌名称
		TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
		item.setBrand(brand.getName());
		//设置分类名称
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
		item.setCategory(itemCat.getName());
		//设置商家名称
		TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
		item.setSeller(seller.getNickName());
		//图片地址(取spu的第一个图片)
		List<Map> mapList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
		if(mapList.size()>0){
			item.setImage((String) mapList.get(0).get("url"));
		}
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){
		//设置goods的状态
		goods.getGoods().setAuditStatus("0");
		goodsMapper.updateByPrimaryKey(goods.getGoods());
		//修改商品描述
		goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());
		//删除原有的sku列表数据
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goods.getGoods().getId());
		itemMapper.deleteByExample(example);
		//重新添加sku
		if("1".equals(goods.getGoods().getIsEnableSpec())){
			//保存条目数据,先遍历条目集合
			for(TbItem item:goods.getItemList()){
				//标题
				String title=goods.getGoods().getGoodsName();
				//将item中的规格和规格列表字符串转换为Map
				Map<String,Object> specMap = JSON.parseObject(item.getSpec());
				//实际上title是加上规格对应的值
				for(String key:specMap.keySet()){
					title+=" "+ specMap.get(key);
				}
				item.setTitle(title);

				//设置其他通用属性
				setItemValues(goods,item);
				//插入数据
				itemMapper.insert(item);
			}

		}else{
			//不启用规格
			//创建条目对象
			TbItem item = new TbItem();
			//设置页面需要传递的属性
			//设置标题
			item.setTitle(goods.getGoods().getGoodsName());
			item.setPrice(goods.getGoods().getPrice());
			//设置状态
			item.setStatus("1");
			//设置默认显示
			item.setIsDefault("1");
			item.setNum(9999);
			item.setSpec("{}");
			setItemValues(goods,item);
			//插入数据
			itemMapper.insert(item);
		}

	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */

	@Override
	public Goods findOne(Long id){
		//查询pojo
		Goods goods = new Goods();
		//查询商品
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setGoods(tbGoods);
		//查询商品描述
		TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goods.setGoodsDesc(tbGoodsDesc);
		//查询SKU即实体类目
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);
		List<TbItem> itemList = itemMapper.selectByExample(example);
		goods.setItemList(itemList);
		return goods;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			//刪除是物理刪除,對是都刪除字段設置值1刪除,空未刪除
			tbGoods.setIsDelete("1");
			goodsMapper.updateByPrimaryKey(tbGoods);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {

		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		TbGoodsExample.Criteria criteria = example.createCriteria();
		criteria.andIsDeleteIsNull();//空沒有刪除
		if(goods!=null){			
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				//此地查询条件需要设置相等,不然模糊查询也有可能把别人的商品也查询出来了
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

    @Override
    public void updateStatus(Long[] ids, String status) {
		for (Long id : ids) {
			//根据id得到商品,持久态修改状态,更新
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}

}

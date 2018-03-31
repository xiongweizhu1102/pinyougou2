 //控制层 
app.controller('goodsController' ,function($scope,$controller   ,goodsService,itemCatService,uploadService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	//商品的添加
	$scope.add=function () {
		//获取文本编辑器里面的内容
		$scope.entity.goodsDesc.introduction=editor.html();
		goodsService.add($scope.entity).success(
			function (response) {
				if(response.success){
					alert('保存成功');
					$scope.entity={};
					//情空富文本编辑器里面的内容
					editor.html("");
				}else{
					alert(response.message);
				}
            }
		)
    }
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    /**
	 * 图片上传信息封装到一个image_entity里面,后续url又被赋值
     */
	$scope.uploadFile=function () {
		uploadService.uploadFile().success(
			function (response) {
				if(response.success){
					$scope.image_entity.url=response.message;
					console.log(response);
				}else{
					alert(response.message)
				}
            }
		).error(function () {
			alert("上传发送错误")
        })
    }

	//定义goods实体非tbGoods实体(需要传许多选择的规格选项过去,故要去添加specificationItems属性.
	$scope.entity={goods:{},goodsDesc:{itemImages:[],customAttributeItems:[],specificationItems:[]}};
	//添加多张图片,图片回显到图片列表  能上传多张图片
	$scope.add_image_entity=function () {//图片实体赋值到商品描述的图片集合里面
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity)
    }
    //將图片文件名的名字置空
	$scope.restFile=function () {
		$("#file").val("");
    }
    //删除图片,就是从数组中删除元素
	$scope.remove_image_entity=function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }
    //读取一级分类,就是父id为0
	$scope.selectItemCart1List=function () {
		itemCatService.findByParentId(0).success(
			function(response){
				$scope.itemCart1List=response;
		}
		)
    };
    //一级分类选择触发二级分类
    /**
	 * 第一个参数是页面进行绑定的变量,如果其发生变化触发函数
	 * newValue是改变后的值,oldValue是之前的值
     */
	$scope.$watch('entity.goods.category1Id',function (newValue,oldValue) {
            itemCatService.findByParentId(newValue).success(
                function(response){
                    $scope.itemCart2List=response;
                }
            )
    })
	//二级级分类选择触发三级级分类
    /**
	 * 第一个参数是页面进行绑定的变量,如果其发生变化触发函数
	 * newValue是改变后的值,oldValue是之前的值
     */
	$scope.$watch('entity.goods.category2Id',function (newValue,oldValue) {
            itemCatService.findByParentId(newValue).success(
                function(response){
                    $scope.itemCart3List=response;
                }
            )
    })
    //三级级分类选择触发模板id
    /**
     * 第一个参数是页面进行绑定的变量,如果其发生变化触发函数
     * newValue是改变后的值,oldValue是之前的值
	 * 去查询分类的typeid就是模板的id,且模板已经在pojo中查询出来了,只需要进行赋值即可
	 * 查询模板typeid字需要分类id查询一个实体就行findOne
	 * 此地pojo里面的分类实体和商品实体都有typeid,去商品的,商品typeid可以去取模板,进而管理品牌,此时因为
	 * 商品要保存,需要进行传商品中的模板id
     */
    $scope.$watch('entity.goods.category3Id',function (newValue,oldValue) {
        itemCatService.findOne(newValue).success(
            function(response){
                $scope.entity.goods.typeTemplateId=response.typeId;
            }
        )
    })
	//模板确定了触发更新品牌/扩展属性
	//模板id确定了,确定了模板,去查询出品牌/扩展属性集合进行遍历
    $scope.$watch('entity.goods.typeTemplateId',function (newValue,oldValue) {
        if(newValue!=null||newValue!=undefined){
            typeTemplateService.findOne(newValue).success(
                function(response){
                    //找到模板
                    $scope.typeTemplate=response;
                    //模板得到品牌列表/扩展属性进行展示
                    //后端传递过来的json字符串转换为json对象
                    //品牌列表
                    $scope.typeTemplate.brandIds=JSON.parse(	$scope.typeTemplate.brandIds);
                    //扩展属性,是赋值给pojo的商品描述实体的扩展属性map集合
                    $scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.typeTemplate.customAttributeItems);
                }
            )


		//查询规格和规格选项列表
			typeTemplateService.findSpecList(newValue).success(
				function (response) {
					//页面绑定的规格和规格选项列表被后台查询出来的规格和规格选项列表赋值
					$scope.specList=response;
                }
			)
        }
    })
	//页面绑定的表达式属性entity.goodsDesc.specticationItems
	//选择规格选项更新specificationItems属性
    /**
	 * 对象就是entity.goodsDesc.specticationItems遍历所得到的包含规格和规格选项的对象
     * @param $event 选择的事件
     * @param name 对象的属性名attributeName对应的值
     * @param value 对象的属性值attributeValue对应的值
	 * 	//[{"attributeName":"网络制式","attributeValue":["移动4G"]},{"attributeName":"屏幕尺寸","attributeValue":["5.5寸","4.5寸"]}]
     */
		$scope.updateSpecAttribute=function ($event,name,value) {
			//定义一个变量接收查询集合中的对象的属性名[attributeName]是否已经存在的对象
			var object=$scope.searchObjecByKey($scope.entity.goodsDesc.specificationItems,'attributeName',name);

			//如果对象集合不为空,就在属性值数组中添加选项规格的值
			if (object!=null){
				//如果被勾选
				if($event.target.checked){
					//对象的属性值数组中要添加值
					object.attributeValue.push(value)
				}else{
					//没有勾选,就是从对象的属性值数组中要移除值
					//移除值就根据值的索引去移除,
					// 值的索引就去就拿值去数组object.attributeValue中拿索引
                    object.attributeValue.splice(object.attributeValue.indexOf(value),1);
                    //如果选项规格都取消了,规格没意义了,就移除这个对象
					if(object.attributeValue.length==0){
						//从集合中移除这个对象
						$scope.entity.goodsDesc.specificationItems.splice(
                            $scope.entity.goodsDesc.specificationItems.indexOf(object),1);
					}
				}
			}else{
				//如果查询为空,也要创建一个对象来接收存进去的值
                $scope.entity.goodsDesc.specificationItems.push(
					{"attributeName":name,"attributeValue":[value]}
				);
			}
        }
		//创建SKU列表,排列列表就是pojo的条目集合list<TbItem>
    $scope.createItemList=function () {
		//定义原始集合只有一条记录 TbItem,对应数据库的tb_item表
		$scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0'}];
      //  $scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0' } ]
		//定义一个变量是规格和规格选项集合
       // spic--》[{"attributeName":"网络","attributeValue":["移动3G","移动4G"]},{"attributeName":"机身内存","attributeValue":["16G"]}]
		var items=$scope.entity.goodsDesc.specificationItems;
        //遍历规格和规格选项集合实现sku列表
        for(var i=0;i<items.length;i++){
			//根据规格选项和规格集合items及旧条目集合$scope.entity.itemList去影响条目集合
			//涉及到属性名和属性值数组
			//加列addColumn其实就是加行和加列
            $scope.entity.itemList=addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue)
		}

    }

    //原条目集合基础上进行增加行和列
    addColumn=function (list, columnName, conlumnValues) {
        var newList=[];//新的集合如果指向旧集合就是浅克隆
        //对条目集合进行遍历
        for(var i=0;i<list.length;i++){
            //先取得旧的条目集合的记录
            var oldRow=list[i];
            //对点击规格选项触发规格和规格选项列表的对象中的属性值数组发生变化,依据这个
            //去改变旧的条目集合
            for(var j=0;j<conlumnValues.length;j++){
                //将旧的条目对象转换为新的条目对象
                //旧的记录序列化成字符串在转换成json对象,就是一顿操作,就实现了深克隆
                var newRow=JSON.parse(JSON.stringify(oldRow));
                //对象中根据key去取值等于对应值属性对应的值
				//{"spec":{"网络":"移动3G","机身内存":"32G"},"price":0,"num":99999,"status":"0","isDefault":"0"}
                newRow.spec[columnName]=conlumnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    }



});

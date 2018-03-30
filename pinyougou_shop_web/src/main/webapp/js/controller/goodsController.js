 //控制层 
app.controller('goodsController' ,function($scope,$controller   ,goodsService,itemCatService,uploadService){
	
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

	//定义goods实体非tbGoods实体
	$scope.entity={goods:{},goodsDesc:{itemImages:[]}};
	//添加多张图片,图片回显到图片列表  能上传多张图片
	$scope.add_image_entity=function () {//图片实体赋值到商品描述的图片集合里面
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity)
    }
    //删除图片,就是从数组中删除元素
	$scope.remove_image_entity=function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }
    
});	

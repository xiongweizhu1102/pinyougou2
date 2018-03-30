 //控制层 
app.controller('itemCatController' ,function($scope,$controller   ,itemCatService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			//查询时就把父类id保存了下来,此时就是为了给添加分类添加东西的父类id进行赋值
			$scope.entity.parerntId=$scope.parerntId;
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 是传递了父类id
		        	$scope.findByParentId($scope.parerntId);
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
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
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	$scope.parerntId=0;
    //根据上级id查到本级的分类列表
	$scope.findByParentId=function (parentId) {
		$scope.parerntId=parentId;
		itemCatService.findByParentId(parentId).success(
			function (response) {
				$scope.list=response;
            }
		)
    }
    //定义两个变量表示分类的级数,此地应该是本级实体,非本级id和本级名字,不然名字显示不了,没本级实体即本级id无法查询,
	//默认级数是1,上级是0,即顶级目录,是固定的
	$scope.grade=1;
	//点击下一级进行查询,是将本级的实体赋值给它,先赋值,并作为下一级的父级id进行查询
	$scope.setGrade=function (value) {
		$scope.grade=value;
    }
    //点击面包屑进行查询就是传递了本级的实体id,就是将本级实体赋值给它,为什么会赋值,查询出来就赋值了,并作为下一级的父级id进行查询
	$scope.selectList=function (p_entity) {
		if($scope.grade==1){//第一级就是顶级,二级没有,三级没有
			$scope.entity_1=null;
			$scope.entity_2=null;
		}
		if($scope.grade==2){//,二级有,三级没有
			$scope.entity_1=p_entity;
			$scope.entity_2=null;
		}
        if($scope.grade==3){//,二级有,三级有
            $scope.entity_2=p_entity;
        }
        //赋值后进行查询下级列表查询
		$scope.findByParentId(p_entity.id)
    }
});	

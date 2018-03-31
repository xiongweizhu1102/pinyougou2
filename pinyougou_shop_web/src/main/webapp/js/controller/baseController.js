 //通用控制层
 //模块与通用控制层进行绑定,同时需要传递数据绑定
app.controller('baseController' ,function($scope){	
	
    //重新加载列表 数据
    $scope.reloadList=function(){
    	//切换页码  
    	$scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);	   	
    }
    
	//分页控件配置 
	$scope.paginationConf = {
         currentPage: 1,
         totalItems: 10,
         itemsPerPage: 10,
         perPageOptions: [10, 20, 30, 40, 50],
         onChange: function(){
        	 $scope.reloadList();//重新加载
     	 }
	}; 
	
	$scope.selectIds=[];//选中的ID集合 

	//更新复选
	$scope.updateSelection = function($event, id) {		
		if($event.target.checked){//如果是被选中,则增加到数组
			$scope.selectIds.push( id);			
		}else{
			var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);//删除 
		}
	}
	$scope.jsonToString=function (jsonString,key) {
		var json=JSON.parse(jsonString);//将json字符串转换为json对象
		var value="";
		for (var i=0;i<json.length;i++){
			if(i>0){
				value+=',';
			}
			value+=json[i][key];
		}
		return value;
    }

    //entity.goodsDesc.specticationItems就是选择了规格选项进行保存的地方,格式如下
	//[{"attributeName":"网络制式","attributeValue":["移动4G"]},{"attributeName":"屏幕尺寸","attributeValue":["5.5寸","4.5寸"]}]
	//在通用的controller定义一个查询集合对象中的属性名对应的属性值是否存在,存在往对象的属性值的值(数组)里面
	//进行存值,不存在,就建立对象就往对象的属性名存值和,对象的属性值存值.其实就是根据对象(map)的key去进行查值
    /**
	 *
     * @param list entity.goodsDesc.specticationItems就是选择了规格选项进行保存的地方
     * @param key attributeName
     * @param keyValue 网络制式
     */
	$scope.searchObjecByKey= function (list,key,keyValue) {
		for(var i=0;i<list.length;i++){
			//list[i]取对象,list[i][key]就是对象中key对应的值
			if(list[i][key]==keyValue){
				return list[i];
			}
		}
		//如果没有就返回空
		return null;
    }

});	
//服务层
//app和服务层进行绑定,向控制层进行提供调用
//同时服务层的内置服务和服务器进行交互得到需要的数据
//业务层就是本层,就用this替代,且向外返回一个去服务器的请求就行
app.service('brandService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../brand/findAll.do');		
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../brand/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../brand/findOne.do?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../brand/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../brand/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../brand/delete.do?ids='+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../brand/search.do?page='+page+"&rows="+rows, searchEntity);
	}
	//下拉框表数据
	this.selectOptionList=function () {
		return $http.get("../brand/selectOptionList.do")
    }
});

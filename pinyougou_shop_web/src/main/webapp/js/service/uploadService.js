app.service("uploadService",function ($http) {
    this.uploadFile=function () {
        //定义表单传递数据
        var formData=new FormData();
        //表单数据加上文件名和对应多个文件
        formData.append("file",file.files[0]);
        return $http({
            method:'post',
            url:"../upload.do",//此地是在good_edit.html页面
            data:formData,
            //默认是application/json,定义undifined,传递数据类型为multipart/form-data.
            headers:{"Content-Type":undefined },
            //序列化我们的表单数据,二进制流
            transformRequest:angular.identity

        });
    }
})
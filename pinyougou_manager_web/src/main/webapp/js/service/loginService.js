app.service("loginService",function ($http) {
    this.loginUser=function () {
        return $http.get('../login/getLoginUser.do');
    }
})
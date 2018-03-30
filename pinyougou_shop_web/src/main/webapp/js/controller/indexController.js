app.controller('indexController',function ($scope,$controller,loginService) {
    $scope.showLoginName=function () {
        loginService.loginUser().success(
            function (response) {
                $scope.loginName=response.LoginUser;
        })
    }
})
/**
Authentication data service : to get/set data from the API

**/

angular.module('liztube.dataService.authService', [
    'restangular'
]).factory('authService', function (Restangular) {
    var RestangularDefault = Restangular.withConfig(function(RestangularConfigurer) {
        RestangularConfigurer.setBaseUrl('/');
    });

    function baseAuth(){
        return Restangular.one("auth");
    }

    return {
        login: login,
        register: register,
        currentUser: currentUser,
        logout: logout,
        emailExist : emailExist,
        pseudoExist : pseudoExist
    };

    /**
    Get current connected user
    **/
    function currentUser() {
        return baseAuth().one('currentProfil').get();
    }

    function login(username, password) {
        return RestangularDefault.one('login')
        .customPOST("username="+username+"&password="+password,
            undefined,
            undefined,
            {'Content-Type': 'application/x-www-form-urlencoded'});
    }
    function register(user) {
        return baseAuth().post('signin', user);
    }
    function emailExist(email) {
        var emailObj = {
            'value':email
        };
        return baseAuth().post('email', emailObj);
    }
    function pseudoExist(pseudo) {
        var pseudoObj = {
            'value':pseudo
        };
        return baseAuth().post('pseudo', pseudoObj);
    }
    function logout(){
        return RestangularDefault.one('logout').get();
    }
});
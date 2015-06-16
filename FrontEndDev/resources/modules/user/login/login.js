/**
 * Created by Youcef on 26/02/2015.
 */
angular.module("liztube.login",[
    "liztube.dataService.authService",
    "liztube.moastr",
    "ngRoute"
]).config(function ($routeProvider){
    $routeProvider.when("/login",{
        title: "LizTube - Connexion",
        page: "Connexion",
        controller: 'loginCtrl',
        templateUrl: "login.html"
    });
})
.controller("loginCtrl", function($scope, $rootScope, $location, authService, $window, moastr, constants){

    $scope.errorLogin = '';
    $scope.submit= function() {
        $scope.$emit('loadingStatus', true);
        authService.login($scope.login, $scope.password).then(function(){
            authService.currentUser().then(function(currentUser){
                $window.user = currentUser;
                $scope.$emit('userStatus', currentUser);
                $location.path('/');
            },function(){
                moastr.error(constants.SERVER_ERROR,'left right bottom');
            });
        },function(){
            moastr.error(constants.LOGIN_FAILED, 'left right bottom');
        }).finally(function(){
            $scope.$emit('loadingStatus', false);
        });
    };
});


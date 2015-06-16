/**
 * Created by Youcef on 04/03/2015.
 */
angular.module("liztube.userStatus",[
    "liztube.dataService.authService",
    "liztube.moastr",
    "ngRoute"
]).controller("connectedCtrl", function($scope,$rootScope,$window, authService,$location,$mdSidenav,moastr, constants) {
    $scope.$on('userIsConnected', function(event, user) {
        if(_.isUndefined(user)){
            $scope.pseudo = '';
            $scope.email = '';
            $scope.userConnected = false;
        }else {
            $scope.pseudo = user.pseudo;
            $scope.email = user.email;
            $scope.userConnected = true;
        }
    });
    $scope.logOut = function(){
        authService.logout().then(function(){
            $scope.$emit('userStatus', undefined);
            $window.user.pseudo = "";
            $location.path("/");
        }, function(){
            moastr.error(constants.SERVER_ERROR,'left right bottom');
        });
    };

    $scope.close = function() {
        $mdSidenav('right').close();
    };

    $scope.checkUserConnected = function(){
        if(!$scope.pseudo){
            if($window.user.pseudo){
                $scope.pseudo = $window.user.pseudo;
                $scope.email = $window.user.email;
                $scope.userConnected = true;
            }else{
                $scope.userConnected = false;
            }
        }
    };

}).directive('isConnected', function () {
    return {
        restrict: 'E',
        controller: 'connectedCtrl',
        templateUrl: "connected.html",
        scope: {
            type: "@"
        }
    };
});
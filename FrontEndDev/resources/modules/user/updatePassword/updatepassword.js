angular.module("liztube.updatepassword",[
    "liztube.moastr",
    "liztube.dataService.userService",
    'ngMessages'
]).config(function ($routeProvider){
    $routeProvider.when("/majmotdepasse",{
        title: "LizTube - Mise à jour du mot de passe",
        page: "Mise à jour du mot de passe",
        controller: 'updatePasswordCtrl',
        templateUrl: "updatepassword.html"
    });
}).controller("updatePasswordCtrl", function($scope, $rootScope, userService, $location, moastr, constants) {

    $scope.errorUpdate = '';
    $scope.password = {
        newPassword : '',
        oldPassword : ''
    };
    $scope.verify = {
        password : ''
    };


    $scope.update = function () {
        $rootScope.$broadcast('loadingStatus', true);
        userService.updatePassword($scope.password).then(function () {
            moastr.successMin(constants.UPDATE_PASSWORD_OK, 'top right');
            $location.path('/profil');
        }, function (response) {
            if( response.data.messages[0] === "#1015"){
                moastr.error(constants.UPDATE_PASSWORD_NOK_OLD_PASSWORD, 'left right bottom');
            }else{
                moastr.error(constants.SERVER_ERROR, 'left right bottom');
           }


        }).finally(function () {
            $scope.$emit('loadingStatus', false);
        });
    };

});
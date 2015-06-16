/**
 * Created by Youcef on 26/02/2015.
 */
angular.module("liztube.register",[
    "ngRoute",
    "liztube.moastr",
    "liztube.dataService.authService",
    'ngMessages'
]).config(function ($routeProvider,$locationProvider){
    $routeProvider.when("/register",{
        title: "LizTube - Inscription",
        page: "Inscription",
        accessAnonymous : true,
        controller: 'registerCtrl',
        templateUrl: "register.html"
    });
}).controller("registerCtrl", function($scope, $rootScope, authService, $location, moastr, constants) {

        $scope.errorRegister = '';

        $scope.register = function () {
            $rootScope.$broadcast('loadingStatus', true);
            authService.register($scope.user).then(function () {
                $location.path('/login');
            }, function () {
                moastr.error(constants.SERVER_ERROR, 'left right bottom');
            }).finally(function () {
                $rootScope.$broadcast('loadingStatus', false);
            });
        };

}).directive('passwordVerify', function() {
    return {
        require: "ngModel",
        scope: {
            passwordVerify: '='
        },
        link: function(scope, element, attrs, ctrl) {
            scope.$watch(function() {
                var combined;

                if (scope.passwordVerify || ctrl.$viewValue) {
                    combined = scope.passwordVerify + '_' + ctrl.$viewValue;
                }
                return combined;
            }, function(value) {
                if (value) {
                    ctrl.$parsers.unshift(function(viewValue) {
                        var origin = scope.passwordVerify;
                        if (origin !== viewValue) {
                            ctrl.$setValidity("passwordVerify", false);
                            return undefined;
                        } else {
                            ctrl.$setValidity("passwordVerify", true);
                            return viewValue;
                        }
                    });
                }
            });
        }
    };
}).directive('emailValidation', function(authService, moastr) {
    return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            ctrl.$asyncValidators.emailValid = function(modelValue, viewValue) {
                var value = modelValue || viewValue;

                var emailExistService = function(){
                    return authService.emailExist(value).then(function(bool){
                        if (bool) {
                            ctrl.$setValidity("emailValidation", false);
                            return undefined;
                        }else{
                            ctrl.$setValidity("emailValidation", true);
                            return viewValue;
                        }
                        return true;
                    },function(){
                        moastr.error(constants.SERVER_ERROR, 'left right bottom');
                    });
                };

                return emailExistService();
            };
        }
    };
}).directive('pseudoValidation', function(authService, moastr) {
    return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            ctrl.$asyncValidators.pseudoValid = function(modelValue, viewValue) {
                var value = modelValue || viewValue;

                var pseudoVerify = function(){
                    return authService.pseudoExist(value).then(function(bool){
                        if (bool) {
                            ctrl.$setValidity("pseudoValidation", false);
                            return undefined;
                        }else{
                            ctrl.$setValidity("pseudoValidation", true);
                            return viewValue;
                        }
                        return true;
                    },function(){
                        moastr.error(constants.SERVER_ERROR, 'left right bottom');
                    });
                };

                return pseudoVerify();
            };
        }
    };
});

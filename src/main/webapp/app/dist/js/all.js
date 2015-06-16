/**
 * Created by Youcef on 28/01/2015.
 */
angular.module("liztube",[
    "liztube.constants",
    "liztube.themes",
    'liztube.utils',
    "liztube.menu",
    "liztube.home",
    "liztube.search",
    "liztube.user",
    "liztube.partial",
    "ngRoute",
    'ngMessages',
    'angularjs-gravatardirective',
    "liztube.videos.module"
]).config(["$routeProvider", "$locationProvider", "RestangularProvider", function ($routeProvider,$locationProvider,RestangularProvider){
    $locationProvider.html5Mode(true);
    RestangularProvider.setBaseUrl('api/');

    // add a response intereceptor
    RestangularProvider.addResponseInterceptor(function(data, operation, what, url, response, deferred) {
        var extractedData;
        // .. to look for getList operations
        if (operation === "getList") {
            // .. and handle the data and meta data
            extractedData = data.videos;
            extractedData.currentPage = data.currentPage;
            extractedData.videosTotalCount = data.videosTotalCount;
            extractedData.totalPage = data.totalPage;
        } else {
            extractedData = data;
        }
        return extractedData;
    });

}]).controller('mainCtrl', ["$scope", function ($scope) {
    $scope.$on('loadingStatus', function (event, bool) {
        $scope.$broadcast('loadingStatusForHeader', bool);
    });
    $scope.$on('userStatus', function(event, user) {
        $scope.$broadcast('userIsConnected', user);
    });
    $scope.$on('loadingUploadVideo', function(event, video) {
        $scope.$broadcast('loadingUploadVideoForHeader', video);
    });
    $scope.$on('addNotification', function(event, bool) {
        $scope.$broadcast('addNotificationForHeader', bool);
    });
    $scope.$on('removeNotification', function(event, bool) {
        $scope.$broadcast('removeNotificationForHeader', bool);
    });
}]).run(["$rootScope", "$window", "$location", function($rootScope,$window,$location) {
    $rootScope.$on('$routeChangeStart', function(event, current, previous) {
        if (current.$$route && current.$$route.resolve) {
            // Show a loading message until promises are not resolved
            $rootScope.isViewLoading = true;
        }
        if (current.hasOwnProperty('$$route')) {
            if ($window.user.pseudo === "" && !current.$$route.accessAnonymous){
                $location.path('/login');
            }
        }
    });
    $rootScope.$on('$routeChangeSuccess', function (event, current, previous ) {
        $rootScope.isViewLoading = false;
        if (current.hasOwnProperty('$$route')) {
            $rootScope.title = current.$$route.title;
            $rootScope.page = current.$$route.page;
        }
    });
}]);

angular.module("liztube.constants",[

]).constant('constants',{
    SERVER_ERROR : 'Une erreur inattendue est survenue. Si le problème persiste veuillez contacter l\'équipe de Liztube.',
    FILE_TYPE_ERROR : 'Veuillez sélectionner une vidéo de type "mp4"',
    FILE_SIZE_ERROR: 'La taille de la vidéo ne doit pas dépasser 500 Mo',
    NO_FILE_SELECTED: 'Aucune vidéo sélectionnée',
    FILE_SIZE_ALLOWED: 524288000,
    UPLOAD_DONE: "Téléchargement de la vidéo terminé",
    DOWNLOAD_ON_AIR_FILE_NAME: "Téléchargement de la vidéo : ",
    NO_NOTIFICATIONS_FOUND: "Vous n'avez aucune notification",
    UPDATE_PASSWORD_OK: "Votre mot de passe a bien été mis à jour",
    UPDATE_PASSWORD_NOK_OLD_PASSWORD: "Votre ancien mot de passe ne correspond pas",
    WRONG_PASSWORD: "Mot de passe incorrect",
    SUCCESS_DELETE: "Votre profil a bien été supprimé",
    UPDATE_PROFILE_OK: "Votre profil a bien été mis à jour",
    UPDATE_VIDEO_DESCRIPTION_OK: "Votre vidéo a bien été mise à jour",
    NO_VIDEOS_FOUND: "Aucune vidéos trouvées",
    VIDEO_NOT_EXISTS: "Vidéo indisponible",
    VIDEO_SHARE_URL_CLIPPED: "Le lien de la vidéo a automatiquement été copié",
    LOGIN_FAILED: "Login ou mot de passe incorrect"
});

/**
Authentication data service : to get/set data from the API

**/

angular.module('liztube.dataService.authService', [
    'restangular'
]).factory('authService', ["Restangular", function (Restangular) {
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
}]);
/**
 User data service : to get/set data from the API

 **/

angular.module('liztube.dataService.userService', [
    'restangular'
]).factory('userService', ["Restangular", function (Restangular) {
    function baseUser(){
        return Restangular.one("user");
    }

    return {
        userProfile : userProfile,
        updateProfile : updateProfile,
        emailExistUpdate : emailExistUpdate,
        updatePassword : updatePassword,
        deleteUser : deleteUser
    };

    /**
     Get current user info
     **/
    function userProfile() {
        return baseUser().get();
    }

    /**
     PUT update  user info
     **/
    function updateProfile(user) {
        return baseUser().customPUT(user);
    }

    /**
     PUT update password
     **/
    function updatePassword(passwords){
        return baseUser().one("password").patch(passwords);
    }

    /**
     * POST check if email changed and if exist
    **/
    function emailExistUpdate(email) {
        var emailObj = {
            'value':email
        };
        return baseAuth().post('email', emailObj);
    }

    /**
     * DELETE user
     */
    function deleteUser(password) {
        return baseUser().post('delete', password);
    }
}]);
/**
 User data service : to get/set data from the API

 **/

angular.module('liztube.dataService.videosService', [
    'restangular'
]).factory('videosService', ["Restangular", function (Restangular) {
    function baseVideo(){
        return Restangular.one("video");
    }

    return {
        getVideos : getVideos,
        getVideoData : getVideoData,
        updateVideoData : updateVideoData
    };

    /**
     Get videos
     **/
    function getVideos(orderBy, params) {
        return baseVideo().one("search").getList(orderBy, params);
    }

    /**
     Get video data
     **/
    function getVideoData(key){
        return baseVideo().customGET(key);
    }

    /**
     Update video data
     **/
    function updateVideoData(videoDesc){
        return baseVideo().customPUT(videoDesc);
    }
}]);
/**
 * Created by Youcef on 01/03/2015.
 */
angular.module("liztube.themes",[
    "ngMaterial"
]).config(["$mdThemingProvider", function ($mdThemingProvider){
    //Theme material design.
    $mdThemingProvider.definePalette('liztubePalette', {
        '50': 'ffebee',
        '100': 'ffcdd2',
        '200': 'ef9a9a',
        '300': 'e57373',
        '400': 'ef5350',
        '500': '333333',
        '600': 'e53935',
        '700': 'd32f2f',
        '800': 'c62828',
        '900': 'b71c1c',
        'A100': 'ff8a80',
        'A200': 'ff5252',
        'A400': 'ff1744',
        'A700': 'd50000',
        'contrastDefaultColor': 'dark',   // whether, by default, text (contrast)
        // on this palette should be dark or light
        'contrastDarkColors': ['50', '100', //hues which contrast should be 'dark' by default
            '200', '300', '400', 'A100'],
        'contrastLightColors': 'light'    // could also specify this if default was 'dark'
    });
    $mdThemingProvider.theme('default')
        .primaryPalette('liztubePalette')
        .accentPalette('blue',{
            'default': '700'
        }).warnPalette("red",{
            'default': '800'
        });
    $mdThemingProvider.theme('navbar')
        .primaryPalette('grey', {
            'default': '200'
        }).accentPalette('grey', {
            'default': '900'
        }).warnPalette("red",{
            'default': '800'
        });
    $mdThemingProvider.theme('sub-bar')
        .primaryPalette('grey', {
            'default': '600'
        }).accentPalette('blue-grey', {
            'default': '500'
        }).warnPalette("red",{
            'default': '800'
        });
    $mdThemingProvider.setDefaultTheme('default');
}]);

/**
 * Created by Youcef on 05/03/2015.
 */
angular.module("liztube.date",[

]).directive('dateToTimestamp', function() {
    return {
        require: 'ngModel',
        link: function(scope, ele, attr, ngModel) {
            // view to model
            ngModel.$parsers.push(function(value) {
                var date = Date.parse(value);
                var currentTime = new Date();
                var now = Date.parse(currentTime.getMonth() + 1 + "/" + currentTime.getDate() + "/" + currentTime.getFullYear());

                if(date >= now ){
                    ngModel.$setValidity("dateToTimestamp", false);
                }else{
                    ngModel.$setValidity("dateToTimestamp", true);
                    return Date.parse(value);
                }
            });
        }
    };
});
/**
 * Created by Youcef on 06/03/2015.
 */
angular.module('liztube.utils',[
    'liztube.date'
]);
/**
 * Created by Youcef on 28/01/2015.
 */
angular.module("liztube.home",[
'ngRoute'
]).config(["$routeProvider", function ($routeProvider){
    $routeProvider.when("/",{
        title: "LizTube - Accueil",
        page: "Accueil",
        accessAnonymous : true,
        controller: 'homeCtrl',
        templateUrl: "home.html"
    }).otherwise({
        redirectTo: '/404',
        title: "LizTube - 404",
        page: "404 : Page non trouvée",
        templateUrl:"404.html"
    });
}]).controller("homeCtrl", ["$scope", function($scope){
    $scope.pageTitle = "Suggestions Liztube";
    $scope.orderBy = "q";
    $scope.page = "1";
    $scope.pagination = "20";
    $scope.userId = "";
    $scope.q = "";
    $scope.for = "home";
}]);

/**
 * Created by Youcef on 28/01/2015.
 */
angular.module("liztube.search",[
'ngRoute'
]).config(["$routeProvider", function ($routeProvider){
    $routeProvider.when("/search/:search",{
        title: "LizTube - Recherche",
        page: "Recherche",
        accessAnonymous : true,
        controller: 'searchCtrl',
        templateUrl: "search.html"
    });
}]).controller("searchCtrl", ["$scope", "$routeParams", function($scope, $routeParams) {
    $scope.pageTitle = "Vidéos les plus récentes";
    $scope.orderBy = "mostrecent";
    $scope.page = "1";
    $scope.pagination = "20";
    $scope.userId = "";
    $scope.q = $routeParams.search;
    $scope.for = "home";
}]);
/**
 * Created by Youcef on 11/02/2015.
 */
angular.module('liztube.menu',[
    'liztube.header',
    'liztube.footer'
]);
/**
 * Created by Youcef on 26/02/2015.
 */
angular.module("liztube.user",[
    "liztube.login",
    "liztube.register",
    "liztube.profile",
    "liztube.updatepassword"
]);

angular.module("liztube.videos.module",[
    "liztube.upload.video",
    "liztube.videos.watch"
]);

angular.module("liztube.moastr",[

]).factory("moastr", ["$mdToast", function($mdToast) {
    return {
        error:error,
        success:success,
        info:info,
        errorMin:errorMin,
        successMin:successMin,
        infoMin:infoMin
    };

    function error(message, position){
        $mdToast.show({
            template: '<md-toast class="moastr full error"><span flex><i class="fa fa-exclamation-triangle"></i> '+message+'</span></md-toast>',
            hideDelay: 6000,
            position: position
        });
    }
    function success(message, position){
        $mdToast.show({
            template: '<md-toast class="moastr full success"><span flex><i class="fa fa-exclamation-triangle"></i> '+message+'</span></md-toast>',
            hideDelay: 6000,
            position: position
        });
    }
    function info(message, position){
        $mdToast.show({
            template: '<md-toast class="moastr full info"><span flex><i class="fa fa-exclamation-triangle"></i> '+message+'</span></md-toast>',
            hideDelay: 6000,
            position: position
        });
    }

    function errorMin(message, position){
        $mdToast.show({
            template: '<md-toast class="moastr min error"><span flex><i class="fa fa-exclamation-triangle"></i> '+message+'</span></md-toast>',
            hideDelay: 6000,
            position: position
        });
    }
    function successMin(message, position){
        $mdToast.show({
            template: '<md-toast class="moastr min success"><span flex><i class="fa fa-exclamation-triangle"></i> '+message+'</span></md-toast>',
            hideDelay: 6000,
            position: position
        });
    }
    function infoMin(message, position){
        $mdToast.show({
            template: '<md-toast class="moastr min info"><span flex><i class="fa fa-exclamation-triangle"></i> '+message+'</span></md-toast>',
            hideDelay: 6000,
            position: position
        });
    }
}]);

/**
 * Created by Youcef on 04/03/2015.
 */
angular.module("liztube.userStatus",[
    "liztube.dataService.authService",
    "liztube.moastr",
    "ngRoute"
]).controller("connectedCtrl", ["$scope", "$rootScope", "$window", "authService", "$location", "$mdSidenav", "moastr", "constants", function($scope,$rootScope,$window, authService,$location,$mdSidenav,moastr, constants) {
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

}]).directive('isConnected', function () {
    return {
        restrict: 'E',
        controller: 'connectedCtrl',
        templateUrl: "connected.html",
        scope: {
            type: "@"
        }
    };
});
/**
 * Created by Youcef on 11/02/2015.
 */
angular.module("liztube.footer",[

]).controller("footerCtrl", ["$scope", function($scope) {

}])
.directive('footer', function () {
    return {
        restrict: 'E',
        controller: 'footerCtrl',
        templateUrl: "footer.html"
    };
});

/**
 * Created by Youcef on 11/02/2015.
 */
angular.module("liztube.header",[
    "liztube.userStatus"
]).controller("headerCtrl", ["$scope", "$mdSidenav", "constants", "$location", function($scope, $mdSidenav, constants, $location) {

    $scope.notification = 0;
    $scope.showNotification = false;
    $scope.isLoading = false;
    $scope.noNotification = constants.NO_NOTIFICATIONS_FOUND;

    /**
     * Add a notification
     */
    $scope.$on('addNotificationForHeader', function(event, bool) {
        $scope.showNotification = bool;
        $scope.notification = $scope.notification + 1;
        $scope.noNotification = "";
    });

    /**
     * Remove a notification
     */
    $scope.$on('removeNotificationForHeader', function(event, bool) {
        if($scope.notification > 0){
            $scope.notification = $scope.notification - 1;
            if($scope.notification === 0){
                $scope.showNotification = false;
                $scope.noNotification = constants.NO_NOTIFICATIONS_FOUND;
            }else{
                $scope.noNotification = "";
            }
        }
    });

    /**
     * Set loading status
     */
    $scope.$on('loadingStatusForHeader', function(event, bool) {
        $scope.isLoading= bool;
    });

    /**
     * Open or close (toggle) side nav part
     */
    $scope.toggleRight = function() {
        $mdSidenav('right').toggle();
    };

    $scope.closeRightBar = function() {
        $mdSidenav('right').close();
    };

    $scope.escapeChar = function (str){
        var char = {
            "&": "",
            "<": "",
            ">": "",
            '"': "",
            "'": "",
            "/": ""
        };
        return String(str).replace(/[&<>"'\/]/g, function (s) {
            return char[s];
        });
    };

    $scope.search = function(){
        if($scope.query === "" || _.isUndefined($scope.query)){
            $location.path('/');
        }else{
            $location.path('/search/'+ $scope.escapeChar($scope.query));
        }
    };

}]).directive('header', function () {
    return {
        restrict: 'E',
        controller: 'headerCtrl',
        templateUrl: "header.html"
    };
});

/**
 * Created by Youcef on 26/02/2015.
 */
angular.module("liztube.login",[
    "liztube.dataService.authService",
    "liztube.moastr",
    "ngRoute"
]).config(["$routeProvider", function ($routeProvider){
    $routeProvider.when("/login",{
        title: "LizTube - Connexion",
        page: "Connexion",
        controller: 'loginCtrl',
        templateUrl: "login.html"
    });
}])
.controller("loginCtrl", ["$scope", "$rootScope", "$location", "authService", "$window", "moastr", "constants", function($scope, $rootScope, $location, authService, $window, moastr, constants){

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
}]);


/**
 * Created by maxime on 08/04/2015.
 */
angular.module("liztube.profile",[
    "liztube.moastr",
    "liztube.dataService.userService",
    "liztube.videos",
    'ngMessages'
]).config(["$routeProvider", function ($routeProvider){
    $routeProvider.when("/profil",{
        title: "LizTube - Profil",
        page: "Profil",
        controller: 'profileCtrl',
        templateUrl: "profile.html"
    });
}]).controller("profileCtrl", ["$scope", "$rootScope", "userService", "$location", "moastr", "constants", "$window", "$mdDialog", function($scope, $rootScope, userService, $location, moastr, constants, $window, $mdDialog) {
    $scope.pageTitle = "Mes vidéos";
    $scope.orderBy = "mostrecent";
    $scope.page = "1";
    $scope.pagination = "20";
    $scope.userId = $window.user.id;
    $scope.q = "";
    $scope.for = "user";

    $scope.errorUpdate = '';
    $scope.isDisable = true;
    $scope.profilTitle = "Profil";

    $scope.enableUpdateProfil = function(){
        $scope.profilTitle = "Mise à jour du profil";
        $scope.isDisable = false;
    };

    var disableUpdateProfil= function(){
        $scope.profilTitle = "Profil";
        $scope.isDisable = true;
    };

    /**
     * Get user profile
     */
    $scope.getUserProfile = function(){
        userService.userProfile().then(function(user){
            $scope.user = user;
            var date = new Date($scope.user.birthdate);
            $scope.user.birthdate = new Date(date.getFullYear(), date.getMonth(), date.getDate());
        },function(){
            moastr.error(constants.SERVER_ERROR, 'left right bottom');
        });
    };


    /**
     * Update user profile
     */
        $scope.update = function () {
            $rootScope.$broadcast('loadingStatus', true);

            userService.updateProfile($scope.user).then(function () {
                moastr.successMin(constants.UPDATE_PROFILE_OK, 'top right');
            }, function () {
                moastr.error(constants.SERVER_ERROR, 'left right bottom');
            }).finally(function () {
                $rootScope.$broadcast('loadingStatus', false);
                disableUpdateProfil();
            });
    };


    /**
     * Alertview for delete user
     */
    $scope.showAdvanced = function(ev) {
        $mdDialog.show({
            controller: DialogController,
            templateUrl: 'dialog.delete.user.html',
            parent: angular.element(document.body),
            targetEvent: ev
        });
    };

    function DialogController($scope, $mdDialog) {
        $scope.hide = function() {
            $mdDialog.hide();
        };
        $scope.cancel = function() {
            $mdDialog.cancel();
        };
        $scope.answer = function(answer) {
            $mdDialog.hide(answer);
            console.log($scope.password);

            $rootScope.$broadcast('loadingStatus', true);


            userService.deleteUser($scope.password).then(function () {
                moastr.successMin(constants.SUCCESS_DELETE, 'top right');
                $scope.$emit('userStatus', undefined);
                $window.user.pseudo = "";
                $location.path("/");
            }, function () {
                moastr.error(constants.WRONG_PASSWORD, 'left right bottom');

            }).finally(function () {
                $rootScope.$broadcast('loadingStatus', false);
            });
        };
    }

}]).directive('emailValidation', ["userService", "moastr", function(userService, moastr) {
    return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            ctrl.$asyncValidators.emailValid = function(modelValue, viewValue) {
                var value = modelValue || viewValue;

                return userService.emailExistUpdate(value).then(function(bool){
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
        }
    };
}]);

/**
 * Created by Youcef on 26/02/2015.
 */
angular.module("liztube.register",[
    "ngRoute",
    "liztube.moastr",
    "liztube.dataService.authService",
    'ngMessages'
]).config(["$routeProvider", "$locationProvider", function ($routeProvider,$locationProvider){
    $routeProvider.when("/register",{
        title: "LizTube - Inscription",
        page: "Inscription",
        accessAnonymous : true,
        controller: 'registerCtrl',
        templateUrl: "register.html"
    });
}]).controller("registerCtrl", ["$scope", "$rootScope", "authService", "$location", "moastr", "constants", function($scope, $rootScope, authService, $location, moastr, constants) {

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

}]).directive('passwordVerify', function() {
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
}).directive('emailValidation', ["authService", "moastr", function(authService, moastr) {
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
}]).directive('pseudoValidation', ["authService", "moastr", function(authService, moastr) {
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
}]);

angular.module("liztube.updatepassword",[
    "liztube.moastr",
    "liztube.dataService.userService",
    'ngMessages'
]).config(["$routeProvider", function ($routeProvider){
    $routeProvider.when("/majmotdepasse",{
        title: "LizTube - Mise à jour du mot de passe",
        page: "Mise à jour du mot de passe",
        controller: 'updatePasswordCtrl',
        templateUrl: "updatepassword.html"
    });
}]).controller("updatePasswordCtrl", ["$scope", "$rootScope", "userService", "$location", "moastr", "constants", function($scope, $rootScope, userService, $location, moastr, constants) {

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

}]);
angular.module("liztube.videos",[
    "liztube.dataService.videosService",
    "liztube.moastr"
]).controller("videosCtrl", ["$scope", "constants", "videosService", "moastr", "$window", function($scope, constants, videosService, moastr, $window) {
    $scope.parameters = {};
    $scope.flexSize = 20;
    $scope.noVideoFound = "";
    $scope.videos = [];
    $scope.videosLoading = false;

    /**
     * Get flex size for display videos according to the screen size send as parameter
     * @param size : screen size
     */
    $scope.getFlexSize = function (size) {
        if(size <= 500){
            $scope.flexSize = 100;
        }else if(size >= 500 && size <= 800){
            $scope.flexSize = 50;
        }else if(size >= 800 && size <= 1300){
            $scope.flexSize = 33;
        }else{
            $scope.flexSize = 20;
        }
    };

    $scope.getFlexSize($window.innerWidth);

    /**
     * Get params form directive to set all params, to use it in getVideos service
     * @param getParams : get params
     */
    $scope.getParams = function(params) {
        $scope.videosLoading = true;
        if (!_.isUndefined(params.for) && params.for === "user") {
            $scope.showConfidentiality = true;
        } else {
            $scope.showConfidentiality = false;
        }

        if (!_.isUndefined(params.for) && params.for === "home") {
            $scope.showSelectVideos = true;
        } else {
            $scope.showSelectVideos = false;
        }

        if (!_.isUndefined(params.pageTitle) && params.pageTitle !== "") {
            $scope.pageTitle = params.pageTitle;
        } else {
            $scope.pageTitle = "Liztube vidéos";
        }

        if (!_.isUndefined(params.orderBy) && params.orderBy !== "") {
            $scope.orderBy = params.orderBy;
        } else {
            $scope.orderBy = "q";
        }
        if (!_.isUndefined(params.page) && params.page !== "") {
            $scope.parameters.page = params.page;
        }
        if (!_.isUndefined(params.pagination) && params.pagination !== "") {
            $scope.parameters.pagination = params.pagination;
        }
        if (!_.isUndefined(params.user) && params.user !== "") {
            $scope.parameters.user = params.user;
        }
        if (!_.isUndefined(params.q) && params.q !== "") {
            $scope.parameters.q = $window.encodeURIComponent(params.q);
        }
        $scope.getVideos($scope.parameters);
    };

    /**
     * Get videos form getVideos services to display videos in home page and myVideos
     * @param getParams : get videos
     */
    $scope.getVideos = function(params) {
        videosService.getVideos($scope.orderBy, params).then(function(data){
            if(data.length === 0 && (_.isUndefined(params.q) || params.q === "")){
                $scope.noVideoFound = constants.NO_VIDEOS_FOUND;
            }else if(data.length === 0 && (!_.isUndefined(params.q) || params.q !== "")) {
                $scope.noVideoFound = constants.NO_VIDEOS_FOUND + " pour la recherche '" + $window.decodeURIComponent(params.q) + "'";
            }else{
                $scope.videos = $scope.videos.concat(data);
                $scope.loadPage = data.currentPage+ 1;
                $scope.totalPage = data.totalPage;
            }
        },function(){
            moastr.error(constants.SERVER_ERROR,'left right bottom');
        }).finally(function(){
            $scope.videosLoading = false;
        });
    };

    /**
     * Get videos form getVideos and filter them by mostrecent or mostviewed or mostshared
     * @param getParams : filter videos
     */
    $scope.filter = function(orderBy){
        if(orderBy === "1"){
            $scope.orderBy = "q";
            $scope.pageTitle = "Suggestions Liztube";
        }else if(orderBy === "2"){
            $scope.orderBy = "mostrecent";
            $scope.pageTitle = "Vidéos les plus récentes";
        }else if(orderBy === "3"){
            $scope.orderBy = "mostviewed";
            $scope.pageTitle = "Vidéos les plus vues";
        }else if(orderBy === "4"){
            $scope.orderBy = "mostshared";
            $scope.pageTitle = "Vidéos les plus partagées";
        }
        $scope.videos = [];
        $scope.getParams({
            pageTitle: $scope.pageTitle,
            orderBy: $scope.orderBy,
            page: 1,
            pagination: $scope.pagination,
            user: $scope.user,
            q: $scope.q,
            for: $scope.for
        });
    };

}]).directive('liztubeVideos', ["$window", function ($window) {
    return {
        restrict: 'E',
        controller: 'videosCtrl',
        templateUrl: "videosDirective.html",
        replace: true,
        scope: {
            pageTitle:"@",
            orderBy: "@",
            page: "@",
            pagination: "@",
            user: "@",
            q: "@",
            for: "@"
        },
        link: function(scope, element, attrs) {
            //send all parms getted from directive to getParams method in controller
            scope.getParams({
                pageTitle: scope.pageTitle,
                orderBy: scope.orderBy,
                page: scope.page,
                pagination: scope.pagination,
                user: scope.user,
                q: scope.q,
                for: scope.for
            });

            //When window risized call getFlexSize method
            $window.addEventListener('resize', function(){
                scope.getFlexSize(element[0].offsetWidth);
                scope.$apply();
            });
        }
    };
}]).filter('formatTime', function() {
    //Convert milliseconds to time (hours:minutes:seconds)
    return function(milliseconds) {
        var seconds = parseInt((milliseconds/1000)%60);
        var minutes = parseInt((milliseconds/(1000*60))%60);
        var hours = parseInt((milliseconds/(1000*60*60))%24);
        var out = "";

        hours = (hours < 10 && hours > 0) ? "0" + hours : hours;
        minutes = (minutes < 10) ? "0" + minutes : minutes;
        seconds = (seconds < 10) ? "0" + seconds : seconds;

        if(hours === 0){
            out = minutes + ":" + seconds;
        }else{
            out = hours + ":" + minutes + ":" + seconds;
        }

        return out;
    };
}).directive("infiniteScroll", ["$window", function ($window) {

    //Call getVides method when scoll down is in bottom of page to create infinite scroll
    return function (scope, element, attrs) {
        angular.element($window).bind("scroll", function () {
            if ($window.document.body.scrollHeight === ($window.document.body.offsetHeight + $window.document.body.scrollTop)) {
                if (scope.loadPage <= scope.totalPage){
                    scope.getParams({
                        pageTitle: scope.pageTitle,
                        orderBy: scope.orderBy,
                        page: scope.loadPage,
                        pagination: scope.pagination,
                        user: scope.user,
                        q: scope.q,
                        for: scope.for
                    });
                    console.log("scope.loadPage " + scope.loadPage);
                    console.log("scope.totalPage " + scope.totalPage);
                }
            }
        });
    };
}]);

/**
 * Created by laurent on 16/04/15.
 */
angular.module('liztube.videos.watch',
    [
        "ngRoute",
        "liztube.moastr",
        "liztube.dataService.videosService",
        "ngSanitize",
        "com.2fdevs.videogular",//Video reader
        "com.2fdevs.videogular.plugins.controls",//Controls
        "com.2fdevs.videogular.plugins.overlayplay",//Big play button
        "com.2fdevs.videogular.plugins.poster",//Thumbnail poster
        "ngClipboard"
    ]
).config(["$routeProvider", function ($routeProvider){
        $routeProvider.when("/watch/:videoKey",{
            title: "LizTube - Vidéo",
            page: "vidéo",
            controller: 'watchCtrl',
            templateUrl: "watch.html",
            accessAnonymous : true
        });

}]).config(['ngClipProvider', function(ngClipProvider) {
        ngClipProvider.setPath("app/dist/libs/zeroclipboard/dist/ZeroClipboard.swf");
}]).controller('watchCtrl', ["$sce", "$rootScope", "$scope", "$routeParams", "$route", "moastr", "videosService", "constants", "$location", "$mdDialog", function ($sce,$rootScope, $scope, $routeParams, $route, moastr, videosService, constants,$location, $mdDialog) {
    $scope.errorUpdate = '';
    $scope.isEnableEditingVideo = false;

    $scope.showLink = function(ev) {
        $mdDialog.show({
            controller: DialogController,
            templateUrl: 'dialogs.html',
            parent: angular.element(document.body),
            targetEvent: ev
        }).then(function(answer) {}, function() {});
    };

    function DialogController($scope, $mdDialog) {
        $scope.urlForSharing = " " + $location.absUrl()+ " ";
        $scope.hide = function() {
            $mdDialog.hide();
        };
        $scope.cancel = function() {
            $mdDialog.cancel();
        };
        $scope.answer = function(answer) {
            $mdDialog.hide(answer);
            moastr.successMin(constants.VIDEO_SHARE_URL_CLIPPED, 'top right');
        };
    }

    $scope.enableEditVideo = function(){
        $scope.isEnableEditingVideo = true;
        if($scope.videoDesc.public){
            $scope.videoDesc.confidentiality = 1;
        }else if(!$scope.videoDesc.public && $scope.videoDesc.publicLink){
            $scope.videoDesc.confidentiality = 2;
        }else{
            $scope.videoDesc.confidentiality = 0;
        }
    };

    var videoOwnerTest= function (userName) {
        if(window.user !== null){
            if(userName == window.user.pseudo )
                return true;
            else
                return false;
        }else{
            return false;
        }
    };

    var videoKey = $routeParams.videoKey;
    /**
     * Get video data
     */
    $scope.getVideoDesc = function(){
        videosService.getVideoData(videoKey).then(function(video){

            $scope.videoDesc = video;
            $scope.editVideo = videoOwnerTest(video.ownerPseudo);
            $scope.config = {
                sources: [
                    {src: $sce.trustAsResourceUrl("/api/video/watch/"+videoKey), type: "video/mp4"}
                ],
                theme: "/app/dist/libs/videogular-themes-default/videogular.css",
                plugins: {
                    poster: "/api/video/thumbnail/"+videoKey+"?width=1280&height=720",
                    controls: {
                        autoHide: true,
                        autoHideTime: 5000
                    }
                }
            };

        },function(responses){
            if(responses.data.messages[0] === "#1101"){
                moastr.error(constants.VIDEO_NOT_EXISTS, 'left right bottom');
                $location.path('/');
            }else if(responses.data.messages[0] === "#1100"){
                moastr.error(constants.VIDEO_NOT_EXISTS, 'left right bottom');
                $location.path('/');
            }else{
                moastr.error(constants.SERVER_ERROR, 'left right bottom');
            }
        });
    };


    /**
     * Update videoDesc
     */
    $scope.updateVideoDesc= function (){

        $rootScope.$broadcast('loadingStatus', true);

        //Intepret confidentiality
        if($scope.videoDesc.confidentiality == 1){//Public
            $scope.videoDesc.public = true;
            $scope.videoDesc.publicLink = true;
        }else if($scope.videoDesc.confidentiality == 2){//PublicLink
            $scope.videoDesc.public = false;
            $scope.videoDesc.publicLink = true;
        }else{//Private
            $scope.videoDesc.public = false;
            $scope.videoDesc.publicLink = false;
        }
        delete $scope.videoDesc.confidentiality;

        videosService.updateVideoData($scope.videoDesc).then(function () {
            moastr.successMin(constants.UPDATE_VIDEO_DESCRIPTION_OK, 'top right');
            $scope.isEnableEditingVideo = false;
        }, function () {
            moastr.error(constants.SERVER_ERROR, 'left right bottom');
        }).finally(function () {
            $rootScope.$broadcast('loadingStatus', false);
        });


    };

}]);
angular.module("liztube.upload.video",[
    "liztube.moastr",
    "ngRoute",
    "liztube.upload.video.page",
    'angularFileUpload' //https://github.com/danialfarid/angular-file-upload
]).controller("uploadVideoCtrl", ["$scope", "$http", "$upload", "constants", "moastr", "$mdSidenav", "$location", function($scope, $http, $upload, constants, moastr, $mdSidenav, $location) {
    $scope.uploadRate = 0;
    $scope.id = 0;
    $scope.notifications = {
        "infos": []
    };
    /**
     * Catch upload video event to upload a video
     */

    $scope.$on('loadingUploadVideoForHeader', function(event, video) {
        $mdSidenav('right').toggle();
        $scope.id = $scope.id+1;
        $scope.$emit('addNotification', true);
        $scope.notifications.infos.push({
            id: $scope.id,
            fileName : constants.DOWNLOAD_ON_AIR_FILE_NAME + video.title,
            uploadRate : 0,
            percent : "0%",
            videoKey: ""
        });

        $upload.upload({
            url: '/api/video/upload',
            fields: {
                title: video.title,
                description: video.description,
                isPublic: video.isPublic,
                isPublicLink: video.isPublicLink
            },
            file: video.file
        }).progress(function (evt) {
            $scope.addVideoAsNotifications({
                id: $scope.id,
                fileName : constants.DOWNLOAD_ON_AIR_FILE_NAME + video.title,
                uploadRate : parseInt(99.0 * evt.loaded / evt.total),
                percent : parseInt(99.0 * evt.loaded / evt.total) + "%",
                videoKey: ""
            });
        }).success(function (data, status, headers, config) {
            $scope.addVideoAsNotifications({
                id: $scope.id,
                fileName : constants.UPLOAD_DONE + video.title,
                uploadRate : 100,
                percent : "100%",
                videoKey: data
            });
            moastr.successMin(constants.UPLOAD_DONE, 'top right');
            $location.path("/profil");
        }).error(function (data, status, headers, config){
            moastr.error(constants.SERVER_ERROR, 'left right bottom');
        });
    });

    $scope.addVideoAsNotifications = function(video){
        for (var j = 0; j < $scope.notifications.infos.length; j++) {
            if (angular.equals($scope.notifications.infos[j].id,video.id)) {
                $scope.notifications.infos[j].fileName = video.fileName;
                $scope.notifications.infos[j].uploadRate = video.uploadRate;
                $scope.notifications.infos[j].percent = video.percent;
                $scope.notifications.infos[j].videoKey = video.videoKey;
            }
        }
    };

    /**
     * Close upload progress bar
     */
    $scope.hideProgressBar = function(index){
        $scope.notifications.infos.splice(index, 1);
        $scope.$emit('removeNotification', true);
    };

}]).directive('uploadVideo', function () {
    return {
        restrict: 'E',
        controller: 'uploadVideoCtrl',
        templateUrl: "uploadVideo.html",
        scope: {
            type: "@"
        }
    };
});
angular.module('liztube.upload.video.page', [
    "liztube.moastr",
    'ngRoute'
]).config(["$routeProvider", "$locationProvider", function ($routeProvider,$locationProvider){
    $routeProvider.when("/upload",{
        title: "LizTube - Upload",
        page: "Upload",
        controller: 'FileUploadController',
        templateUrl: "upload.html"
    });
}]).controller('FileUploadController', ["$rootScope", "$scope", "moastr", "constants", "$location", function($rootScope, $scope, moastr, constants, $location) {

    $scope.isPublic = false;
    $scope.isPublicLink = false;
    $scope.fileName = "";

    $scope.video = {
        confidentiality: '1'
    };
    /**
     * Upload a video
     */
    $scope.submit= function() {
        if ($scope.video.files && $scope.video.files.length) {
            if($scope.video.confidentiality == '0'){
                $scope.isPublic = false;
                $scope.isPublicLink = false;
            }else if($scope.video.confidentiality == '1'){
                $scope.isPublic = true;
                $scope.isPublicLink = true;
            }else{
                $scope.isPublic = false;
                $scope.isPublicLink = true;
            }
            var video = {
                file: $scope.video.files[0],
                title: $scope.video.title,
                description: $scope.video.description,
                isPublic: $scope.isPublic,
                isPublicLink: $scope.isPublicLink
            };
            $scope.$emit('loadingUploadVideo', video);
            $location.path("/");

        }else{
            moastr.error(constants.NO_FILE_SELECTED, 'left right bottom');
        }
    };

    /**
     * Watch to valid video file
     */
    $scope.$watch('video.files', function() {
        if($scope.video){
            $scope.isValidFile($scope.video);
        }
    });

    /**
     * Method to analyse and define if video is valid
     * @param video
     */
    $scope.isValidFile = function(video){
        if(video.files && video.files.length){
            if(video.files[0].type != "video/mp4") {
                moastr.error(constants.FILE_TYPE_ERROR, 'left right bottom');
            }else{
                if (video.files[0].size > constants.FILE_SIZE_ALLOWED) {
                    moastr.error(constants.FILE_SIZE_ERROR, 'left right bottom');
                }else{
                    $scope.fileName = video.files[0].name;
                }
            }
        }
    };
}]);
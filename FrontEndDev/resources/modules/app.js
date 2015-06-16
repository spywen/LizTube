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
]).config(function ($routeProvider,$locationProvider,RestangularProvider){
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

}).controller('mainCtrl', function ($scope) {
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
}).run(function($rootScope,$window,$location) {
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
});

/**
 * Created by Youcef on 28/01/2015.
 */
angular.module("liztube.search",[
'ngRoute'
]).config(function ($routeProvider){
    $routeProvider.when("/search/:search",{
        title: "LizTube - Recherche",
        page: "Recherche",
        accessAnonymous : true,
        controller: 'searchCtrl',
        templateUrl: "search.html"
    });
}).controller("searchCtrl", function($scope, $routeParams) {
    $scope.pageTitle = "Vidéos les plus récentes";
    $scope.orderBy = "mostrecent";
    $scope.page = "1";
    $scope.pagination = "20";
    $scope.userId = "";
    $scope.q = $routeParams.search;
    $scope.for = "home";
});
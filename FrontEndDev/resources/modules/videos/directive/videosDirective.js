angular.module("liztube.videos",[
    "liztube.dataService.videosService",
    "liztube.moastr"
]).controller("videosCtrl", function($scope, constants, videosService, moastr, $window) {
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

}).directive('liztubeVideos', function ($window) {
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
}).filter('formatTime', function() {
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
}).directive("infiniteScroll", function ($window) {

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
});

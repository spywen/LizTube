describe('liztube.videos', function(){

    var $scope, $rootScope, createController, videosService, constants, $window, $q, $filter;

    var mockConstants = {
        SERVER_ERROR : 'Une erreur inattendue est survenue. Si le problème persiste veuillez contacter l\'équipe de Liztube.',
        NO_VIDEOS_FOUND : 'Aucune vidéos trouvées'
    };
    beforeEach(module('liztube.videos'));
    beforeEach(module('liztube.dataService.videosService'));
    beforeEach(module('liztube.moastr'));

    var changePromiseResult = function (promise, status, value) {
        if (status === 'resolve')
            promise.resolve(value);
        else
            promise.reject(value);
        $rootScope.$digest();
    };

    beforeEach(function() {
        module(function($provide) {
            $provide.constant('constants', mockConstants);
        });
    });

    var moastr = {
        error: function(message){
            return message;
        },
        successMin: function(message){
            return message;
        }
    };

    beforeEach(inject(function (_$rootScope_, _videosService_,_$q_, _$filter_, _$window_) {
        $rootScope =_$rootScope_;
        videosService = _videosService_;
        $q = _$q_;
        $filter = _$filter_;
        $window = _$window_;
    }));

    beforeEach(inject(function ($controller) {
        $scope = $rootScope.$new();
        createController = function () {
            return $controller('videosCtrl', {
                '$scope': $scope,
                'constants': mockConstants,
                'moastr': moastr
            });
        };
    }));

    beforeEach(function(){
        createController();
    });

    describe('getParams method', function() {
        var videoServicePromise;
        beforeEach(function(){
            $scope.parameters = {};
            $scope.noVideoFound = "";
            $scope.videos = [];
            $scope.loadPage = 0;
            $scope.totalPage = 0;
            $scope.videosLoading = false;
            videoServicePromise = $q.defer();
            spyOn(moastr, 'error').and.callThrough();
            spyOn($scope,'getParams').and.callThrough();
            spyOn(videosService, 'getVideos').and.returnValue(videoServicePromise.promise);
            $scope.params = {
                pageTitle: "Vidéos les plus récentes",
                orderBy: "mostrecent",
                page: "1",
                pagination: "10",
                user: "test",
                q: "query",
                for: "home"
            };
        });

        it('Should params are setted and $scope for params created', function () {
            $scope.getParams($scope.params);
            expect($scope.videosLoading).toBe(true);
            expect($scope.pageTitle).toEqual("Vidéos les plus récentes");
            expect($scope.orderBy).toEqual("mostrecent");
            expect($scope.parameters.page).toEqual("1");
            expect($scope.parameters.pagination).toEqual("10");
            expect($scope.parameters.user).toEqual("test");
            expect($scope.parameters.q).toEqual("query");
        });

        it('Should be a successful getVideos and $scope.params.q is undefind and data.length = 0', function () {
            $scope.params.q= "";
            $scope.getParams($scope.params);
            changePromiseResult(videoServicePromise, "resolve", []);
            expect($scope.noVideoFound).toEqual(mockConstants.NO_VIDEOS_FOUND);
        })

        it('Should be a successful getVideos and $scope.params.q is defined and data.length = 0', function () {
            $scope.params.q= "query";
            $scope.getParams($scope.params);
            changePromiseResult(videoServicePromise, "resolve", []);
            expect($scope.noVideoFound).toEqual(mockConstants.NO_VIDEOS_FOUND + " pour la recherche '" + $window.decodeURIComponent($scope.parameters.q) + "'");
        });

        it('Should be a successful getVideos and data.length > 0', function () {
            $scope.params.q= "query";
            $scope.getParams($scope.params);
            changePromiseResult(videoServicePromise, "resolve", [{id:1}, {id:2}]);
            expect($scope.noVideoFound).toEqual("");
            expect($scope.videos.length).toEqual(2);
        });

        it('should return an error message if api call failed', function(){
            $scope.getParams($scope.params);
            changePromiseResult(videoServicePromise, "failed");
            expect(moastr.error).toHaveBeenCalledWith(mockConstants.SERVER_ERROR,'left right bottom');
        });

        it('Should set $scope for params as default if $scope.params is undefind', function () {
            $scope.params.pageTitle= "";
            $scope.params.orderBy= "";
            $scope.getParams($scope.params);
            expect($scope.pageTitle).toEqual("Liztube vidéos");
            expect($scope.orderBy).toEqual("q");
        });

        it('Should showConfidentiality is false if $scope.params.for is not user', function () {
            $scope.getParams($scope.params);
            expect($scope.showConfidentiality).toEqual(false);
        });

        it('Should showConfidentiality is true if $scope.params.for is user', function () {
            $scope.params.for= "user";
            $scope.getParams($scope.params);
            expect($scope.showConfidentiality).toEqual(true);
        });

        it('Should showConfidentiality is false if $scope.params.for is not home', function () {
            $scope.params.for= "user";
            $scope.getParams($scope.params);
            expect($scope.showSelectVideos).toEqual(false);
        });

        it('Should showConfidentiality is true if $scope.params.for is  home', function () {
            $scope.params.for= "home";
            $scope.getParams($scope.params);
            expect($scope.showSelectVideos).toEqual(true);
        });
    });

    describe('filter function', function() {
        var filterPromise;
        beforeEach(function(){
            spyOn(moastr, 'error').and.callThrough();
            spyOn($scope,'filter').and.callThrough();
            $scope.videos = {};
            filterPromise = $q.defer();
            spyOn(videosService, 'getVideos').and.returnValue(filterPromise.promise);
        });

        it('Should $scope.orderBy equal q if orderBy equal 1', function () {
            $scope.filter("1");
            expect($scope.orderBy).toEqual("q");
            expect($scope.pageTitle).toEqual("Suggestions Liztube");
        });

        it('Should $scope.orderBy equal mostrecent if orderBy equal 2', function () {
            $scope.filter("2");
            expect($scope.orderBy).toEqual("mostrecent");
            expect($scope.pageTitle).toEqual("Vidéos les plus récentes");
        });

        it('Should $scope.orderBy equal mostviewed if orderBy equal 3', function () {
            $scope.filter("3");
            expect($scope.orderBy).toEqual("mostviewed");
            expect($scope.pageTitle).toEqual("Vidéos les plus vues");
        });

        it('Should $scope.orderBy equal mostshared if orderBy equal 4', function () {
            $scope.filter("4");
            expect($scope.orderBy).toEqual("mostshared");
            expect($scope.pageTitle).toEqual("Vidéos les plus partagées");
        });

        it('should be a successful filter and data.length > 0', function() {
            $scope.filter("1");
            changePromiseResult(filterPromise, "resolve", [{id:1}, {id:2}]);
            expect($scope.noVideoFound).toEqual("");
            expect($scope.videos.length).toEqual(2);
        });

        it('should be a successful filter and data.length = 0', function() {
            $scope.filter("1");
            changePromiseResult(filterPromise, "resolve", []);
            expect($scope.noVideoFound).toEqual(mockConstants.NO_VIDEOS_FOUND);
        });

        it('should return an error message', function(){
            $scope.filter("1");
            changePromiseResult(filterPromise, "failed");
            expect(moastr.error).toHaveBeenCalledWith(mockConstants.SERVER_ERROR,'left right bottom');
        });

    });

    describe('getFlexSize function', function() {
        beforeEach(function(){
            spyOn($scope,'getFlexSize').and.callThrough();
            $scope.flexSize = 20;
        });

        it('Should $scope.flexSize equal 100 if getFlexSize less than or equal 500', function () {
            $scope.getFlexSize(400);
            expect($scope.flexSize).toEqual(100);
        });

        it('Should $scope.flexSize equal 50 if getFlexSize greater than or equal 500 and less than or equal 800', function () {
            $scope.getFlexSize(650);
            expect($scope.flexSize).toEqual(50);
        });

        it('Should $scope.flexSize equal 33 if getFlexSize greater than or equal 800 and less than or equal 1300', function () {
            $scope.getFlexSize(950);
            expect($scope.flexSize).toEqual(33);
        });

        it('Should $scope.flexSize equal 20 if getFlexSize greater than 1300', function () {
            $scope.getFlexSize(1350);
            expect($scope.flexSize).toEqual(20);
        });
    });

    describe('filter formatTime', function() {
        it('should convert milliseconds to minutes and seconds', function () {
            expect($filter('formatTime')(1965605)).toEqual('32:45');
        });
        it('should convert milliseconds to minutes and seconds and hours', function () {
            expect($filter('formatTime')(1965604568)).toEqual('18:00:04');
        });
    });
});
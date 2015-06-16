describe('liztube.videos.watch', function() {
    var changePromiseResult = function (promise, status, value) {
        if (status === 'resolve')
            promise.resolve(value);
        else
            promise.reject(value);
        $rootScope.$digest();
    };

    beforeEach(module('liztube.videos.watch'));
    beforeEach(module('liztube.dataService.videosService'));
    beforeEach(module('ngRoute'));
    beforeEach(module('ngSanitize'));
    beforeEach(module('com.2fdevs.videogular'));
    beforeEach(module('com.2fdevs.videogular.plugins.controls'));
    beforeEach(module('com.2fdevs.videogular.plugins.overlayplay'));
    beforeEach(module('com.2fdevs.videogular.plugins.poster'));
    beforeEach(module('ngClipboard'));

    var $scope, $rootScope, $location, videosService, $q, constants, $window, createController, keyVideo, $sce, $mdDialog;

    var mockConstants = {
        SERVER_ERROR : 'Une erreur inattendue est survenue. Si le problème persiste veuillez contacter l\'équipe de Liztube.',
        UPDATE_VIDEO_DESCRIPTION_OK: "Votre vidéo a bien été mise à jour"
    };

    beforeEach(function() {
        module(function($provide) {
            $provide.constant('constants', mockConstants);
        });
        keyVideo = "e477e67e-1288-4445-bf60-df52a1cc88d2";
    });

    beforeEach(inject(function (_$rootScope_, _$location_, _$route_, _videosService_, _$window_, _$q_, _$sce_) {
        $rootScope =_$rootScope_;
        $location = _$location_;
        route = _$route_;
        videosService = _videosService_;
        $window= _$window_;
        $q = _$q_;
        $sce = _$sce_;
    }));

    var moastr = {
        error: function(message){
            return message;
        },
        successMin: function(message){
            return message;
        }
    };

    var $mdDialog = function(){
        return true;
    };


    beforeEach(inject(function ($controller) {
        $scope = $rootScope.$new();
        createController = function () {
            return $controller('watchCtrl', {
                '$scope': $scope,
                'moastr' : moastr,
                '$mdDialog': $mdDialog
            });
        };
    }));

    beforeEach(function(){
        createController();
        $scope.isEnableEditingVideo = false;
    });


    describe('Watch route', function() {
        beforeEach(inject(
            function($httpBackend) {
                $httpBackend.expectGET('watch.html')
                    .respond(200);
            }));

        /*it('should load the video watch page on successful load of /watch=videoKey', function() {
            $location.path('/watch/'+keyVideo);
            $rootScope.$digest();
            expect(route.current.controller).toBe('watchCtrl');
            expect(route.current.title).toBe('LizTube - ');
            expect(route.current.page).toBe('Watch');
            expect(route.current.params.videoKey).toBe(keyVideo);
        });*/
    });



    describe('Get watch Info', function() {
        var videoDescPromise, videoDesc, videoConfig;

        beforeEach(function(){
            spyOn(videosService, 'getVideoData').and.returnValue(videoDescPromise.promise);
            spyOn($location,'path').and.callThrough();
            spyOn(moastr, 'error').and.callThrough();
            $scope.getVideoDesc();
        });

        beforeEach(function(){
            videoDesc={
                title: "Titre",
                description: "blabla",
                views: 100,
                key: keyVideo,
                ownerPseudo: "pseudo",
                ownerEmail: "pseudo@liztube.fr",
                ownerId: 1,
                isPublic: true,
                isPublicLink: false,
                creationDate: 1429800731,
                duration: ""
            };
            videoConfig = {
                sources: [
                    {src: $sce.trustAsResourceUrl("/api/video/watch/"+keyVideo), type: "video/mp4"}
                ],
                theme: "/app/dist/libs/videogular-themes-default/videogular.css",
                plugins: {
                    poster: "/api/video/thumbnail/"+keyVideo+"?width=1280&height=720"
                }
            };
            $scope.videoDesc=null;
            videoDescPromise = $q.defer();
        });




       /* it('should return an error message', function(){
            changePromiseResult(videoDescPromise, "failed");
            expect(moastr.error).toHaveBeenCalledWith(mockConstants.SERVER_ERROR, 'left right bottom');
        });

        it('should be a successful get user info', function() {
            changePromiseResult(videoDescPromise, "resolve", videoDesc);
            expect($scope.videoDesc).toEqual(videoDesc);
            expect($scope.videoDesc.creationDate).toEqual(new Date(2015, 3, 23));
            expect($scope.config).toEqual(videoConfig);
        });*/

    });

    describe('Edit video load data', function(){
        beforeEach(function(){
            $scope.isEnableEditingVideo = false;
            $scope.videoDesc = {
                public:false,
                publicLink:false
            }
        });

        it('should pass in edit mode', function(){
            expect($scope.isEnableEditingVideo).toBeFalsy();
            $scope.enableEditVideo();
            expect($scope.isEnableEditingVideo).toBeTruthy();
        });

        it('confidentiality should be private (0)', function(){
            $scope.videoDesc = {
                public:false,
                publicLink:false
            }
            $scope.enableEditVideo();
            expect($scope.videoDesc.confidentiality).toEqual(0);
        });

        it('confidentiality should be public link (2)', function(){
            $scope.videoDesc = {
                public:false,
                publicLink:true
            }
            $scope.enableEditVideo();
            expect($scope.videoDesc.confidentiality).toEqual(2);
        });

    });

    describe('Update video', function(){
        var updateVideoPromise;

        beforeEach(function(){
            $scope.isEnableEditingVideo = true;
            $scope.videoDesc = {
                public:false,
                publicLink:false,
                title:'title',
                description:'desc',
                confidentiality:0
            };
            spyOn($rootScope, '$broadcast').and.callThrough();
            spyOn(moastr, 'successMin').and.callThrough();
            spyOn(moastr, 'error').and.callThrough();

            updateVideoPromise = $q.defer();
            spyOn(videosService, 'updateVideoData').and.returnValue(updateVideoPromise.promise);
        });

        it('should start loading', function(){
            $scope.updateVideoDesc();
            expect($rootScope.$broadcast).toHaveBeenCalledWith('loadingStatus', true);
        });

        it('should set video as public', function(){
            $scope.videoDesc = {
                public:false,
                publicLink:false,
                title:'title',
                description:'desc',
                confidentiality:1
            };
            $scope.updateVideoDesc();
            expect($scope.videoDesc.public).toBeTruthy();
            expect($scope.videoDesc.publicLink).toBeTruthy();
        });

        it('should set video as private', function(){
            $scope.videoDesc = {
                public:true,
                publicLink:true,
                title:'title',
                description:'desc',
                confidentiality:0
            };
            $scope.updateVideoDesc();
            expect($scope.videoDesc.public).toBeFalsy();
            expect($scope.videoDesc.publicLink).toBeFalsy();
        });

        it('should set video as public link', function(){
            $scope.videoDesc = {
                public:false,
                publicLink:false,
                title:'title',
                description:'desc',
                confidentiality:2
            };
            $scope.updateVideoDesc();
            expect($scope.videoDesc.public).toBeFalsy();
            expect($scope.videoDesc.publicLink).toBeTruthy();
        });

        it('should remove confidentiality attribute from object to send', function(){
            $scope.updateVideoDesc();
            expect($scope.videoDesc.confidentiality).toEqual(undefined);
        });

        it('should display successful notification in case of success and pass back to the view mode and stop loading', function(){
            $scope.updateVideoDesc();
            changePromiseResult(updateVideoPromise,'resolve');
            expect($scope.isEnableEditingVideo).toBeFalsy();
            expect(moastr.successMin).toHaveBeenCalledWith(mockConstants.UPDATE_VIDEO_DESCRIPTION_OK, 'top right');
            expect($rootScope.$broadcast).toHaveBeenCalledWith('loadingStatus', false);
        });

        it('should display error notification in case of error and stop loading', function(){
            $scope.updateVideoDesc();
            changePromiseResult(updateVideoPromise,'error');
            expect($scope.isEnableEditingVideo).toBeTruthy();
            expect(moastr.error).toHaveBeenCalledWith(mockConstants.SERVER_ERROR, 'left right bottom');
            expect($rootScope.$broadcast).toHaveBeenCalledWith('loadingStatus', false);
        });

    });

});
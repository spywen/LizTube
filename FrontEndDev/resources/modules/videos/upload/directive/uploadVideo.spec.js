describe('liztube.upload.video', function(){

    beforeEach(module('liztube.moastr'));
    beforeEach(module('ngRoute'));
    beforeEach(module('liztube.upload.video.page'));
    beforeEach(module('liztube.upload.video'));
    beforeEach(module('angularFileUpload'));
    var createController, $scope, $rootScope, $http, $upload, constants, moastr, $q, $compile;

    var mockConstants = {
        SERVER_ERROR : 'Une erreur inattendue est survenue. Si le problème persiste veuillez contacter l\'équipe de Liztube.',
        UPLOAD_DONE: "Téléchargement de la vidéo terminer",
        DOWNLOAD_ON_AIR_FILE_NAME: "Téléchargement de la vidéo : "
    };

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

    beforeEach(inject(function (_$rootScope_,_$http_, _$upload_, _$q_, _$compile_) {
        $rootScope =_$rootScope_;
        $http = _$http_;
        $upload = _$upload_;
        $q = _$q_;
        $compile = _$compile_;
    }));

    var $mdSidenav = function(test){
        return {
            toggle: function(){
                return true;
            }
        };
    };

    var moastr = {
        error: function(message){
            return message;
        },
        successMin: function(message){
            return message;
        }
    };

    beforeEach(inject(function ($controller) {
        $scope = $rootScope.$new();
        createController = function () {
            return $controller('uploadVideoCtrl', {
                '$scope': $scope,
                '$http': $http,
                '$upload': $upload,
                'constants': mockConstants,
                'moastr': moastr,
                '$mdSidenav': $mdSidenav
            });
        };
    }));

    describe('uploadVideoCtrl', function(){

        beforeEach(function(){
            createController();
        });

        describe('scope Init', function(){

            it('scope variables initialized', function(){
                expect($scope.uploadRate).toEqual(0);
                expect($scope.id).toEqual(0);
            });

        });

        describe('on loadingUploadVideoForHeader', function() {

            beforeEach(function(){
                spyOn(moastr, 'successMin').and.callThrough();
                spyOn(moastr, 'error').and.callThrough();
                spyOn($upload, 'upload').and.callThrough();
                spyOn($scope, '$emit').and.callThrough();
                var video = {
                    title: "test",
                    description: "test",
                    isPublic: false,
                    isPublicLink : false,
                    file: "test"
                };
                $scope.notifications = {
                    "infos": []
                };
                $scope.$broadcast('loadingUploadVideoForHeader', video);
                $scope.notifications.infos.push({
                    id: $scope.id,
                    fileName : mockConstants.DOWNLOAD_ON_AIR_FILE_NAME + video.title,
                    uploadRate : 0,
                    percent : "0%"
                });
            });

            it('should emit addNotification', function () {
                expect($scope.$emit).toHaveBeenCalledWith('addNotification', true);
                expect($scope.id).toEqual(1);
            });

            it('$upload.upload should have been called', function () {
                expect($upload.upload).toHaveBeenCalled();
            });

        });

        describe('addVideoAsNotifications', function(){
            beforeEach(function(){
                $scope.notifications = {
                    "infos": [
                        {
                            id: 1,
                            fileName : mockConstants.DOWNLOAD_ON_AIR_FILE_NAME + "file 1",
                            uploadRate : 0,
                            percent : "0%"
                        }
                    ]
                };
                var infos = {
                    id: 2,
                    fileName : mockConstants.DOWNLOAD_ON_AIR_FILE_NAME + 'file 2',
                    uploadRate : 10,
                    percent : "10%"
                };
                $scope.addVideoAsNotifications(infos);
            });

            it('Should update object', function(){
                infos = {
                    id: 1,
                    fileName : mockConstants.DOWNLOAD_ON_AIR_FILE_NAME + 'file 1',
                    uploadRate : 50,
                    percent : "10%"
                };
                $scope.addVideoAsNotifications(infos);
                expect($scope.notifications.infos[0].uploadRate).toEqual(50);
            });
        });


        describe('hideProgressBar', function(){
            beforeEach(function(){
                spyOn($scope, '$emit').and.callThrough();
                $scope.notifications = {
                    "infos": [
                        {
                            id: $scope.id,
                            fileName : mockConstants.DOWNLOAD_ON_AIR_FILE_NAME + "test",
                            uploadRate : 0,
                            percent : "0%"
                        }
                    ]
                };
                $scope.hideProgressBar(0);
            });

            it('Should emit for removeNotification', function(){
                expect($scope.notifications.infos).toEqual([]);
                expect($scope.$emit).toHaveBeenCalledWith('removeNotification', true);
            });
        });

        describe('directive uploadVideo', function() {
            it('Should directives uploadVideo has text', function() {
                var directive = $compile('<upload-video></upload-video>')($scope);
                expect(directive.html()).toEqual("");
            });
        });
    });
});


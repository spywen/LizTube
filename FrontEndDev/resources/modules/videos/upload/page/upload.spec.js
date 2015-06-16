describe('liztube.upload.video', function(){

    beforeEach(module('liztube.moastr'));
    beforeEach(module('ngRoute'));
    beforeEach(module('liztube.upload.video.page'));
    var createController, $scope, $rootScope, constants, moastr, route, location;

    var mockConstants = {
        SERVER_ERROR : 'Une erreur inattendue est survenue. Si le problème persiste veuillez contacter l\'équipe de Liztube.',
        FILE_TYPE_ERROR : 'Veuillez sélectionnez une vidéo de type "mp4"',
        FILE_SIZE_ERROR: 'La taille de la vidéo ne dois pas dépasser 500 MO',
        NO_FILE_SELECTED: 'Aucune vidéo sélectionnée',
        FILE_SIZE_ALLOWED: 524288000,
        UPLOAD_DONE: "Téléchargement de la vidéo terminer"
    };

    beforeEach(function() {
        module(function($provide) {
            $provide.constant('constants', mockConstants);
        });
    });

    beforeEach(inject(function (_$rootScope_, _$route_, _$location_) {
        $rootScope =_$rootScope_;
        route = _$route_;
        location = _$location_;
    }));

    var moastr = {
        error: function(message){
            return message;
        }
    };

    beforeEach(inject(function ($controller) {
        $scope = $rootScope.$new();
        createController = function () {
            return $controller('FileUploadController', {
                '$scope': $scope,
                'constants': mockConstants,
                'moastr': moastr
            });
        };
    }));

    describe('Upload route', function() {
        beforeEach(inject(
            function($httpBackend) {
                $httpBackend.expectGET('upload.html')
                    .respond(200);
            }));

        it('should load the upload page on successful load of /upload', function() {
            location.path('/upload');
            $rootScope.$digest();
            expect(route.current.controller).toBe('FileUploadController');
            expect(route.current.title).toBe('LizTube - Upload');
            expect(route.current.page).toBe('Upload');
        });
    });

    describe('FileUploadController', function(){

        beforeEach(function(){
            createController();
        });

        describe('scope Init', function(){

            it('scope variables initialized', function(){
                expect($scope.isPublic).toEqual(false);
                expect($scope.isPublicLink).toEqual(false);
                expect($scope.fileName).toEqual("");
            });

        });

        describe('submit', function(){
            beforeEach(function(){
                $scope.video = {
                    files: null,
                    title: "test",
                    description: "test",
                    confidentiality: '0'
                };
                spyOn($scope, '$emit').and.callThrough();
                spyOn(moastr, 'error').and.callThrough();

            });

            it('if $scope.video.files is empty then moastr should be raised NO_FILE_SELECTED', function(){
                $scope.submit();
                expect(moastr.error).toHaveBeenCalledWith(mockConstants.NO_FILE_SELECTED, 'left right bottom');
                expect($scope.$emit).not.toHaveBeenCalled();
            });

            it('if $scope.video.files.length is empty then moastr should be raised NO_FILE_SELECTED', function(){
                $scope.video.files = {length: null}
                $scope.submit();
                expect(moastr.error).toHaveBeenCalledWith(mockConstants.NO_FILE_SELECTED, 'left right bottom');
                expect($scope.$emit).not.toHaveBeenCalled();
            });

            it('if video.confidentiality equal 0 should set isPublic & isPublicLink to false', function(){
                $scope.video.files = {length: '5'}
                $scope.submit();
                expect($scope.isPublic).toEqual(false);
                expect($scope.isPublicLink).toEqual(false);
            });

            it('if video.confidentiality equal 1 should set isPublic & isPublicLink to true', function(){
                $scope.video.files = {length: '5'}
                $scope.video.confidentiality = '1';
                $scope.submit();
                expect($scope.isPublic).toEqual(true);
                expect($scope.isPublicLink).toEqual(true);
            });

            it('if video.confidentiality equal 2 should set isPublic to false & isPublicLink to true', function(){
                $scope.video.files = {length: '5'}
                $scope.video.confidentiality = '2';
                $scope.submit();
                expect($scope.isPublic).toEqual(false);
                expect($scope.isPublicLink).toEqual(true);
            });

            it('should add a notification by emit and emit a loadingUploadVideo event', function(){
                $scope.video.files = {length: '5'}
                $scope.video.confidentiality = '2';
                var video = {
                    file: $scope.video.files[0],
                    title: $scope.video.title,
                    description: $scope.video.description,
                    isPublic: false,
                    isPublicLink: true
                };
                $scope.submit();
                expect($scope.$emit).toHaveBeenCalledWith('loadingUploadVideo', video);
            });
        });

        describe('watch video.files', function(){

            beforeEach(function(){
                spyOn($scope, 'isValidFile').and.callThrough();
            });

            it('Should $scope.video is not empty then call isValidFile, while watch', function(){
                $scope.video = {
                    files: "dd",
                    title: "test",
                    description: "test",
                    confidentiality: '0'
                };
                $scope.$apply();
                expect($scope.isValidFile).toHaveBeenCalled();
            });

            it('Should $scope.video is empty then not call isValidFile, while watch', function(){
                $scope.video = null;
                $scope.$apply();
                expect($scope.isValidFile).not.toHaveBeenCalled();
            });
        });

        describe('isValidFile', function(){
            beforeEach(function(){
                spyOn($scope, '$emit').and.callThrough();
                spyOn(moastr, 'error').and.callThrough();
            });

            it('if video.files is empty then check other test', function(){
                var video = {};
                $scope.isValidFile(video);
                expect(moastr.error).not.toHaveBeenCalled();
            });

            it('if video.files.length is empty then check other test', function(){
                var video = {length: null};
                $scope.isValidFile(video);
                expect(moastr.error).not.toHaveBeenCalled();
            });

            it('should raise an error if file size not allowed', function(){
                var video = {
                    length: '5',
                    files: [{
                        type: 'video/mp4',
                        size: 524288333444
                    }]
                };
                $scope.isValidFile(video);
                expect(moastr.error).toHaveBeenCalledWith(mockConstants.FILE_SIZE_ERROR, 'left right bottom');
            });

            it('should not raise an error if file size is allowed', function(){
                var video = {
                    length: '5',
                    files: [{
                        type: 'video/mp4',
                        size: 5242883,
                        name: "test"
                    }]
                };
                $scope.isValidFile(video);
                expect($scope.fileName).toEqual(video.files[0].name);
            });

            it('should raise an error if file type is not allowed', function(){
                var video = {
                    length: '5',
                    files: [{
                        type: 'video/avi',
                        size: 5242883
                    }]
                };
                $scope.isValidFile(video);
                expect(moastr.error).toHaveBeenCalledWith(mockConstants.FILE_TYPE_ERROR, 'left right bottom');
            });
        });
    });
});


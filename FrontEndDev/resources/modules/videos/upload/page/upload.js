angular.module('liztube.upload.video.page', [
    "liztube.moastr",
    'ngRoute'
]).config(function ($routeProvider,$locationProvider){
    $routeProvider.when("/upload",{
        title: "LizTube - Upload",
        page: "Upload",
        controller: 'FileUploadController',
        templateUrl: "upload.html"
    });
}).controller('FileUploadController', function($rootScope, $scope, moastr, constants, $location) {

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
});
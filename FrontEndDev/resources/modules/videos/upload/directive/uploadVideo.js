angular.module("liztube.upload.video",[
    "liztube.moastr",
    "ngRoute",
    "liztube.upload.video.page",
    'angularFileUpload' //https://github.com/danialfarid/angular-file-upload
]).controller("uploadVideoCtrl", function($scope, $http, $upload, constants, moastr, $mdSidenav, $location) {
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

}).directive('uploadVideo', function () {
    return {
        restrict: 'E',
        controller: 'uploadVideoCtrl',
        templateUrl: "uploadVideo.html",
        scope: {
            type: "@"
        }
    };
});
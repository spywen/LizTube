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
).config(function ($routeProvider){
        $routeProvider.when("/watch/:videoKey",{
            title: "LizTube - Vidéo",
            page: "vidéo",
            controller: 'watchCtrl',
            templateUrl: "watch.html",
            accessAnonymous : true
        });

}).config(['ngClipProvider', function(ngClipProvider) {
        ngClipProvider.setPath("app/dist/libs/zeroclipboard/dist/ZeroClipboard.swf");
}]).controller('watchCtrl', function ($sce,$rootScope, $scope, $routeParams, $route, moastr, videosService, constants,$location, $mdDialog) {
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

});
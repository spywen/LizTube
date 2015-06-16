/**
 User data service : to get/set data from the API

 **/

angular.module('liztube.dataService.videosService', [
    'restangular'
]).factory('videosService', function (Restangular) {
    function baseVideo(){
        return Restangular.one("video");
    }

    return {
        getVideos : getVideos,
        getVideoData : getVideoData,
        updateVideoData : updateVideoData
    };

    /**
     Get videos
     **/
    function getVideos(orderBy, params) {
        return baseVideo().one("search").getList(orderBy, params);
    }

    /**
     Get video data
     **/
    function getVideoData(key){
        return baseVideo().customGET(key);
    }

    /**
     Update video data
     **/
    function updateVideoData(videoDesc){
        return baseVideo().customPUT(videoDesc);
    }
});
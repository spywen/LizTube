angular.module("liztube.moastr",[

]).factory("moastr", function($mdToast) {
    return {
        error:error,
        success:success,
        info:info,
        errorMin:errorMin,
        successMin:successMin,
        infoMin:infoMin
    };

    function error(message, position){
        $mdToast.show({
            template: '<md-toast class="moastr full error"><span flex><i class="fa fa-exclamation-triangle"></i> '+message+'</span></md-toast>',
            hideDelay: 6000,
            position: position
        });
    }
    function success(message, position){
        $mdToast.show({
            template: '<md-toast class="moastr full success"><span flex><i class="fa fa-exclamation-triangle"></i> '+message+'</span></md-toast>',
            hideDelay: 6000,
            position: position
        });
    }
    function info(message, position){
        $mdToast.show({
            template: '<md-toast class="moastr full info"><span flex><i class="fa fa-exclamation-triangle"></i> '+message+'</span></md-toast>',
            hideDelay: 6000,
            position: position
        });
    }

    function errorMin(message, position){
        $mdToast.show({
            template: '<md-toast class="moastr min error"><span flex><i class="fa fa-exclamation-triangle"></i> '+message+'</span></md-toast>',
            hideDelay: 6000,
            position: position
        });
    }
    function successMin(message, position){
        $mdToast.show({
            template: '<md-toast class="moastr min success"><span flex><i class="fa fa-exclamation-triangle"></i> '+message+'</span></md-toast>',
            hideDelay: 6000,
            position: position
        });
    }
    function infoMin(message, position){
        $mdToast.show({
            template: '<md-toast class="moastr min info"><span flex><i class="fa fa-exclamation-triangle"></i> '+message+'</span></md-toast>',
            hideDelay: 6000,
            position: position
        });
    }
});

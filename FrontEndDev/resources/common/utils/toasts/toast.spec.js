describe('liztube.moastr', function(){

    var moast, $mdToast;

    beforeEach(module('liztube.moastr'));

    beforeEach(module(function($provide) {
        $mdToast= {
            show: function () {
                return true;
            }
        };
        $provide.constant('$mdToast', $mdToast);
    }));

    beforeEach(inject(function (_moastr_) {
        moast = _moastr_;
        spyOn($mdToast, 'show').and.callThrough();
    }));

    it('should toast an error', function(){
        moast.error("test", 'left right bottom');
        expect($mdToast.show).toHaveBeenCalledWith({
            template: '<md-toast class="moastr full error"><span flex><i class="fa fa-exclamation-triangle"></i> test</span></md-toast>',
            hideDelay: 6000,
            position: 'left right bottom'
        });
    });

    it('should toast a success', function(){
        moast.success("test", 'left right bottom');
        expect($mdToast.show).toHaveBeenCalledWith({
            template: '<md-toast class="moastr full success"><span flex><i class="fa fa-exclamation-triangle"></i> test</span></md-toast>',
            hideDelay: 6000,
            position: 'left right bottom'
        });
    });

    it('should toast an info', function(){
        moast.info("test", 'left right bottom');
        expect($mdToast.show).toHaveBeenCalledWith({
            template: '<md-toast class="moastr full info"><span flex><i class="fa fa-exclamation-triangle"></i> test</span></md-toast>',
            hideDelay: 6000,
            position: 'left right bottom'
        });
    });

    it('should toast an error', function(){
        moast.errorMin("test", 'left right bottom');
        expect($mdToast.show).toHaveBeenCalledWith({
            template: '<md-toast class="moastr min error"><span flex><i class="fa fa-exclamation-triangle"></i> test</span></md-toast>',
            hideDelay: 6000,
            position: 'left right bottom'
        });
    });

    it('should toast a success', function(){
        moast.successMin("test", 'left right bottom');
        expect($mdToast.show).toHaveBeenCalledWith({
            template: '<md-toast class="moastr min success"><span flex><i class="fa fa-exclamation-triangle"></i> test</span></md-toast>',
            hideDelay: 6000,
            position: 'left right bottom'
        });
    });

    it('should toast an info', function(){
        moast.infoMin("test", 'left right bottom');
        expect($mdToast.show).toHaveBeenCalledWith({
            template: '<md-toast class="moastr min info"><span flex><i class="fa fa-exclamation-triangle"></i> test</span></md-toast>',
            hideDelay: 6000,
            position: 'left right bottom'
        });
    });



});
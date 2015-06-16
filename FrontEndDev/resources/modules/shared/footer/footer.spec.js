describe('liztube.footer', function(){

    var $scope, $rootScope, createController, $compile;

    beforeEach(module('liztube.footer'));

    beforeEach(inject(function (_$rootScope_,_$compile_) {
        $rootScope =_$rootScope_;
        $compile = _$compile_;
    }));

    beforeEach(inject(function ($controller) {
        $scope = $rootScope.$new();
        createController = function () {
            return $controller('footerCtrl', {
                '$scope': $scope
            });
        };
    }));

    beforeEach(function(){
        createController();
    });

    describe('directive footer', function() {
        it('Should directives footer has empty text', function() {
            var directive = $compile('<footer></footer>')($scope);
            //$scope.$digest();
            expect(directive.html()).toEqual("");
        });
    });
});
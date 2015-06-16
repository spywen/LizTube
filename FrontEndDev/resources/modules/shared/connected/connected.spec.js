describe('liztube.userStatus',function(){

    var changePromiseResult = function (promise, status, value) {
        if (status === 'resolve')
            promise.resolve(value);
        else
            promise.reject(value);
        $rootScope.$digest();
    };

    beforeEach(module('liztube.userStatus'));
    beforeEach(module('liztube.dataService.authService'));

    var createController, $scope, $rootScope, $location, authService, $window, $q, $mdSidenav, $templateCache, constants, $compile;

    var mockConstants = {
        SERVER_ERROR : 'Une erreur inattendue est survenue. Si le problème persiste veuillez contacter l\'équipe de Liztube.'
    };

    beforeEach(function() {
        module(function($provide) {
            $provide.constant('constants', mockConstants);
        });
    });


    beforeEach(inject(function (_$rootScope_, _$location_, _authService_, _$window_, _$q_, _$templateCache_, _$compile_) {
        $rootScope =_$rootScope_;
        $location = _$location_;
        authService = _authService_;
        $window= _$window_;
        $q = _$q_;
        $templateCache = _$templateCache_;
        $compile = _$compile_;
    }));

    var moastr = {
        error: function(message){
            return message;
        }
    };

    var $mdSidenav = function(){
        return {
            close: function(){
                return true;
            }
        };
    };

    beforeEach(inject(function ($controller) {
        $scope = $rootScope.$new();
        createController = function () {
            return $controller('connectedCtrl', {
                '$scope': $scope,
                'moastr' : moastr,
                '$mdSidenav': $mdSidenav
            });
        };
    }));


    beforeEach(function(){
        createController();
    });

    describe('on userIsConnected', function(){

        beforeEach(function(){
        });

        it('should not connect user if $brodcast is undefined', function(){
            $scope.$broadcast('userIsConnected', undefined);
            expect($scope.userConnected).toEqual(false);
            expect($scope.email).toEqual("");
            expect($scope.pseudo).toEqual("");
        });

        it('should connect user if $brodcast is defined', function(){
            var user = {
                pseudo : "test",
                email: "test"
            }
            $scope.$broadcast('userIsConnected', user);
            expect($scope.userConnected).toEqual(true);
            expect($scope.email).toEqual("test");
            expect($scope.pseudo).toEqual("test");
        });

    });

    describe('logOut', function(){
        var logoutPromise;
        beforeEach(function(){
            logoutPromise = $q.defer();
            spyOn(authService, 'logout').and.returnValue(logoutPromise.promise);
        });

        beforeEach(function(){

            $window.user = {
                "pseudo":"test",
                "roles":["test"]
            };
            $scope.checkUserConnected();

            spyOn($scope, '$emit').and.callThrough();
            spyOn($location, 'path').and.callThrough();
            spyOn(moastr, 'error').and.callThrough();
            $scope.logOut();
        });

        it('Should call the logout method', function(){
            expect(authService.logout).toHaveBeenCalled();
        });

        it('Should broadcast and redirect logout message if success', function(){
            changePromiseResult(logoutPromise, "resolve");
            expect($scope.$emit).toHaveBeenCalledWith('userStatus', undefined);
            expect($location.path).toHaveBeenCalledWith('/');
        });

        it('Should not broadcast logout message if error', function(){
            changePromiseResult(logoutPromise, "failed");
            expect($scope.$emit).not.toHaveBeenCalledWith('userStatus', undefined);
            expect(moastr.error).toHaveBeenCalledWith(mockConstants.SERVER_ERROR,'left right bottom');
        });

    });

    describe('checkUserConnected ', function(){
        beforeEach(function(){
            spyOn($scope, 'checkUserConnected').and.callThrough();
            $window.user = {
                "pseudo":""
            };
        });

        it('Should set userConnected to true', function(){
            $window.user = {
                "pseudo":"test"
            };
            $scope.checkUserConnected();
            expect($scope.userConnected).toEqual(true);
        });

        it('Should set userConnected to false', function(){
            $scope.checkUserConnected();
            expect($scope.userConnected).toEqual(false);
        });
    });

    describe('directive connected', function() {
        it('Should directives connected has text', function() {
            var directive = $compile('<is-connected type="forHeaderBar"></is-connected>')($scope);
            expect(directive.html()).toEqual("");
        });
    });

    describe('close method', function(){
        beforeEach(function(){
            spyOn($mdSidenav('right'), 'close').and.callThrough();
        });

        it('Should close bar close', function(){
            $scope.close();
            //expect($mdSidenav('right').close).toHaveBeenCalled();
        });
    });
});

/*
describe('liztube.userStatus directive', function(){
    var type, element, $scope;

    beforeEach(module('connectedCtrl', function($controllerProvider) {
        $controllerProvider.register('connectedCtrl', function($scope) {
            // Controller Mock
        });
    }));

    beforeEach(module('liztube.userStatus'));

    beforeEach(inject(function ($compile) {
        $templateCache.put('connected.html', "");

        type = 'forSideBar';
        element = angular.element('<is-connected type="'+type+'"></is-connected>');

        $compile(element)($rootScope.$new());
        $rootScope.$digest();
        element.controller();

        $scope = element.isolateScope() || element.scope();
    }));

    it('should set the type scope value', function(){
        expect($scope.type).toEqual(type);
    });
});
*/

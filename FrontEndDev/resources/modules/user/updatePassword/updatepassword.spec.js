describe('liztube.updatepassword', function() {

    var changePromiseResult = function (promise, status, value) {
        if (status === 'resolve')
            promise.resolve(value);
        else
            promise.reject(value);
        $rootScope.$digest();
    };

    beforeEach(module('liztube.moastr'));
    beforeEach(module('ngRoute'));
    beforeEach(module('liztube.updatepassword'));
    beforeEach(module('liztube.dataService.userService'));

    var $scope, $rootScope, $location, userService, $q, constants, $window, createController;

    var mockConstants = {
        SERVER_ERROR : 'Une erreur inattendue est survenue. Si le problème persiste veuillez contacter l\'équipe de Liztube.',
        UPDATE_PASSWORD_OK: "Votre mot de passe a bien était mis à jour",
        UPDATE_PASSWORD_NOK_OLD_PASSWORD: "Votre ancien mot de passe ne correspond pas"
    };

    beforeEach(function() {
        module(function($provide) {
            $provide.constant('constants', mockConstants);
        });
    });

    beforeEach(inject(function (_$rootScope_, _$location_, _$route_, _userService_, _$window_, _$q_) {
        $rootScope =_$rootScope_;
        $location = _$location_;
        route = _$route_;
        userService = _userService_;
        $window= _$window_;
        $q = _$q_;
    }));

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
            return $controller('updatePasswordCtrl', {
                '$scope': $scope,
                'constants': mockConstants,
                'moastr' : moastr
            });
        };
    }));

    beforeEach(function(){
        createController();
    });



    describe('UpdatePassword route', function() {
        beforeEach(inject(
            function($httpBackend) {
                $httpBackend.expectGET('updatepassword.html')
                    .respond(200);
            }));

        it('should load the updatepassord page on successful load of /majmotdepasse', function() {
            $location.path('/majmotdepasse');
            $rootScope.$digest();
            expect(route.current.controller).toBe('updatePasswordCtrl');
            expect(route.current.title).toBe('LizTube - Mise à jour du mot de passe');
            expect(route.current.page).toBe('Mise à jour du mot de passe');
        });
    });

    describe('UpdatePasswordCtrl', function() {


        describe('scope Init', function(){

            it('scope variables initialized', function(){
                expect($scope.password.oldPassword).toEqual("");
                expect($scope.password.newPassword).toEqual("");
                expect($scope.verify.password).toEqual("");
                expect($scope.errorUpdate).toEqual("");
            });

        });

        describe('update password', function() {
            var submitPromise, responseBody, response;

            beforeEach(function(){
                submitPromise = $q.defer();
                spyOn(userService, 'updatePassword').and.returnValue(submitPromise.promise);
                $scope.update();
            });

            beforeEach(function(){
               $scope.password={
                   oldPassword: "oldPassword",
                   newPassword: "newPassword"
               };
                responseBody = {
                    messages:{}
                };

                response= {
                    data: {
                        messages:''
                    }
                }
            });

            beforeEach(function(){
                spyOn($location,'path').and.callThrough();
                spyOn(moastr, 'error').and.callThrough();
                spyOn(moastr, 'successMin').and.callThrough();
                userService.updatePassword($scope.password);
            });

            it('should return an server error message', function(){
                changePromiseResult(submitPromise, "failed", response);
                expect(moastr.error).toHaveBeenCalledWith(mockConstants.SERVER_ERROR,'left right bottom');
            });

            it('should return message #1015 error message', function () {
                response.data ={
                    messages : "#1015"
                };
                changePromiseResult(submitPromise, "failed",response);
                //expect(moastr.error).toHaveBeenCalledWith(mockConstants.UPDATE_PASSWORD_NOK_OLD_PASSWORD, 'left right bottom');
            });

            it('should be a successful passowrd update', function() {
                changePromiseResult(submitPromise, "resolve");
                expect(moastr.successMin).toHaveBeenCalledWith(mockConstants.UPDATE_PASSWORD_OK, 'top right');
                expect($location.path).toHaveBeenCalledWith('/profil');
            });

        });
    });

});
/*describe('liztube.profile', function() {
    var changePromiseResult = function (promise, status, value) {
        if (status === 'resolve')
            promise.resolve(value);
        else
            promise.reject(value);
        $rootScope.$digest();
    };

	beforeEach(module('liztube.moastr'));
    beforeEach(module('ngRoute'));
    beforeEach(module('liztube.profile'));
    beforeEach(module("liztube.videos"));
    beforeEach(module('liztube.dataService.userService'));

    var $scope, $rootScope, $location, userService, $q, constants, $window, createController, route, $mdDialog;

    var mockConstants = {
        SERVER_ERROR : 'Une erreur inattendue est survenue. Si le problème persiste veuillez contacter l\'équipe de Liztube.',
        UPDATE_PROFILE_OK: "Votre profil à bien était mis à jour",
        WRONG_PASSWORD: "Mauvais password",
        SUCCESS_DELETE: "Votre profil à bien était supprimer"
    };

    beforeEach(function() {
        module(function($provide) {
            $provide.constant('constants', mockConstants);
        });
    });

    beforeEach(inject(function (_$rootScope_, _$location_, _$route_, _userService_, _$window_, _$q_, _$mdDialog_) {
        $rootScope =_$rootScope_;
        $location = _$location_;
        route = _$route_;
        userService = _userService_;
        $window= _$window_;
        $q = _$q_;
        $mdDialog = _$mdDialog_;
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
            return $controller('profileCtrl', {
                '$scope': $scope,
                'constants': mockConstants,
                'moastr' : moastr,
                '$mdDialog' : $mdDialog
            });
        };
    }));

    beforeEach(function(){
        createController();
        $scope.isDisable = true;
        $scope.profilTitle = "Profil";
    });

    describe('Profile route', function() {
        beforeEach(inject(
            function($httpBackend) {
                $httpBackend.expectGET('profile.html')
                    .respond(200);
            }));

        it('should load the profile page on successful load of /profil', function() {
            $location.path('/profil');
            $rootScope.$digest();
            expect(route.current.controller).toBe('profileCtrl');
            expect(route.current.title).toBe('LizTube - Profil');
            expect(route.current.page).toBe('Profil');
        });
    });

    describe("Click for update profile", function() {
        beforeEach(function(){
            $scope.enableUpdateProfil();
        });

        it("should be click button for update profil", function(){
            expect($scope.isDisable).toEqual(false);
            expect($scope.profilTitle).toEqual("Mise à jour du profil");
        });
    });

    describe('ProfileCtrl', function() {

    	describe('Get user Info', function() {
    		var userInfoPromise, user;

			beforeEach(function(){
                user={
                    firstname: 'firstname',
                    lastname: 'lastname',
                    email: 'email@test.fr',
                    birthdate: 703429875000,
                    isfemale: false
                };
                $scope.user=null;
                userInfoPromise = $q.defer();
	            spyOn(userService, 'userProfile').and.returnValue(userInfoPromise.promise);
                $scope.getUserProfile();
	    	});

	    	beforeEach(function(){
	            spyOn($location,'path').and.callThrough();
	            spyOn(moastr, 'error').and.callThrough();
	        });

	    	it('should return an error message', function(){
	            changePromiseResult(userInfoPromise, "failed");
	            expect(moastr.error).toHaveBeenCalledWith(mockConstants.SERVER_ERROR, 'left right bottom');
	        });

	        it('should be a successful get user info', function() {
				changePromiseResult(userInfoPromise, "resolve", user);
                expect($scope.user).toEqual(user);
                expect($scope.user.birthdate).toEqual(new Date(1992, 3, 16));
			});

    	});

        describe('Update user info', function() {
            var submitPromise, user;

            beforeEach(function(){
                user={
                    firstname: 'firstname',
                    lastname: 'lastname',
                    email: 'email@test.fr',
                    birthdate: 703429875000,
                    isfemale: false
                };

                $scope.isDisable = false;
                $scope.profilTitle = "Mise à jour du profil";
            });

            beforeEach(function(){
                submitPromise = $q.defer();
                spyOn(userService, 'updateProfile').and.returnValue(submitPromise.promise);
                $scope.update();
            });

            beforeEach(function(){
                spyOn($location,'path').and.callThrough();
                spyOn(moastr, 'error').and.callThrough();
                spyOn(moastr, 'successMin').and.callThrough();
                userService.updateProfile(user);
            });

            it('should return an error message', function(){
                changePromiseResult(submitPromise, "failed");
                expect(moastr.error).toHaveBeenCalledWith(mockConstants.SERVER_ERROR,'left right bottom');
            });

            it('should be a successful update', function() {
                changePromiseResult(submitPromise, "resolve");
                expect(moastr.successMin).toHaveBeenCalledWith(mockConstants.UPDATE_PROFILE_OK, 'top right');
                expect($scope.isDisable).toEqual(true);
                expect($scope.profilTitle).toEqual("Profil");
            });
        });

        describe("Show user vidéos", function() {
            beforeEach(function(){
                window.user = {
                    "id":1,
                    "email":"spywen@hotmail.fr",
                    "pseudo":"spywen",
                    "roles":["AUTHENTICATED","ADMIN"]
                };
                $scope.userId = window.user.id;
            });

            it('scope variables initialized', function(){
                expect($scope.pageTitle).toEqual("Mes vidéos");
                expect($scope.orderBy).toEqual("mostrecent");
                expect($scope.page).toEqual("1");
                expect($scope.pagination).toEqual("20");
                expect($scope.userId).toEqual(1);
                expect($scope.q).toEqual("");
                expect($scope.for).toEqual("user");
            });
        });


        describe("Delete user from database", function() {
            var password = {
                password : ""
            };
            var deleteUserPromise;

            beforeEach(function(){
                password = {
                    password : "testMDP"
                };
                window.user = {
                    "id":1,
                    "email":"spywen@hotmail.fr",
                    "pseudo":"spywen",
                    "roles":["AUTHENTICATED","ADMIN"]
                };


            });

            beforeEach(function() {
                deleteUserPromise = $q.defer();
                spyOn(userService, 'deleteUser').and.returnValue(deleteUserPromise.promise);
                $scope.update();
            })

            beforeEach(function(){
                spyOn($location,'path').and.callThrough();
                spyOn(moastr, 'error').and.callThrough();
                spyOn(moastr, 'successMin').and.callThrough();
                userService.deleteUSer(password);
            });

        })
    });

});*/

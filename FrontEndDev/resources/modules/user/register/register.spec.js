describe('liztube.register', function() {

	var changePromiseResult = function (promise, status, value) {
	    if (status === 'resolve')
	        promise.resolve(value);
	    else
	        promise.reject(value);
	    $rootScope.$digest();
	};

	beforeEach(module('liztube.register'));
	beforeEach(module('liztube.dataService.authService'));
    beforeEach(module('ngRoute'));

	var $scope, $rootScope, $location, authService, $q, constants, $window, createController, $compile, form;

    var mockConstants = {
        SERVER_ERROR : 'Une erreur inattendue est survenue. Si le problème persiste veuillez contacter l\'équipe de Liztube.'
    };

    beforeEach(function() {
        module(function($provide) {
            $provide.constant('constants', mockConstants);
        });
    });

	beforeEach(inject(function (_$rootScope_, _$location_, _$route_, _authService_, _$window_, _$q_, _$compile_) {
		$rootScope =_$rootScope_;
		$location = _$location_;
		authService = _authService_;
        route = _$route_;
		$window= _$window_;
	    $q = _$q_;
        $compile = _$compile_;
	}));

    var moastr = {
        error: function(message){
            return message;
        }
    };

	beforeEach(inject(function ($controller) {
        $scope = $rootScope.$new();
        createController = function () {
            return $controller('registerCtrl', {
             '$scope': $scope,
             'moastr' : moastr
         });
        };
	}));

	beforeEach(function(){
        createController();
    });

    describe('variables', function(){

        it('variables should initialized', function() {
            expect($scope.errorRegister).toEqual('');
        });

    });


	describe('Register', function() {
		var registerPromise ;

		beforeEach(function(){
	        registerPromise = $q.defer();
            spyOn(authService, 'register').and.returnValue(registerPromise.promise);
    	});

    	beforeEach(function(){
            spyOn($rootScope, '$broadcast').and.callThrough();
            spyOn($location,'path').and.callThrough();
            spyOn(moastr, 'error').and.callThrough();
            $scope.register();
        });

    	it('should start global loading', function(){
            expect($rootScope.$broadcast).toHaveBeenCalledWith('loadingStatus', true);
        });

        it('should stop global loading when request is finished', function(){
            changePromiseResult(registerPromise, "failed");
            expect($rootScope.$broadcast).toHaveBeenCalledWith('loadingStatus', false);
        });


    	it('should return an error message', function(){
            changePromiseResult(registerPromise, "failed");
            expect(moastr.error).toHaveBeenCalledWith(mockConstants.SERVER_ERROR,'left right bottom');
        });

		it('should be a successful register', function() {
			changePromiseResult(registerPromise, "resolve");
			expect($location.path).toHaveBeenCalledWith('/login');
		});
	});

	describe('Password verify directive', function() {

        beforeEach(function(){
            var element = angular.element(
                '<form name="form">' +
                    '<input type="password" name="passwordcheck" ng-model="passwordcheck">'+
                    '<input type="password" name="passwordconfirm" ng-model="passwordconfirm">' +
                '</form>'
            );
            $scope.model = {passwordcheck: null};
            $scope.model2 = {passwordconfirm: null};
            $compile(element)($scope);
            form = $scope.form;
        });
        describe('passwordVerify', function() {
            it('should password equal to password_confirm', function() {
                form.passwordcheck.$setViewValue("test");
                form.passwordconfirm.$setViewValue("test");
                $scope.$digest();
                expect($scope.passwordconfirm).toEqual($scope.passwordcheck);
                expect(form.passwordconfirm.$valid).toBe(true);
            });
            it('should password not equal to password_confirm', function() {
                form.passwordcheck.$setViewValue("test");
                form.passwordconfirm.$setViewValue("tests");
                $scope.$digest();
                expect($scope.passwordconfirm).not.toEqual($scope.passwordcheck);
                expect(form.passwordconfirm.$valid).toBe(true);
            });
        });

	});
/*
	describe('email validation directive', function() {
		var emailVerifyPromise, isValid;

		beforeEach(function(){
            isValid = true;
            emailVerifyPromise = $q.defer();
            spyOn(authService, 'emailExist').and.returnValue(emailVerifyPromise.promise);
    	});

        beforeEach(function(){
            spyOn(moastr, 'error').and.callThrough();
            emailExistService();
        });

        it('should return an error message', function(){
            changePromiseResult(emailVerifyPromise, "failed");
            expect(moastr.error).toHaveBeenCalledWith(mockConstants.SERVER_ERROR,'left right bottom');
        });

        it('should be a successful email is valid', function() {
            changePromiseResult(emailVerifyPromise, "resolve", isValid);
            expect(true).toEqual(isValid);
        });

        beforeEach(function(){
            isValid = false;
        });

        it('should be a successful email is not valid', function() {
            changePromiseResult(emailVerifyPromise, "resolve", isValid);
            expect(false).toEqual(isValid);
        });

	});*/
/*
	describe('pseudo validation directive', function() {
        var pseudoVerifyPromise, isValid;

        beforeEach(function(){
            isValid = true;
            pseudoVerifyPromise = $q.defer();
            spyOn(authService, 'pseudoExist').and.returnValue(pseudoVerifyPromise.promise);
        });

        beforeEach(function(){
            spyOn(moastr, 'error').and.callThrough();
            pseudoVerify();
        });

        it('should return an error message', function(){
            changePromiseResult(pseudoVerifyPromise, "failed");
            expect(moastr.error).toHaveBeenCalledWith(mockConstants.SERVER_ERROR,'left right bottom');
        });

        it('should be a successful pseudo is valid', function() {
            changePromiseResult(pseudoVerifyPromise, "resolve", isValid);
            expect(true).toEqual(isValid);
        });

        beforeEach(function(){
            isValid = false;
        });

        it('should be a successful pseudois not valid', function() {
            changePromiseResult(pseudoVerifyPromise, "resolve", isValid);
            expect(false).toEqual(isValid);
        });
	});*/


});
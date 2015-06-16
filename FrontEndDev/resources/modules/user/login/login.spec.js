describe('liztube.login', function() {

	var changePromiseResult = function (promise, status, value) {
        if (status === 'resolve')
            promise.resolve(value);
        else
            promise.reject(value);
        $rootScope.$digest();
    };
    
    beforeEach(module('liztube.login'));
    beforeEach(module('liztube.dataService.authService'));

    var createController, $scope, $rootScope, $location, authService, $window, $q, constants;

    var mockConstants = {
        SERVER_ERROR : 'Une erreur inattendue est survenue. Si le problème persiste veuillez contacter l\'équipe de Liztube.',
        LOGIN_FAILED: "Login ou mot de passe incorrect"
    };

    beforeEach(function() {
        module(function($provide) {
            $provide.constant('constants', mockConstants);
        });
    });

    beforeEach(inject(function (_$rootScope_, _$location_, _authService_, _$window_, _$q_) {
    	$rootScope =_$rootScope_;
    	$location = _$location_;
    	authService = _authService_;
    	$window= _$window_;
        $q = _$q_;
    }));

    var moastr = {
        error: function(message){
            return message;
        }
    };

	beforeEach(inject(function ($controller) {
        $scope = $rootScope.$new();
        createController = function () {
            return $controller('loginCtrl', {
                '$scope': $scope,
                'moastr': moastr
         });
        };
	}));

    beforeEach(function(){
        createController();
    });

    describe('variables', function(){

        it('variables should initialized', function() {
            expect($scope.errorLogin).toEqual('');
        });

    });

    describe('submit', function() {
        var loginPromise, currentProfilPromise, currentUserResponse, windowUserNotConnected;
        windowUserNotConnected = {
            pseudo: "",
            roles:[]
        };

        beforeEach(function(){
            loginPromise = $q.defer();
            spyOn(authService, 'login').and.returnValue(loginPromise.promise);
        });

        beforeEach(function(){
            currentProfilPromise = $q.defer();
            spyOn(authService, 'currentUser').and.returnValue(currentProfilPromise.promise);
            currentUserResponse = {
                pseudo:'max',
                roles:['ROLE_ADMIN']
            };
        });

        beforeEach(function(){
            spyOn($scope, '$emit').and.callThrough();
            spyOn($location,'path').and.callThrough();
            spyOn(moastr, 'error').and.callThrough();
            $scope.submit();
        });

        afterEach(function(){
            $window.user = windowUserNotConnected;
        });

        it('should start global loading', function(){
            expect($scope.$emit).toHaveBeenCalledWith('loadingStatus', true);
        });

        it('should stop global loading when request is finished', function(){
            changePromiseResult(loginPromise, "failed");
            expect($scope.$emit).toHaveBeenCalledWith('loadingStatus', false);
        });

        it('should return an error message', function(){
            changePromiseResult(loginPromise, "failed");
            expect(moastr.error).toHaveBeenCalledWith(mockConstants.LOGIN_FAILED,'left right bottom');
        });

        it('should be a successful authentication', function(){
            changePromiseResult(loginPromise, "resolve");
            changePromiseResult(currentProfilPromise, "resolve", currentUserResponse);

            expect($window.user).toEqual(currentUserResponse);
            expect($scope.$emit).toHaveBeenCalledWith('userStatus', currentUserResponse);
            expect($location.path).toHaveBeenCalledWith('/');
        });

        it('should failed when trying to get user info', function(){
            changePromiseResult(loginPromise, "resolve");
            changePromiseResult(currentProfilPromise, "error");

            expect($window.user).toEqual(windowUserNotConnected);
            expect($scope.$emit).not.toHaveBeenCalledWith('userStatus', currentUserResponse);
            expect(moastr.error).toHaveBeenCalledWith(mockConstants.SERVER_ERROR,'left right bottom');
        });
    });
	
});
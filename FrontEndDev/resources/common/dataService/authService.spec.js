describe('liztube.dataService.authService', function(){
	var $httpBackend, authService, Restangular;

	beforeEach(module('liztube.dataService.authService'));

	beforeEach(inject(function(_$httpBackend_, _authService_, _Restangular_){
		$httpBackend = _$httpBackend_;
		Restangular = _Restangular_;
		authService = _authService_;
	}));
	
	afterEach(function() {
        $httpBackend.verifyNoOutstandingRequest();
        $httpBackend.verifyNoOutstandingExpectation();
    });

	it('should get current profil', function(){
		$httpBackend.expectGET('/auth/currentProfil').respond();
		authService.currentUser();
        $httpBackend.flush();
	});
	
	it('should post for login', function(){
		$httpBackend.expectPOST('/login').respond();
		authService.login();
        $httpBackend.flush();
	});

    it('should get for logout', function(){
        $httpBackend.expectGET('/logout').respond();
        authService.logout();
        $httpBackend.flush();
    });

    it('should post for register', function(){
        $httpBackend.expectPOST('/auth/signin').respond();
        authService.register({});
        $httpBackend.flush();
    });

    it('should post for Exist Email', function(){
        $httpBackend.expectPOST('/auth/email').respond();
        authService.emailExist();
        $httpBackend.flush();
    });

    it('should post for Exist Pseudo', function(){
        $httpBackend.expectPOST('/auth/pseudo').respond();
        authService.pseudoExist();
        $httpBackend.flush();
    });
});
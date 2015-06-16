describe('liztube.dataService.userService', function(){
    var $httpBackend, userService, Restangular;

    beforeEach(module('liztube.dataService.userService'));

    beforeEach(inject(function(_$httpBackend_, _userService_, _Restangular_){
        $httpBackend = _$httpBackend_;
        Restangular = _Restangular_;
        userService = _userService_;
    }));

    afterEach(function() {
        $httpBackend.verifyNoOutstandingRequest();
        $httpBackend.verifyNoOutstandingExpectation();
    });

    it('should get current info profil', function(){
        $httpBackend.expectGET('/user').respond();
        userService.userProfile();
        $httpBackend.flush();
    });

    it('should put for user profile', function(){
        $httpBackend.expectPUT('/user').respond();
        userService.updateProfile({});
        $httpBackend.flush();
    });

    it('should put password user ', function(){
        $httpBackend.expectPATCH('/user/password').respond();
        userService.updatePassword({});
        $httpBackend.flush();
    });

    it('should post for delete account', function(){
        $httpBackend.expectPOST('/user/delete').respond();
        userService.deleteUser({});
        $httpBackend.flush();
    });
});
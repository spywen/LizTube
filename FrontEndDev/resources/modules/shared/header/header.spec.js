describe('liztube.header', function(){

    beforeEach(module('liztube.header'));
    beforeEach(module('liztube.userStatus'));
    var createController, $scope, $rootScope, $mdSidenav, constants, $location, $compile;

    var mockConstants = {
        NO_NOTIFICATIONS_FOUND: "Vous n'avez aucune notifications"
    };

    beforeEach(function() {
        module(function($provide) {
            $provide.constant('constants', mockConstants);
        });
    });

    beforeEach(inject(function (_$rootScope_, _$location_, _$compile_) {
        $rootScope =_$rootScope_;
        $location = _$location_;
        $compile = _$compile_;
    }));

    var $mdSidenav = function(test){
        return {
            toggle: function(){
                return true;
            },
            close: function(){
                return true;
            }
        };
    };

    var moastr = {
        error: function(message){
            return message;
        }
    };

    beforeEach(inject(function ($controller) {
        $scope = $rootScope.$new();
        createController = function () {
            return $controller('headerCtrl', {
                '$scope': $scope,
                '$mdSidenav': $mdSidenav,
                'constants': mockConstants
            });
        };
    }));

    beforeEach(function(){
        createController();
    });

    describe('scope Init', function(){

        it('scope variables initialized', function(){
            expect($scope.isLoading).toEqual(false);
            expect($scope.showNotification).toEqual(false);
            expect($scope.notification).toEqual(0);
            expect($scope.noNotification).toEqual(mockConstants.NO_NOTIFICATIONS_FOUND);
        });

    });

    describe('on addNotification', function(){

        beforeEach(function(){
            $scope.showNotification = false;
            $scope.notification = 0;
            $scope.noNotification = "something";
        });

        it('should put showNotification to boolean if a addNotification event is broadcast and add 1 to the notification count', function(){
            $scope.$broadcast('addNotificationForHeader', true);
            expect($scope.showNotification).toEqual(true);
            expect($scope.notification).toEqual(1);
            expect($scope.noNotification).toEqual("");
        });

    });

    describe('on removeNotificationForHeader', function(){

        beforeEach(function(){
            $scope.showNotification = true;
        });

        it('should soustract 1 to notification count', function(){
            $scope.notification = 1;
            $scope.$broadcast('removeNotificationForHeader', true);
            expect($scope.notification).toEqual(0);
            expect($scope.noNotification).toEqual(mockConstants.NO_NOTIFICATIONS_FOUND);
        });

        it('should keep showNotification to true if some notification are left', function(){
            $scope.notification = 2;
            $scope.$broadcast('removeNotificationForHeader', true);
            expect($scope.showNotification).toEqual(true);
            expect($scope.noNotification).toEqual("");
        });

        it('should set showNotification to false if no notification are left', function(){
            $scope.notification = 1;
            $scope.$broadcast('removeNotificationForHeader', true);
            expect($scope.showNotification).toEqual(false);
            expect($scope.noNotification).toEqual(mockConstants.NO_NOTIFICATIONS_FOUND);
        });

        it('should do nothing if no notification are left', function(){
            $scope.notification = 0;
            $scope.showNotification = false;
            $scope.$broadcast('removeNotificationForHeader', true);
            expect($scope.showNotification).toEqual(false);
            expect($scope.notification).toEqual(0);
            expect($scope.noNotification).toEqual(mockConstants.NO_NOTIFICATIONS_FOUND);
        });
    });

    describe('set loading status', function(){

        it('should put isLoading to true if a loadingStatus event is broadcast with true', function(){
            $scope.isLoading = false;
            $scope.$broadcast('loadingStatusForHeader', true);
            expect($scope.isLoading).toEqual(true);
        });

        it('should put isLoading to false if a loadingStatus event is broadcast with false', function(){
            $scope.isLoading = true;
            $scope.$broadcast('loadingStatusForHeader', false);
            expect($scope.isLoading).toEqual(false);
        });

    });

    describe('on escapeChar', function(){

        beforeEach(function(){
            spyOn($scope,'escapeChar').and.callThrough();
        });

        it('Should call the escapeChar method and return a a formated string', function(){
            expect($scope.escapeChar("test")).toEqual("test");
        });

        it('Should call the escapeChar method and return a a formated string', function(){
            expect($scope.escapeChar('t>e<st/"\'&')).toEqual("test");
        });
    });

    describe('on search function', function(){

        beforeEach(function(){
            spyOn($location,'path').and.callThrough();
        });

        it('Should call redirect to / if query is null', function(){
            $scope.query = "";
            $scope.search();
            expect($location.path).toHaveBeenCalledWith('/');
        });

        it('Should call redirect to /search with params if query is not null', function(){
            $scope.query = "test";
            $scope.search();
            expect($location.path).toHaveBeenCalledWith('/search/test');
        });

    });

    describe('directive header', function() {
        it('Should directives header has text', function() {
            var directive = $compile('<header></header>')($scope);
            expect(directive.html()).toEqual("");
        });
    });
    describe('toggleRight method', function(){
        beforeEach(function(){
            spyOn($mdSidenav('right'), 'toggle').and.callThrough();
        });

        it('Should toggleRight open', function(){
            $scope.toggleRight();
            //expect($mdSidenav('right').toggle).toHaveBeenCalled();
        });
    });

    describe('closeRightBar  method', function(){
        beforeEach(function(){
            spyOn($mdSidenav('right'), 'close').and.callThrough();
        });

        it('Should toggleRight close', function(){
            $scope.closeRightBar();
            //expect($mdSidenav('right').close).toHaveBeenCalled();
        });
    });
});

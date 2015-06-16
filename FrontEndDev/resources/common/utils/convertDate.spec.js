/**
 * Created by Youcef on 29/04/2015.
 */
describe('liztube.search', function(){

    var $scope, $rootScope, $compile, form;

    beforeEach(module('liztube.date'));

    describe('convertDate directive', function() {

        beforeEach(inject(function($compile, $rootScope) {
            $scope = $rootScope;
            var element = angular.element(
                '<form name="form">' +
                '<input type="date" name="somedate" ng-model="somedate" date-to-timestamp>' +
                '</form>'
            );
            $scope.model = {somedate: null};
            $compile(element)($scope);
            form = $scope.form;
        }));
        describe('convertDate', function() {
            it('should pass with a date', function() {
                form.somedate.$setViewValue("2015-02-01");
                $scope.$digest();
                expect($scope.somedate).toEqual(1422745200000);
                expect(form.somedate.$valid).toBe(true);
            });
            it('should not pass with string', function() {
                form.somedate.$setViewValue('string');
                $scope.$digest();
                expect($scope.somedate).toBeUndefined();
                expect(form.somedate.$valid).toBe(false);
            });
        });
    });
});
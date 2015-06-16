/**
 * Created by Youcef on 11/02/2015.
 */
angular.module("liztube.footer",[

]).controller("footerCtrl", function($scope) {

})
.directive('footer', function () {
    return {
        restrict: 'E',
        controller: 'footerCtrl',
        templateUrl: "footer.html"
    };
});

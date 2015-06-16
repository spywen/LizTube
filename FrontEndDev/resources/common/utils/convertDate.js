/**
 * Created by Youcef on 05/03/2015.
 */
angular.module("liztube.date",[

]).directive('dateToTimestamp', function() {
    return {
        require: 'ngModel',
        link: function(scope, ele, attr, ngModel) {
            // view to model
            ngModel.$parsers.push(function(value) {
                var date = Date.parse(value);
                var currentTime = new Date();
                var now = Date.parse(currentTime.getMonth() + 1 + "/" + currentTime.getDate() + "/" + currentTime.getFullYear());

                if(date >= now ){
                    ngModel.$setValidity("dateToTimestamp", false);
                }else{
                    ngModel.$setValidity("dateToTimestamp", true);
                    return Date.parse(value);
                }
            });
        }
    };
});
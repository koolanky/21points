(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', 'Preferences', 'Points', 'Bloodpressure', '$state'];

    function HomeController ($scope, Principal, LoginService, Preferences, Points, Bloodpressure, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        
        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
            
            Points.thisWeek(function(data){
            	vm.pointsThisWeek=data;
            	vm.pointsPercentage=(data.points/21)*100;
            });
            
            Preferences.user(function(data){
            	vm.preferences=data;
            });
            
            Bloodpressure.last30Days(function (bpReadings) {
                vm.bpReadings = bpReadings;
                if (bpReadings.readings.length) {
                    vm.bpOptions = angular.copy(Chart.getBpChartConfig());
                    vm.bpOptions.title.text = bpReadings.period;
                    vm.bpOptions.chart.yAxis.axisLabel = "Blood Pressure";
                    var systolics, diastolics, upperValues, lowerValues;
                    systolics = [];
                    diastolics = [];
                    upperValues = [];
                    lowerValues = [];
                    bpReadings.readings.forEach(function (item) {
                        systolics.push({
                            x: new Date(item.date),
                            y: item.systolic
                        });
                        diastolics.push({
                            x: new Date(item.date),
                            y: item.diastolic
                        });
                        upperValues.push(item.systolic);
                        lowerValues.push(item.diastolic);
                    });
                    vm.bpData = [{
                        values: systolics,
                        key: 'Systolic',
                        color: '#673ab7'
                    }, {
                        values: diastolics,
                        key: 'Diastolic',
                        color: '#03a9f4'
                    }];
                    // set y scale to be 10 more than max and min
                    vm.bpOptions.chart.yDomain = [Math.min.apply(Math, lowerValues) - 10, Math.max.apply(Math, upperValues) + 10]
                }
            });
        }
        
        function register () {
            $state.go('register');
        }
    }
})();

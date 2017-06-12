(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('BloodpressureDialogController', BloodpressureDialogController);

    BloodpressureDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bloodpressure', 'User'];

    function BloodpressureDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bloodpressure, User) {
        var vm = this;

        vm.bloodpressure = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bloodpressure.id !== null) {
                Bloodpressure.update(vm.bloodpressure, onSaveSuccess, onSaveError);
            } else {
                Bloodpressure.save(vm.bloodpressure, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('21PointsApp:bloodpressureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('BloodpressureDeleteController',BloodpressureDeleteController);

    BloodpressureDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bloodpressure'];

    function BloodpressureDeleteController($uibModalInstance, entity, Bloodpressure) {
        var vm = this;

        vm.bloodpressure = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bloodpressure.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

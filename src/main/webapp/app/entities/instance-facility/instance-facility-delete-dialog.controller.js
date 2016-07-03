(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceFacilityDeleteController',InstanceFacilityDeleteController);

    InstanceFacilityDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceFacility'];

    function InstanceFacilityDeleteController($uibModalInstance, entity, InstanceFacility) {
        var vm = this;
        vm.instanceFacility = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstanceFacility.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

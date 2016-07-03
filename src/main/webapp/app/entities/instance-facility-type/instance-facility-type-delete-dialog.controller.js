(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceFacilityTypeDeleteController',InstanceFacilityTypeDeleteController);

    InstanceFacilityTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceFacilityType'];

    function InstanceFacilityTypeDeleteController($uibModalInstance, entity, InstanceFacilityType) {
        var vm = this;
        vm.instanceFacilityType = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstanceFacilityType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceRoomFacilityTypeDeleteController',InstanceRoomFacilityTypeDeleteController);

    InstanceRoomFacilityTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceRoomFacilityType'];

    function InstanceRoomFacilityTypeDeleteController($uibModalInstance, entity, InstanceRoomFacilityType) {
        var vm = this;
        vm.instanceRoomFacilityType = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstanceRoomFacilityType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

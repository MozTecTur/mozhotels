(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceRoomFacilityDeleteController',InstanceRoomFacilityDeleteController);

    InstanceRoomFacilityDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceRoomFacility'];

    function InstanceRoomFacilityDeleteController($uibModalInstance, entity, InstanceRoomFacility) {
        var vm = this;
        vm.instanceRoomFacility = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstanceRoomFacility.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

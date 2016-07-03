(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceRoomTypeDeleteController',InstanceRoomTypeDeleteController);

    InstanceRoomTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceRoomType'];

    function InstanceRoomTypeDeleteController($uibModalInstance, entity, InstanceRoomType) {
        var vm = this;
        vm.instanceRoomType = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstanceRoomType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

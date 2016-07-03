(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceTurTypeDeleteController',InstanceTurTypeDeleteController);

    InstanceTurTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceTurType'];

    function InstanceTurTypeDeleteController($uibModalInstance, entity, InstanceTurType) {
        var vm = this;
        vm.instanceTurType = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstanceTurType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

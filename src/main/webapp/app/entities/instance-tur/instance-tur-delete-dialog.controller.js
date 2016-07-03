(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceTurDeleteController',InstanceTurDeleteController);

    InstanceTurDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceTur'];

    function InstanceTurDeleteController($uibModalInstance, entity, InstanceTur) {
        var vm = this;
        vm.instanceTur = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstanceTur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

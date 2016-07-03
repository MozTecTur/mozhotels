(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstancePolicyDeleteController',InstancePolicyDeleteController);

    InstancePolicyDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstancePolicy'];

    function InstancePolicyDeleteController($uibModalInstance, entity, InstancePolicy) {
        var vm = this;
        vm.instancePolicy = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstancePolicy.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

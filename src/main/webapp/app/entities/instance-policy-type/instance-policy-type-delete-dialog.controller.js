(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstancePolicyTypeDeleteController',InstancePolicyTypeDeleteController);

    InstancePolicyTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstancePolicyType'];

    function InstancePolicyTypeDeleteController($uibModalInstance, entity, InstancePolicyType) {
        var vm = this;
        vm.instancePolicyType = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstancePolicyType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

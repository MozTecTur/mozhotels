(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceActivityTypeDeleteController',InstanceActivityTypeDeleteController);

    InstanceActivityTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceActivityType'];

    function InstanceActivityTypeDeleteController($uibModalInstance, entity, InstanceActivityType) {
        var vm = this;
        vm.instanceActivityType = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstanceActivityType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

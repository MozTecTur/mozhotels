(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceInfoDeleteController',InstanceInfoDeleteController);

    InstanceInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceInfo'];

    function InstanceInfoDeleteController($uibModalInstance, entity, InstanceInfo) {
        var vm = this;
        vm.instanceInfo = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstanceInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

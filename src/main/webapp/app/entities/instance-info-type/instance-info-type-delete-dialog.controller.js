(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceInfoTypeDeleteController',InstanceInfoTypeDeleteController);

    InstanceInfoTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceInfoType'];

    function InstanceInfoTypeDeleteController($uibModalInstance, entity, InstanceInfoType) {
        var vm = this;
        vm.instanceInfoType = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstanceInfoType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceContactDeleteController',InstanceContactDeleteController);

    InstanceContactDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceContact'];

    function InstanceContactDeleteController($uibModalInstance, entity, InstanceContact) {
        var vm = this;
        vm.instanceContact = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstanceContact.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

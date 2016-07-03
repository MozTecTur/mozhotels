(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceActivityDeleteController',InstanceActivityDeleteController);

    InstanceActivityDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceActivity'];

    function InstanceActivityDeleteController($uibModalInstance, entity, InstanceActivity) {
        var vm = this;
        vm.instanceActivity = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstanceActivity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

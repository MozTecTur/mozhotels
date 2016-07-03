(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('LocalTurDeleteController',LocalTurDeleteController);

    LocalTurDeleteController.$inject = ['$uibModalInstance', 'entity', 'LocalTur'];

    function LocalTurDeleteController($uibModalInstance, entity, LocalTur) {
        var vm = this;
        vm.localTur = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            LocalTur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

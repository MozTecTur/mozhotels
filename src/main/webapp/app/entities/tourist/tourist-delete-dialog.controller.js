(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('TouristDeleteController',TouristDeleteController);

    TouristDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tourist'];

    function TouristDeleteController($uibModalInstance, entity, Tourist) {
        var vm = this;
        vm.tourist = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Tourist.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InfoDeleteController',InfoDeleteController);

    InfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Info'];

    function InfoDeleteController($uibModalInstance, entity, Info) {
        var vm = this;
        vm.info = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Info.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

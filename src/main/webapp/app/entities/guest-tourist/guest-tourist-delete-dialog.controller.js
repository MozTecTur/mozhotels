(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('GuestTouristDeleteController',GuestTouristDeleteController);

    GuestTouristDeleteController.$inject = ['$uibModalInstance', 'entity', 'GuestTourist'];

    function GuestTouristDeleteController($uibModalInstance, entity, GuestTourist) {
        var vm = this;
        vm.guestTourist = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            GuestTourist.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

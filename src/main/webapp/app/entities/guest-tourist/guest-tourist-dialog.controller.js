(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('GuestTouristDialogController', GuestTouristDialogController);

    GuestTouristDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'GuestTourist', 'Booking'];

    function GuestTouristDialogController ($scope, $stateParams, $uibModalInstance, entity, GuestTourist, Booking) {
        var vm = this;
        vm.guestTourist = entity;
        vm.bookings = Booking.query();
        vm.load = function(id) {
            GuestTourist.get({id : id}, function(result) {
                vm.guestTourist = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:guestTouristUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.guestTourist.id !== null) {
                GuestTourist.update(vm.guestTourist, onSaveSuccess, onSaveError);
            } else {
                GuestTourist.save(vm.guestTourist, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('TouristDialogController', TouristDialogController);

    TouristDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Tourist', 'Booking', 'InstanceReview', 'User'];

    function TouristDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, Tourist, Booking, InstanceReview, User) {
        var vm = this;
        vm.tourist = entity;
        vm.bookings = Booking.query();
        vm.instancereviews = InstanceReview.query();
        vm.users = User.query();
        vm.load = function(id) {
            Tourist.get({id : id}, function(result) {
                vm.tourist = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:touristUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.tourist.id !== null) {
                Tourist.update(vm.tourist, onSaveSuccess, onSaveError);
            } else {
                Tourist.save(vm.tourist, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('BookingDialogController', BookingDialogController);

    BookingDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Booking', 'InstanceRoomType', 'InstanceRoomFacility', 'Tourist', 'GuestTourist', 'InstanceTur', 'BookingPayment'];

    function BookingDialogController ($scope, $stateParams, $uibModalInstance, entity, Booking, InstanceRoomType, InstanceRoomFacility, Tourist, GuestTourist, InstanceTur, BookingPayment) {
        var vm = this;
        vm.booking = entity;
        vm.instanceroomtypes = InstanceRoomType.query();
        vm.instanceroomfacilitys = InstanceRoomFacility.query();
        vm.tourists = Tourist.query();
        vm.guesttourists = GuestTourist.query();
        vm.instanceturs = InstanceTur.query();
        vm.bookingpayments = BookingPayment.query();
        vm.load = function(id) {
            Booking.get({id : id}, function(result) {
                vm.booking = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:bookingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.booking.id !== null) {
                Booking.update(vm.booking, onSaveSuccess, onSaveError);
            } else {
                Booking.save(vm.booking, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.checkIn = false;
        vm.datePickerOpenStatus.checkOut = false;
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.editDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();

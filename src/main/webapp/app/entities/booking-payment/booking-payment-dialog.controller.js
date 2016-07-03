(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('BookingPaymentDialogController', BookingPaymentDialogController);

    BookingPaymentDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'BookingPayment', 'Booking'];

    function BookingPaymentDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, BookingPayment, Booking) {
        var vm = this;
        vm.bookingPayment = entity;
        vm.bookings = Booking.query({filter: 'bookingpayment-is-null'});
        $q.all([vm.bookingPayment.$promise, vm.bookings.$promise]).then(function() {
            if (!vm.bookingPayment.booking || !vm.bookingPayment.booking.id) {
                return $q.reject();
            }
            return Booking.get({id : vm.bookingPayment.booking.id}).$promise;
        }).then(function(booking) {
            vm.bookings.push(booking);
        });
        vm.load = function(id) {
            BookingPayment.get({id : id}, function(result) {
                vm.bookingPayment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:bookingPaymentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.bookingPayment.id !== null) {
                BookingPayment.update(vm.bookingPayment, onSaveSuccess, onSaveError);
            } else {
                BookingPayment.save(vm.bookingPayment, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.date = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();

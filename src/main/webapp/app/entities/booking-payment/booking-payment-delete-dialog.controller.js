(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('BookingPaymentDeleteController',BookingPaymentDeleteController);

    BookingPaymentDeleteController.$inject = ['$uibModalInstance', 'entity', 'BookingPayment'];

    function BookingPaymentDeleteController($uibModalInstance, entity, BookingPayment) {
        var vm = this;
        vm.bookingPayment = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            BookingPayment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

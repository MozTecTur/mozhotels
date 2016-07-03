(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('BookingPaymentDetailController', BookingPaymentDetailController);

    BookingPaymentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'BookingPayment', 'Booking'];

    function BookingPaymentDetailController($scope, $rootScope, $stateParams, entity, BookingPayment, Booking) {
        var vm = this;
        vm.bookingPayment = entity;
        vm.load = function (id) {
            BookingPayment.get({id: id}, function(result) {
                vm.bookingPayment = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:bookingPaymentUpdate', function(event, result) {
            vm.bookingPayment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('BookingDetailController', BookingDetailController);

    BookingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Booking', 'InstanceRoomType', 'InstanceRoomFacility', 'Tourist', 'GuestTourist', 'InstanceTur', 'BookingPayment'];

    function BookingDetailController($scope, $rootScope, $stateParams, entity, Booking, InstanceRoomType, InstanceRoomFacility, Tourist, GuestTourist, InstanceTur, BookingPayment) {
        var vm = this;
        vm.booking = entity;
        vm.load = function (id) {
            Booking.get({id: id}, function(result) {
                vm.booking = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:bookingUpdate', function(event, result) {
            vm.booking = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

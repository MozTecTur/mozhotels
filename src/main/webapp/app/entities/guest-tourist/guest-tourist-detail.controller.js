(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('GuestTouristDetailController', GuestTouristDetailController);

    GuestTouristDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'GuestTourist', 'Booking'];

    function GuestTouristDetailController($scope, $rootScope, $stateParams, entity, GuestTourist, Booking) {
        var vm = this;
        vm.guestTourist = entity;
        vm.load = function (id) {
            GuestTourist.get({id: id}, function(result) {
                vm.guestTourist = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:guestTouristUpdate', function(event, result) {
            vm.guestTourist = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('TouristDetailController', TouristDetailController);

    TouristDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Tourist', 'Booking', 'InstanceReview', 'User'];

    function TouristDetailController($scope, $rootScope, $stateParams, entity, Tourist, Booking, InstanceReview, User) {
        var vm = this;
        vm.tourist = entity;
        vm.load = function (id) {
            Tourist.get({id: id}, function(result) {
                vm.tourist = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:touristUpdate', function(event, result) {
            vm.tourist = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

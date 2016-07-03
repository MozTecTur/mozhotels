(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceContactDetailController', InstanceContactDetailController);

    InstanceContactDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceContact', 'InstanceTur'];

    function InstanceContactDetailController($scope, $rootScope, $stateParams, entity, InstanceContact, InstanceTur) {
        var vm = this;
        vm.instanceContact = entity;
        vm.load = function (id) {
            InstanceContact.get({id: id}, function(result) {
                vm.instanceContact = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instanceContactUpdate', function(event, result) {
            vm.instanceContact = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

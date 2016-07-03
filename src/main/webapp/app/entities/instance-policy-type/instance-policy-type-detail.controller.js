(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstancePolicyTypeDetailController', InstancePolicyTypeDetailController);

    InstancePolicyTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstancePolicyType', 'InstancePolicy'];

    function InstancePolicyTypeDetailController($scope, $rootScope, $stateParams, entity, InstancePolicyType, InstancePolicy) {
        var vm = this;
        vm.instancePolicyType = entity;
        vm.load = function (id) {
            InstancePolicyType.get({id: id}, function(result) {
                vm.instancePolicyType = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instancePolicyTypeUpdate', function(event, result) {
            vm.instancePolicyType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

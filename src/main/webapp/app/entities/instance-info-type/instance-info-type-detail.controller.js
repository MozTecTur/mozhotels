(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceInfoTypeDetailController', InstanceInfoTypeDetailController);

    InstanceInfoTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceInfoType', 'InstanceInfo'];

    function InstanceInfoTypeDetailController($scope, $rootScope, $stateParams, entity, InstanceInfoType, InstanceInfo) {
        var vm = this;
        vm.instanceInfoType = entity;
        vm.load = function (id) {
            InstanceInfoType.get({id: id}, function(result) {
                vm.instanceInfoType = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instanceInfoTypeUpdate', function(event, result) {
            vm.instanceInfoType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('LocalTurDetailController', LocalTurDetailController);

    LocalTurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'LocalTur', 'InstanceTur', 'Province'];

    function LocalTurDetailController($scope, $rootScope, $stateParams, entity, LocalTur, InstanceTur, Province) {
        var vm = this;
        vm.localTur = entity;
        vm.load = function (id) {
            LocalTur.get({id: id}, function(result) {
                vm.localTur = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:localTurUpdate', function(event, result) {
            vm.localTur = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

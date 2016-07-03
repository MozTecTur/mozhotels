(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('InstanceTur', InstanceTur);

    InstanceTur.$inject = ['$resource'];

    function InstanceTur ($resource) {
        var resourceUrl =  'api/instance-turs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

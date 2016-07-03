(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('InstancePolicy', InstancePolicy);

    InstancePolicy.$inject = ['$resource'];

    function InstancePolicy ($resource) {
        var resourceUrl =  'api/instance-policies/:id';

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

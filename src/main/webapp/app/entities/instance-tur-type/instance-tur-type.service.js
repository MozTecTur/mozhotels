(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('InstanceTurType', InstanceTurType);

    InstanceTurType.$inject = ['$resource'];

    function InstanceTurType ($resource) {
        var resourceUrl =  'api/instance-tur-types/:id';

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

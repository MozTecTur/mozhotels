(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('InstanceInfo', InstanceInfo);

    InstanceInfo.$inject = ['$resource'];

    function InstanceInfo ($resource) {
        var resourceUrl =  'api/instance-infos/:id';

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

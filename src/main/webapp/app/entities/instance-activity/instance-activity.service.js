(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('InstanceActivity', InstanceActivity);

    InstanceActivity.$inject = ['$resource'];

    function InstanceActivity ($resource) {
        var resourceUrl =  'api/instance-activities/:id';

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

(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('InstanceFacility', InstanceFacility);

    InstanceFacility.$inject = ['$resource'];

    function InstanceFacility ($resource) {
        var resourceUrl =  'api/instance-facilities/:id';

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

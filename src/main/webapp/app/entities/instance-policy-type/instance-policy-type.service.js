(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('InstancePolicyType', InstancePolicyType);

    InstancePolicyType.$inject = ['$resource'];

    function InstancePolicyType ($resource) {
        var resourceUrl =  'api/instance-policy-types/:id';

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

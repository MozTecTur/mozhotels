(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .factory('InstancePolicyTypeSearch', InstancePolicyTypeSearch);

    InstancePolicyTypeSearch.$inject = ['$resource'];

    function InstancePolicyTypeSearch($resource) {
        var resourceUrl =  'api/_search/instance-policy-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

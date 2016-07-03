(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .factory('InstancePolicySearch', InstancePolicySearch);

    InstancePolicySearch.$inject = ['$resource'];

    function InstancePolicySearch($resource) {
        var resourceUrl =  'api/_search/instance-policies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

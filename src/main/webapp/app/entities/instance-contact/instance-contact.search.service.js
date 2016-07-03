(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .factory('InstanceContactSearch', InstanceContactSearch);

    InstanceContactSearch.$inject = ['$resource'];

    function InstanceContactSearch($resource) {
        var resourceUrl =  'api/_search/instance-contacts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

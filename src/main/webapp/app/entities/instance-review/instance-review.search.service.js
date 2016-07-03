(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .factory('InstanceReviewSearch', InstanceReviewSearch);

    InstanceReviewSearch.$inject = ['$resource'];

    function InstanceReviewSearch($resource) {
        var resourceUrl =  'api/_search/instance-reviews/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

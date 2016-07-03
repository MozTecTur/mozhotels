(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('InstanceReview', InstanceReview);

    InstanceReview.$inject = ['$resource'];

    function InstanceReview ($resource) {
        var resourceUrl =  'api/instance-reviews/:id';

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

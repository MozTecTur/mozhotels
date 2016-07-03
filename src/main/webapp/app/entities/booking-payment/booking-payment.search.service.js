(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .factory('BookingPaymentSearch', BookingPaymentSearch);

    BookingPaymentSearch.$inject = ['$resource'];

    function BookingPaymentSearch($resource) {
        var resourceUrl =  'api/_search/booking-payments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

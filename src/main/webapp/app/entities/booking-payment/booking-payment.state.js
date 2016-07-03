(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('booking-payment', {
            parent: 'entity',
            url: '/booking-payment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.bookingPayment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/booking-payment/booking-payments.html',
                    controller: 'BookingPaymentController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bookingPayment');
                    $translatePartialLoader.addPart('jPaymentType');
                    $translatePartialLoader.addPart('eCurrency');
                    $translatePartialLoader.addPart('kPaymentState');
                    $translatePartialLoader.addPart('lCardType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('booking-payment-detail', {
            parent: 'entity',
            url: '/booking-payment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.bookingPayment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/booking-payment/booking-payment-detail.html',
                    controller: 'BookingPaymentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bookingPayment');
                    $translatePartialLoader.addPart('jPaymentType');
                    $translatePartialLoader.addPart('eCurrency');
                    $translatePartialLoader.addPart('kPaymentState');
                    $translatePartialLoader.addPart('lCardType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BookingPayment', function($stateParams, BookingPayment) {
                    return BookingPayment.get({id : $stateParams.id});
                }]
            }
        })
        .state('booking-payment.new', {
            parent: 'booking-payment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking-payment/booking-payment-dialog.html',
                    controller: 'BookingPaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                currency: null,
                                amount: null,
                                date: null,
                                state: null,
                                cardHolder: null,
                                cardType: null,
                                cardNumber: null,
                                cardExpiry: null,
                                cardCCV: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('booking-payment', null, { reload: true });
                }, function() {
                    $state.go('booking-payment');
                });
            }]
        })
        .state('booking-payment.edit', {
            parent: 'booking-payment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking-payment/booking-payment-dialog.html',
                    controller: 'BookingPaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BookingPayment', function(BookingPayment) {
                            return BookingPayment.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('booking-payment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('booking-payment.delete', {
            parent: 'booking-payment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking-payment/booking-payment-delete-dialog.html',
                    controller: 'BookingPaymentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BookingPayment', function(BookingPayment) {
                            return BookingPayment.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('booking-payment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

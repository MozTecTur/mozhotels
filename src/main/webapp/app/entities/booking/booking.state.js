(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('booking', {
            parent: 'entity',
            url: '/booking?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.booking.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/booking/bookings.html',
                    controller: 'BookingController',
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
                    $translatePartialLoader.addPart('booking');
                    $translatePartialLoader.addPart('iBookingState');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('booking-detail', {
            parent: 'entity',
            url: '/booking/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.booking.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/booking/booking-detail.html',
                    controller: 'BookingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('booking');
                    $translatePartialLoader.addPart('iBookingState');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Booking', function($stateParams, Booking) {
                    return Booking.get({id : $stateParams.id});
                }]
            }
        })
        .state('booking.new', {
            parent: 'booking',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking/booking-dialog.html',
                    controller: 'BookingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                checkIn: null,
                                checkOut: null,
                                peopleAdult: null,
                                peopleChild: null,
                                createdDate: null,
                                editDate: null,
                                state: null,
                                rooms: null,
                                tax: null,
                                totalPrice: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('booking', null, { reload: true });
                }, function() {
                    $state.go('booking');
                });
            }]
        })
        .state('booking.edit', {
            parent: 'booking',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking/booking-dialog.html',
                    controller: 'BookingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Booking', function(Booking) {
                            return Booking.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('booking', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('booking.delete', {
            parent: 'booking',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking/booking-delete-dialog.html',
                    controller: 'BookingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Booking', function(Booking) {
                            return Booking.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('booking', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

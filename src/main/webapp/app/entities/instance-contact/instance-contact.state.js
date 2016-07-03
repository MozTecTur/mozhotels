(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-contact', {
            parent: 'entity',
            url: '/instance-contact?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceContact.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-contact/instance-contacts.html',
                    controller: 'InstanceContactController',
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
                    $translatePartialLoader.addPart('instanceContact');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-contact-detail', {
            parent: 'entity',
            url: '/instance-contact/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceContact.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-contact/instance-contact-detail.html',
                    controller: 'InstanceContactDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceContact');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceContact', function($stateParams, InstanceContact) {
                    return InstanceContact.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-contact.new', {
            parent: 'instance-contact',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-contact/instance-contact-dialog.html',
                    controller: 'InstanceContactDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                contactNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-contact', null, { reload: true });
                }, function() {
                    $state.go('instance-contact');
                });
            }]
        })
        .state('instance-contact.edit', {
            parent: 'instance-contact',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-contact/instance-contact-dialog.html',
                    controller: 'InstanceContactDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceContact', function(InstanceContact) {
                            return InstanceContact.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-contact', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-contact.delete', {
            parent: 'instance-contact',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-contact/instance-contact-delete-dialog.html',
                    controller: 'InstanceContactDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceContact', function(InstanceContact) {
                            return InstanceContact.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-contact', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

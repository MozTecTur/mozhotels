(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-activity', {
            parent: 'entity',
            url: '/instance-activity?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceActivity.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-activity/instance-activities.html',
                    controller: 'InstanceActivityController',
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
                    $translatePartialLoader.addPart('instanceActivity');
                    $translatePartialLoader.addPart('gActivityArea');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-activity-detail', {
            parent: 'entity',
            url: '/instance-activity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceActivity.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-activity/instance-activity-detail.html',
                    controller: 'InstanceActivityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceActivity');
                    $translatePartialLoader.addPart('gActivityArea');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceActivity', function($stateParams, InstanceActivity) {
                    return InstanceActivity.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-activity.new', {
            parent: 'instance-activity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-activity/instance-activity-dialog.html',
                    controller: 'InstanceActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceActivityName: null,
                                description: null,
                                area: null,
                                photoPrincipal: null,
                                photoPrincipalContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-activity', null, { reload: true });
                }, function() {
                    $state.go('instance-activity');
                });
            }]
        })
        .state('instance-activity.edit', {
            parent: 'instance-activity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-activity/instance-activity-dialog.html',
                    controller: 'InstanceActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceActivity', function(InstanceActivity) {
                            return InstanceActivity.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-activity', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-activity.delete', {
            parent: 'instance-activity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-activity/instance-activity-delete-dialog.html',
                    controller: 'InstanceActivityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceActivity', function(InstanceActivity) {
                            return InstanceActivity.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-activity', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

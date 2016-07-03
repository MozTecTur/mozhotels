(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-review', {
            parent: 'entity',
            url: '/instance-review?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceReview.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-review/instance-reviews.html',
                    controller: 'InstanceReviewController',
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
                    $translatePartialLoader.addPart('instanceReview');
                    $translatePartialLoader.addPart('gGEvaluation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-review-detail', {
            parent: 'entity',
            url: '/instance-review/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceReview.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-review/instance-review-detail.html',
                    controller: 'InstanceReviewDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceReview');
                    $translatePartialLoader.addPart('gGEvaluation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceReview', function($stateParams, InstanceReview) {
                    return InstanceReview.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-review.new', {
            parent: 'instance-review',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-review/instance-review-dialog.html',
                    controller: 'InstanceReviewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cleanliness: null,
                                roomConfort: null,
                                location: null,
                                serviceStaff: null,
                                sleepQuality: null,
                                valuePrice: null,
                                evaluation: null,
                                title: null,
                                comment: null,
                                active: null,
                                approval: null,
                                userApproval: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-review', null, { reload: true });
                }, function() {
                    $state.go('instance-review');
                });
            }]
        })
        .state('instance-review.edit', {
            parent: 'instance-review',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-review/instance-review-dialog.html',
                    controller: 'InstanceReviewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceReview', function(InstanceReview) {
                            return InstanceReview.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-review', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-review.delete', {
            parent: 'instance-review',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-review/instance-review-delete-dialog.html',
                    controller: 'InstanceReviewDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceReview', function(InstanceReview) {
                            return InstanceReview.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-review', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bloodpressure', {
            parent: 'entity',
            url: '/bloodpressure',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Bloodpressures'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bloodpressure/bloodpressures.html',
                    controller: 'BloodpressureController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('bloodpressure-detail', {
            parent: 'bloodpressure',
            url: '/bloodpressure/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Bloodpressure'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bloodpressure/bloodpressure-detail.html',
                    controller: 'BloodpressureDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Bloodpressure', function($stateParams, Bloodpressure) {
                    return Bloodpressure.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bloodpressure',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bloodpressure-detail.edit', {
            parent: 'bloodpressure-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bloodpressure/bloodpressure-dialog.html',
                    controller: 'BloodpressureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bloodpressure', function(Bloodpressure) {
                            return Bloodpressure.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bloodpressure.new', {
            parent: 'bloodpressure',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bloodpressure/bloodpressure-dialog.html',
                    controller: 'BloodpressureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                systolic: null,
                                diastolic: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bloodpressure', null, { reload: 'bloodpressure' });
                }, function() {
                    $state.go('bloodpressure');
                });
            }]
        })
        .state('bloodpressure.edit', {
            parent: 'bloodpressure',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bloodpressure/bloodpressure-dialog.html',
                    controller: 'BloodpressureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bloodpressure', function(Bloodpressure) {
                            return Bloodpressure.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bloodpressure', null, { reload: 'bloodpressure' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bloodpressure.delete', {
            parent: 'bloodpressure',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bloodpressure/bloodpressure-delete-dialog.html',
                    controller: 'BloodpressureDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bloodpressure', function(Bloodpressure) {
                            return Bloodpressure.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bloodpressure', null, { reload: 'bloodpressure' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

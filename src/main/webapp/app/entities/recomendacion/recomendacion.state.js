(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('recomendacion', {
            parent: 'entity',
            url: '/recomendacion',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.recomendacion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/recomendacion/recomendacions.html',
                    controller: 'RecomendacionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recomendacion');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('recomendacion-detail', {
            parent: 'entity',
            url: '/recomendacion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.recomendacion.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/recomendacion/recomendacion-detail.html',
                    controller: 'RecomendacionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recomendacion');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Recomendacion', function($stateParams, Recomendacion) {
                    return Recomendacion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'recomendacion',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('recomendacion-detail.edit', {
            parent: 'recomendacion-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recomendacion/recomendacion-dialog.html',
                    controller: 'RecomendacionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Recomendacion', function(Recomendacion) {
                            return Recomendacion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('recomendacion.new', {
            parent: 'recomendacion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recomendacion/recomendacion-dialog.html',
                    controller: 'RecomendacionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                texto: null,
                                fechaEnvio: null,
                                fechaResolucion: null,
                                aceptada: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('recomendacion', null, { reload: 'recomendacion' });
                }, function() {
                    $state.go('recomendacion');
                });
            }]
        })
        .state('recomendacion.edit', {
            parent: 'recomendacion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recomendacion/recomendacion-dialog.html',
                    controller: 'RecomendacionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Recomendacion', function(Recomendacion) {
                            return Recomendacion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('recomendacion', null, { reload: 'recomendacion' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('recomendacion.delete', {
            parent: 'recomendacion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recomendacion/recomendacion-delete-dialog.html',
                    controller: 'RecomendacionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Recomendacion', function(Recomendacion) {
                            return Recomendacion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('recomendacion', null, { reload: 'recomendacion' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

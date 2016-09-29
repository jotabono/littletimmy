(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('centro', {
            parent: 'entity',
            url: '/centro',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.centro.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/centro/centros.html',
                    controller: 'CentroController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('centro');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('centro-detail', {
            parent: 'entity',
            url: '/centro/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.centro.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/centro/centro-detail.html',
                    controller: 'CentroDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('centro');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Centro', function($stateParams, Centro) {
                    return Centro.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'centro',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('centro-detail.edit', {
            parent: 'centro-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/centro/centro-dialog.html',
                    controller: 'CentroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Centro', function(Centro) {
                            return Centro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('centro.new', {
            parent: 'centro',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/centro/centro-dialog.html',
                    controller: 'CentroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                numEmpleados: null,
                                fechaFundacion: null,
                                ubicacion: null,
                                latitud: null,
                                longitud: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('centro', null, { reload: 'centro' });
                }, function() {
                    $state.go('centro');
                });
            }]
        })
        .state('centro.edit', {
            parent: 'centro',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/centro/centro-dialog.html',
                    controller: 'CentroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Centro', function(Centro) {
                            return Centro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('centro', null, { reload: 'centro' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('centro.delete', {
            parent: 'centro',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/centro/centro-delete-dialog.html',
                    controller: 'CentroDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Centro', function(Centro) {
                            return Centro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('centro', null, { reload: 'centro' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

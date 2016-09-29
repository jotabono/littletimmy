(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('empresa', {
            parent: 'entity',
            url: '/empresa',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.empresa.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/empresa/empresas.html',
                    controller: 'EmpresaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('empresa');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('empresa-detail', {
            parent: 'entity',
            url: '/empresa/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.empresa.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/empresa/empresa-detail.html',
                    controller: 'EmpresaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('empresa');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Empresa', function($stateParams, Empresa) {
                    return Empresa.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'empresa',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('empresa-detail.edit', {
            parent: 'empresa-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/empresa/empresa-dialog.html',
                    controller: 'EmpresaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Empresa', function(Empresa) {
                            return Empresa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('empresa.new', {
            parent: 'empresa',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/empresa/empresa-dialog.html',
                    controller: 'EmpresaDialogController',
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
                    $state.go('empresa', null, { reload: 'empresa' });
                }, function() {
                    $state.go('empresa');
                });
            }]
        })
        .state('empresa.edit', {
            parent: 'empresa',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/empresa/empresa-dialog.html',
                    controller: 'EmpresaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Empresa', function(Empresa) {
                            return Empresa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('empresa', null, { reload: 'empresa' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('empresa.delete', {
            parent: 'empresa',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/empresa/empresa-delete-dialog.html',
                    controller: 'EmpresaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Empresa', function(Empresa) {
                            return Empresa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('empresa', null, { reload: 'empresa' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

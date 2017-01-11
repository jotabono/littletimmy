(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('perfil', {
                parent: 'entity',
                url: '/perfil/{user}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Perfil de Usuario'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/components/perfilusuario/perfilusuario.html',
                        controller: 'PerfilUsuarioController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    userInfo: ["User", "$stateParams", function(User, $stateParams){
                        return User.get({login:$stateParams.user});
                    }],
                    userFriends: ["User", "$stateParams", function(User, $stateParams){
                        return User.getFriendsUser({login:$stateParams.user});
                    }],
                    userTrabajo: ["User", "$stateParams", function(User, $stateParams){
                        return User.getTrabajosUser({login:$stateParams.user});
                    }],
                    userEstudio: ["User", "$stateParams", function(User, $stateParams){
                        return User.getEstudiosUser({login:$stateParams.user});
                    }],
                    userFriendship: ["Friend_user", "$stateParams", function (Friend_user, $stateParams) {
                         return Friend_user.isFriendship({login:$stateParams.user});
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('perfil-recomendaciones', {
                parent: 'perfil',
                url: '/trabajo/{id_trabajo}/recomendations',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/components/perfilusuario/recomendaciones.html',
                        controller: 'RecoTrabajoController',
                        controllerAs: 'vm',
                        backdrop: true,
                        backdropClass: 'backdropBlurred',
                        size: 'lg',
                        resolve: {
                            recomendaciones: ["Recomendacion", function(Recomendacion){
                                return Recomendacion.recomendacionesTrabajo({id_trabajo:$stateParams.id_trabajo});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('perfil', {login:$stateParams.login}, { reload: false });
                    }, function() {
                        $state.go('perfil', {login:$stateParams.login});
                    });
                }]
            })
            .state('perfil-recomendar', {
                parent: 'perfil-recomendaciones',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/components/perfilusuario/recomendar.html',
                        controller: 'RecoDialogController',
                        controllerAs: 'vm',
                        backdrop: true,
                        backdropClass: 'backdropBlurred',
                        size: 'md'
                    }).result.then(function(res) {
                        console.log(res);
                        $state.go('perfil-recomendaciones', {id_trabajo:$stateParams.id_trabajo}, { reload: false });
                    }, function() {
                        $state.go('perfil', {login:$stateParams.login});
                    });
                }]
            });
    }
})();

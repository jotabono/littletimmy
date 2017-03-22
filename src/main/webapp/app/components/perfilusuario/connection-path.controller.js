(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('ConnectionPathController', ConnectionPathController);

    ConnectionPathController.$inject = ['$http', '$scope', '$stateParams', '$uibModalInstance', '$state', '$rootScope', 'User'];

    function ConnectionPathController ($http, $scope, $stateParams, $uibModalInstance, $state, $rootScope, User) {
        var vm = this;

        vm.userSrcLogin = $stateParams.user_src;
        vm.userDstLogin = $stateParams.user;

        vm.userSrc = User.get({login: vm.userSrcLogin});
        vm.userDst = User.get({login: vm.userDstLogin});

        vm.irPerfil = irPerfil;

        function irPerfil(userDest){
            $uibModalInstance.dismiss();
            var a = document.createElement("a");
            a.href = "/#/perfil/"+userDest;
            a.click();
        }

        vm.loaded = false;

        vm.userConnectionPath = [];

        vm.userSrc.$promise.then(function(resultado){
            vm.userDst.$promise.then(function(res){
                $http({
                    method: 'GET',
                    url: '/api/users/connectionDegree/from/'+resultado.id+'/to/'+res.id
                }).then(function successCallback(res) {
                    vm.userConnectionPath = res.data;
                    vm.loaded = true;
                });
            });
        });


    }
})();

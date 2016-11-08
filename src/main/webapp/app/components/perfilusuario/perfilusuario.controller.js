(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('PerfilUsuarioController', PerfilUsuarioController);

    PerfilUsuarioController.$inject = ['$scope', '$state', 'userInfo', '$http'];

    function PerfilUsuarioController ($scope, $state, userInfo, $http) {
        var vm = this;
        vm.user = userInfo;
        vm.estudios = [];
        vm.estudioactuales = [];
        vm.estudioactual = [];

        vm.trabajos = [];
        vm.trabajoactuales = [];
        vm.trabajoactual = [];

        vm.user.$promise.then(function () {
            $http({
                method: 'GET',
                url: 'api/estudios/user/'+vm.user.login
            }).then(function successcallback(response){
                vm.estudios = response.data;
                for(var i=0; i<vm.estudios.length; i++){
                    if (vm.estudios[i].actualmente) {
                        vm.estudioactuales.push(vm.estudios[i]);
                    }
                }
                vm.estudioactual = vm.estudioactuales[vm.estudioactuales.length-1];
                if (vm.estudioactual == null){
                    vm.estudioactual = vm.estudios[vm.estudios.length-1];
                }
            })
        }),
        vm.user.$promise.then(function () {
            $http({
                method: 'GET',
                url: 'api/trabajos/usuario/'+vm.user.login
            }).then(function successcallback(response){
                console.log(response)
                vm.trabajos = response.data;
                for(var i=0; i<vm.trabajos.length; i++){
                        if (vm.trabajos[i].actualmente) {
                            vm.trabajoactuales.push(vm.trabajos[i]);
                        }
                }
                    vm.trabajoactual = vm.trabajoactuales[vm.trabajoactuales.length-1];
            })
        })
    }
})();

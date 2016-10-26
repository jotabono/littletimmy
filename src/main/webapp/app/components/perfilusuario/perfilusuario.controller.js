(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('PerfilUsuarioController', PerfilUsuarioController);

    PerfilUsuarioController.$inject = ['$scope', '$state', 'userInfo'];

    function PerfilUsuarioController ($scope, $state, userInfo) {
        var vm = this;
        vm.user = userInfo;
    }
})();

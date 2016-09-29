(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('EmpresaController', EmpresaController);

    EmpresaController.$inject = ['$scope', '$state', 'Empresa', 'EmpresaSearch'];

    function EmpresaController ($scope, $state, Empresa, EmpresaSearch) {
        var vm = this;
        
        vm.empresas = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Empresa.query(function(result) {
                vm.empresas = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EmpresaSearch.query({query: vm.searchQuery}, function(result) {
                vm.empresas = result;
            });
        }    }
})();

(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('CentroController', CentroController);

    CentroController.$inject = ['$scope', '$state', 'Centro', 'CentroSearch'];

    function CentroController ($scope, $state, Centro, CentroSearch) {
        var vm = this;
        
        vm.centros = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Centro.query(function(result) {
                vm.centros = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CentroSearch.query({query: vm.searchQuery}, function(result) {
                vm.centros = result;
            });
        }    }
})();

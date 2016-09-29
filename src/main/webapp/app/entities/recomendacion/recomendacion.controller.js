(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('RecomendacionController', RecomendacionController);

    RecomendacionController.$inject = ['$scope', '$state', 'Recomendacion', 'RecomendacionSearch'];

    function RecomendacionController ($scope, $state, Recomendacion, RecomendacionSearch) {
        var vm = this;
        
        vm.recomendacions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Recomendacion.query(function(result) {
                vm.recomendacions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            RecomendacionSearch.query({query: vm.searchQuery}, function(result) {
                vm.recomendacions = result;
            });
        }    }
})();

(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('TrabajoController', TrabajoController);

    TrabajoController.$inject = ['$scope', '$state', 'DataUtils', 'Trabajo', 'TrabajoSearch'];

    function TrabajoController ($scope, $state, DataUtils, Trabajo, TrabajoSearch) {
        var vm = this;
        
        vm.trabajos = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Trabajo.query(function(result) {
                vm.trabajos = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TrabajoSearch.query({query: vm.searchQuery}, function(result) {
                vm.trabajos = result;
            });
        }    }
})();

(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('EstudiosController', EstudiosController);

    EstudiosController.$inject = ['$scope', '$state', 'DataUtils', 'Estudios', 'EstudiosSearch'];

    function EstudiosController ($scope, $state, DataUtils, Estudios, EstudiosSearch) {
        var vm = this;
        
        vm.estudios = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Estudios.query(function(result) {
                vm.estudios = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EstudiosSearch.query({query: vm.searchQuery}, function(result) {
                vm.estudios = result;
            });
        }    }
})();

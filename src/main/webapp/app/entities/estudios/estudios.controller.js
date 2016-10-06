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
        vm.loadAll = function() {
            Estudios.query(function(result) {
                vm.estudios = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EstudiosSearch.query({query: vm.searchQuery}, function(result) {
                vm.estudios = result;
            });
        };
        vm.loadAll();
        
    }
})();

(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('EstudiosDetailController', EstudiosDetailController);

    EstudiosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Estudios', 'Centro'];

    function EstudiosDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Estudios, Centro) {
        var vm = this;
        vm.estudios = entity;
        vm.load = function (id) {
            Estudios.get({id: id}, function(result) {
                vm.estudios = result;
            });
        };
        var unsubscribe = $rootScope.$on('littletimmyApp:estudiosUpdate', function(event, result) {
            vm.estudios = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();

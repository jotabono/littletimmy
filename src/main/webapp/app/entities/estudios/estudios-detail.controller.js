(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('EstudiosDetailController', EstudiosDetailController);

    EstudiosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Estudios', 'Centro', 'User'];

    function EstudiosDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Estudios, Centro, User) {
        var vm = this;

        vm.estudios = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('littletimmyApp:estudiosUpdate', function(event, result) {
            vm.estudios = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('TrabajoDetailController', TrabajoDetailController);

    TrabajoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Trabajo'];

    function TrabajoDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Trabajo) {
        var vm = this;

        vm.trabajo = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('littletimmyApp:trabajoUpdate', function(event, result) {
            vm.trabajo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

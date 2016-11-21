(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('RecoTrabajoController', RecoTrabajoController);

    RecoTrabajoController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'recomendaciones'];

    function RecoTrabajoController ($timeout, $scope, $stateParams, $uibModalInstance, recomendaciones) {
        var vm = this;

       vm.recomendaciones = recomendaciones;
    }
})();

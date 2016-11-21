(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('RecoTrabajoController', RecoTrabajoController);

    RecoTrabajoController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'recomendaciones'];

    function RecoTrabajoController ($timeout, $scope, $stateParams, $uibModalInstance, recomendaciones) {
        var vm = this;

       	vm.recomendaciones = recomendaciones;
       	vm.trabajo;
       	vm.recomendaciones.$promise.then(function(res){
       		vm.trabajo = vm.recomendaciones[0].trabajo;
       	});
    }
})();

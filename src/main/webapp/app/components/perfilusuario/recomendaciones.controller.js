(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('RecoTrabajoController', RecoTrabajoController);

    RecoTrabajoController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'recomendaciones', '$state'];

    function RecoTrabajoController ($timeout, $scope, $stateParams, $uibModalInstance, recomendaciones, $state) {
        var vm = this;

       	vm.recomendaciones = recomendaciones;
       	vm.trabajo;
       	vm.recomendaciones.$promise.then(function(res){
       		vm.trabajo = vm.recomendaciones[0].trabajo;
       	});
        vm.toProfile = toProfile;

        function clear () {
          $uibModalInstance.dismiss('cancel');
        }
        function toProfile(){
          $uibModalInstance.dismiss('cancel');
        }
    }
})();

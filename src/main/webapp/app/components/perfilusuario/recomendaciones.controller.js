(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('RecoTrabajoController', RecoTrabajoController);

    RecoTrabajoController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'recomendaciones', '$state', '$rootScope'];

    function RecoTrabajoController ($timeout, $scope, $stateParams, $uibModalInstance, recomendaciones, $state, $rootScope) {
        var vm = this;

       	vm.recomendaciones = recomendaciones;
       	vm.trabajo;
       	vm.recomendaciones.$promise.then(function(res){
       		vm.trabajo = vm.recomendaciones[0].trabajo;
       		$rootScope.trabajo = vm.trabajo;
       	});
        vm.toProfile = toProfile;

        $rootScope.$on('littletimmyApp:nuevaRecomendacion', function (e,res){
            vm.recomendaciones.push(res);
        });

        function clear () {
          $uibModalInstance.dismiss('cancel');
        }
        function toProfile(){
          $uibModalInstance.dismiss('cancel');
        }
    }
})();

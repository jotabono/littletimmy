(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('RecomendacionDialogController', RecomendacionDialogController);

    RecomendacionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Recomendacion'];

    function RecomendacionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Recomendacion) {
        var vm = this;

        vm.recomendacion = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.recomendacion.id !== null) {
                Recomendacion.update(vm.recomendacion, onSaveSuccess, onSaveError);
            } else {
                Recomendacion.save(vm.recomendacion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('littletimmyApp:recomendacionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaEnvio = false;
        vm.datePickerOpenStatus.fechaResolucion = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('CentroDialogController', CentroDialogController);

    CentroDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Centro'];

    function CentroDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Centro) {
        var vm = this;

        vm.centro = entity;
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
            if (vm.centro.id !== null) {
                Centro.update(vm.centro, onSaveSuccess, onSaveError);
            } else {
                Centro.save(vm.centro, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('littletimmyApp:centroUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaFundacion = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

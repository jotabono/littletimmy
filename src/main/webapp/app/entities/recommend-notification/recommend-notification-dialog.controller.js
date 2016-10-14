(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('Recommend_notificationDialogController', Recommend_notificationDialogController);

    Recommend_notificationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Recommend_notification', 'User'];

    function Recommend_notificationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Recommend_notification, User) {
        var vm = this;

        vm.recommend_notification = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.recommend_notification.id !== null) {
                Recommend_notification.update(vm.recommend_notification, onSaveSuccess, onSaveError);
            } else {
                Recommend_notification.save(vm.recommend_notification, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('littletimmyApp:recommend_notificationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaRecibida = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

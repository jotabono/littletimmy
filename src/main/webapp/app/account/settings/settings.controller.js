(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['Principal', 'Auth', 'JhiLanguageService', '$translate'];

    function SettingsController (Principal, Auth, JhiLanguageService, $translate) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login,
                imagen: account.imagen,
                fecha_nacimiento: account.fecha_nacimiento,
                web_personal: account.web_personal,
                facebook: account.facebook,
                twitter: account.twitter,
                skype: account.skype,
                carta_presentacion: account.carta_presentacion,
                correo_alternativo: account.correo_alternativo,
                dni: account.dni,
                domicilio: account.domicilio
            };
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });

        function save () {
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function(account) {
                    vm.settingsAccount = copyAccount(account);
                });
                JhiLanguageService.getCurrent().then(function(current) {
                    if (vm.settingsAccount.langKey !== current) {
                        $translate.use(vm.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }
    }
})();
